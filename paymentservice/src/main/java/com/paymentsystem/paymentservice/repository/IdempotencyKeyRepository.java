package com.paymentsystem.paymentservice.repository;

import com.paymentsystem.paymentservice.domain.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, String> { }
