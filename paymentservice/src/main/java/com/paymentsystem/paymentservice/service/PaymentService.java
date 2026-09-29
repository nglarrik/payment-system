package com.paymentsystem.paymentservice.service;

import tools.jackson.databind.json.JsonMapper;
import com.paymentsystem.paymentservice.domain.IdempotencyKey;
import com.paymentsystem.paymentservice.domain.OutboxEvent;
import com.paymentsystem.paymentservice.domain.Transaction;
import com.paymentsystem.paymentservice.dto.CreateTransactionRequest;
import com.paymentsystem.paymentservice.dto.TransactionResponse;
import com.paymentsystem.paymentservice.repository.IdempotencyKeyRepository;
import com.paymentsystem.paymentservice.repository.OutboxEventRepository;
import com.paymentsystem.paymentservice.repository.TransactionRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final JsonMapper jsonMapper;

    public PaymentService(TransactionRepository transactionRepository,
            OutboxEventRepository outboxEventRepository,
            IdempotencyKeyRepository idempotencyKeyRepository,
            JsonMapper jsonMapper) {
        this.transactionRepository = transactionRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public TransactionResponse createTransaction(String idempotencyKeyValue, CreateTransactionRequest request) {

        // Шаг 1: проверяем, не обрабатывали ли мы уже этот запрос
        Optional<IdempotencyKey> existing = idempotencyKeyRepository.findById(idempotencyKeyValue);
        if (existing.isPresent()) {
            IdempotencyKey key = existing.get();
            if ("COMPLETED".equals(key.getStatus())) {
                return deserializeResponse(key.getResponseBody());
            }
            // Если PROCESSING или FAILED — для пет-проекта пока просто бросаем ошибку.
            // В реальной системе тут была бы более сложная логика (retry-after, 409 и т.д.)
            throw new IllegalStateException(
                    "Request with this idempotency key is already being processed or failed: " + idempotencyKeyValue);
        }

        // Шаг 2: создаём запись idempotency key со статусом PROCESSING
        Instant now = Instant.now();
        IdempotencyKey key = new IdempotencyKey();
        key.setIdempotencyKey(idempotencyKeyValue);
        key.setStatus("PROCESSING");
        key.setCreatedAt(now);
        key.setUpdatedAt(now);
        idempotencyKeyRepository.save(key);

        // Шаг 3: создаём саму транзакцию
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setFromAccountId(request.getFromAccountId());
        transaction.setToAccountId(request.getToAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        transactionRepository.save(transaction);

        // Шаг 4: пишем событие в outbox
        String payloadJson = toJson(Map.of(
                "transactionId", transaction.getId().toString(),
                "fromAccountId", transaction.getFromAccountId().toString(),
                "toAccountId", transaction.getToAccountId().toString(),
                "amount", transaction.getAmount().toString(),
                "currency", transaction.getCurrency()));

        OutboxEvent event = new OutboxEvent();
        event.setId(UUID.randomUUID());
        event.setAggregateId(transaction.getId());
        event.setEventType("TRANSACTION_CREATED");
        event.setPayload(payloadJson);
        event.setStatus("PENDING");
        event.setCreatedAt(now);
        outboxEventRepository.save(event);

        // Шаг 5: формируем финальный ответ
        TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCreatedAt());

        // Шаг 6: обновляем idempotency key на COMPLETED с сохранённым ответом
        key.setStatus("COMPLETED");
        key.setResponseBody(toJson(response));
        key.setUpdatedAt(Instant.now());
        idempotencyKeyRepository.save(key);

        return response;
    }

    private String toJson(Object obj) {
        try {
            return jsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    private TransactionResponse deserializeResponse(String json) {
        try {
            return jsonMapper.readValue(json, TransactionResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize stored response", e);
        }
    }
}