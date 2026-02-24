package com.pismo.transactionroutine.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Account id is required")
    @JsonProperty("account_id")
    private Long accountId;

    @NotBlank(message = "Operation type is required")
    @JsonProperty("operation_type")
    private String operationType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Max 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Idempotency key is required")
    @JsonProperty("idempotency_key")
    private UUID idempotencyKey;
}