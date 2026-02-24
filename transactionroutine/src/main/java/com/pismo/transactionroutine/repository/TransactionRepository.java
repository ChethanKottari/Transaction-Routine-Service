package com.pismo.transactionroutine.repository;


import com.pismo.transactionroutine.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdempotencyKey(UUID idempotencyKey);
}