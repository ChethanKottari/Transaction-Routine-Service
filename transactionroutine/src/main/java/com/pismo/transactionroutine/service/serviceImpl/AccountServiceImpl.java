package com.pismo.transactionroutine.service.serviceImpl;

import com.pismo.transactionroutine.DTO.AccountCreateRequest;
import com.pismo.transactionroutine.DTO.AccountCreateResponse;
import com.pismo.transactionroutine.DTO.AccountResponse;
import com.pismo.transactionroutine.exception.DuplicateDocumentIdException;
import com.pismo.transactionroutine.models.Account;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Optional<AccountResponse> getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .map(account -> AccountResponse.builder().documentId(account.getDocumentId())
                        .accountId(account.getAccountId())
                        .build());
    }

    @Override
    @Transactional
    public AccountCreateResponse createAccount(AccountCreateRequest accountCreateRequest) throws DuplicateDocumentIdException{
        // first check if the document exists
        if(accountRepository.existsByDocumentId(accountCreateRequest.getDocumentId())){
            // if yes throw and expection
            throw new DuplicateDocumentIdException(accountCreateRequest.getDocumentId());
        }
        Account account = new Account();
        account.setDocumentId(accountCreateRequest.getDocumentId());

        Account savedAccount = accountRepository.save(account);

        return AccountCreateResponse.builder().
                accountId(savedAccount.getAccountId()).
                documentId(savedAccount.getDocumentId()).
                build();
    }


}
