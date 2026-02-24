package com.pismo.transactionroutine.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("operation_type")
    private Integer operationId;

    private BigDecimal amount;

    @JsonProperty("event_date")
    private LocalDateTime eventDate;
}
