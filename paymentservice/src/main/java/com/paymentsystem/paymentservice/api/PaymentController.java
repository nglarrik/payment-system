package com.paymentsystem.paymentservice.api;

import com.paymentsystem.paymentservice.dto.CreateTransactionRequest;
import com.paymentsystem.paymentservice.dto.TransactionResponse;
import com.paymentsystem.paymentservice.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody CreateTransactionRequest request) {

        TransactionResponse response = paymentService.createTransaction(idempotencyKey, request);
        return ResponseEntity.ok(response);
    }
}