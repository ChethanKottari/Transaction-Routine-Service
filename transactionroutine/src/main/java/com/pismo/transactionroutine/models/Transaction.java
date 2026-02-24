package com.pismo.transactionroutine.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private UUID idempotencyKey;

    @Column(name = "account_id",nullable = false)
    private Long accountId;

    @Column(name = "operation_id", nullable = false)
    private Integer operationId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime createdAt;
}
