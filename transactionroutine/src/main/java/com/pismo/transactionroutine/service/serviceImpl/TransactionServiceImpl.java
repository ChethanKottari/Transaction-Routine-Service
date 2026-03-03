package com.pismo.transactionroutine.service.serviceImpl;

import com.pismo.transactionroutine.DTO.TransactionRequest;
import com.pismo.transactionroutine.DTO.TransactionResponse;
import com.pismo.transactionroutine.Enum.OperationTypeMapper;
import com.pismo.transactionroutine.exception.AccountNotFoundException;
import com.pismo.transactionroutine.exception.InvalidOperationTypeException;
import com.pismo.transactionroutine.models.Account;
import com.pismo.transactionroutine.models.Transaction;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.repository.TransactionRepository;
import com.pismo.transactionroutine.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponse create(TransactionRequest request) throws Exception {

        //  idempotency check
        Optional<Transaction> existing =
                transactionRepository
                        .findByIdempotencyKey(
                                request.getIdempotencyKey());

        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        Account account = accountRepository.findById(
                        request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                request.getAccountId()));

        int operationId =
                OperationTypeMapper
                        .fromString(
                                request.getOperationType())
                        .getId();

        BigDecimal signedAmount =
                applySign(
                        request.getAmount(),
                        operationId);

        // updating the credit limit
        if(operationId <= 3){
            if(account.getCreditLimit().compareTo(signedAmount.abs())<0){
                throw new InvalidOperationTypeException("credit limit is low");
            }
        }

        Account accountLocked = accountRepository.getAccountWithLock(request.getAccountId());

        accountRepository.updateAccontCreditLimit(account.getCreditLimit().add(signedAmount),account.getAccountId());


        Transaction transaction =
                Transaction.builder()
                        .idempotencyKey(
                                request.getIdempotencyKey())
                        .accountId(
                                request.getAccountId())
                        .operationId(operationId)
                        .amount(signedAmount)
                        .build();

        try {
            transaction =
                    transactionRepository.save(transaction);

        } catch (DataIntegrityViolationException ex) {

            Transaction existingTransaction =
                    transactionRepository
                            .findByIdempotencyKey(
                                    request.getIdempotencyKey())
                            .orElseThrow();

            return toResponse(existingTransaction);
        }

        return toResponse(transaction);
    }

    private TransactionResponse toResponse(
            Transaction t) {

        return TransactionResponse.builder()
                .transactionId(
                        t.getTransactionId())
                .accountId(
                        t.getAccountId())
                .operationId(
                        t.getOperationId())
                .amount(
                        t.getAmount())
                .eventDate(
                        t.getCreatedAt())
                .build();
    }

    private BigDecimal applySign(
            BigDecimal amount,
            int operationId) {

        return switch (operationId) {

            case 1,2,3 -> amount.abs().negate(); // debit negatives
            case 4     -> amount.abs();          // credit positives

            default ->
                    throw new RuntimeException(
                            "Invalid operation id");
        };
    }
}
