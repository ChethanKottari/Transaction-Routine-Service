package com.pismo.transactionroutine.controller;

import com.pismo.transactionroutine.DTO.AccountCreateRequest;
import com.pismo.transactionroutine.DTO.AccountCreateResponse;
import com.pismo.transactionroutine.DTO.AccountResponse;
import com.pismo.transactionroutine.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @GetMapping("accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccountByDocumentId(@PathVariable Long accountId){
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE ACCOUNT WITH DCUMENT NUMBER -> RETURNS THE ACCOUNT IF AND DOCUMENT ID

    @PostMapping("/accounts")
    public ResponseEntity<AccountCreateResponse> createAccountWithDocumentId(@Valid @RequestBody AccountCreateRequest accountCreateRequest){
        AccountCreateResponse response = accountService.createAccount(accountCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // CREATE A NEW TRANSACTION(POST) TO THE DATABASE AND MAKE IT THREAD SAFE AND IDEMPOTENT


}
