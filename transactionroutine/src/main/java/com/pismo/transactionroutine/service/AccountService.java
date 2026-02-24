package com.pismo.transactionroutine.service;


import com.pismo.transactionroutine.DTO.AccountCreateRequest;
import com.pismo.transactionroutine.DTO.AccountCreateResponse;
import com.pismo.transactionroutine.DTO.AccountResponse;
import com.pismo.transactionroutine.models.Account;

import java.util.Optional;

public interface AccountService {

    Optional<AccountResponse> getAccountById(Long accountId);
    AccountCreateResponse createAccount(AccountCreateRequest accountCreateRequest);

}
