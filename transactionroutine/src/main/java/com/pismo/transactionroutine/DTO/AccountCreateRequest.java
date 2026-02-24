package com.pismo.transactionroutine.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCreateRequest {

    @NotNull(message = "Document Number is required")
    private String documentId;
}
