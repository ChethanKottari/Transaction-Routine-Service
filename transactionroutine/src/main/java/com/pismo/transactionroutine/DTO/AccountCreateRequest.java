package com.pismo.transactionroutine.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreateRequest {

    @NotNull(message = "Document Number is required")
    private String documentId;

    @NotNull(message = "Credit Limit needs to passed in")
    @PositiveOrZero
    private BigDecimal creditLimit;

}
