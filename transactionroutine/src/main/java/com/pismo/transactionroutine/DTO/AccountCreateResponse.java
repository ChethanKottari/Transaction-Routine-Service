package com.pismo.transactionroutine.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreateResponse {

    private Long accountId;
    private String documentId;
}
