package com.pismo.transactionroutine.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {

    private Long accountId;
    private String documentId;
    private BigDecimal creditLimit;

}
