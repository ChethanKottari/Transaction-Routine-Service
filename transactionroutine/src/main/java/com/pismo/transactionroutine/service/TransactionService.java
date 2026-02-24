package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.DTO.TransactionRequest;
import com.pismo.transactionroutine.DTO.TransactionResponse;

public interface TransactionService {

    TransactionResponse create(TransactionRequest request);
}
