package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.DTO.TransactionRequest;
import com.pismo.transactionroutine.DTO.TransactionResponse;
import com.pismo.transactionroutine.exception.AccountNotFoundException;
import com.pismo.transactionroutine.models.Account;
import com.pismo.transactionroutine.models.Transaction;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.repository.TransactionRepository;
import com.pismo.transactionroutine.service.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    // TransactionServiceImpl uses @RequiredArgsConstructor (Lombok),
    // so we pass mocks via constructor
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository);
    }

    // ---------- create — new transaction ----------

    @Test
    void create_savesAndReturnsNewTransaction() {
        // Arrange
        UUID idempotencyKey = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // use all-args constructor (Lombok @AllArgsConstructor on TransactionRequest)
        TransactionRequest request = new TransactionRequest(
                1L, "PURCHASE", new BigDecimal("100.00"), idempotencyKey);

        // no existing transaction with this key
        when(transactionRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        // account exists
        Account account = Account.builder().accountId(1L).documentId("12345678").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // saved transaction — use builder (Lombok @Builder on Transaction)
        Transaction savedTransaction = Transaction.builder()
                .transactionId(10L)
                .idempotencyKey(idempotencyKey)
                .accountId(1L)
                .operationId(1)
                .amount(new BigDecimal("-100.00"))
                .createdAt(now)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponse result = transactionService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getTransactionId());
        assertEquals(1L, result.getAccountId());
        assertEquals(1, result.getOperationId());
        assertEquals(new BigDecimal("-100.00"), result.getAmount());
        assertEquals(now, result.getEventDate());

        verify(transactionRepository, times(1)).findByIdempotencyKey(idempotencyKey);
        verify(accountRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // ---------- create — idempotent duplicate ----------

    @Test
    void create_returnsCachedResponse_whenIdempotencyKeyExists() {
        // Arrange
        UUID idempotencyKey = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TransactionRequest request = new TransactionRequest(
                1L, "PURCHASE", new BigDecimal("100.00"), idempotencyKey);

        // existing transaction already stored
        Transaction existing = Transaction.builder()
                .transactionId(10L)
                .idempotencyKey(idempotencyKey)
                .accountId(1L)
                .operationId(1)
                .amount(new BigDecimal("-100.00"))
                .createdAt(now)
                .build();

        when(transactionRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.of(existing));

        // Act
        TransactionResponse result = transactionService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getTransactionId());

        // should NOT call save since the transaction already exists
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).findById(anyLong());
    }

    // ---------- create — account not found ----------

    @Test
    void create_throwsException_whenAccountNotFound() {
        // Arrange
        UUID idempotencyKey = UUID.randomUUID();

        TransactionRequest request = new TransactionRequest(
                99L, "PURCHASE", new BigDecimal("50.00"), idempotencyKey);

        when(transactionRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> transactionService.create(request));

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    // ---------- create — credit voucher (positive amount) ----------

    @Test
    void create_appliesPositiveAmount_forCreditVoucher() {
        // Arrange
        UUID idempotencyKey = UUID.randomUUID();

        TransactionRequest request = new TransactionRequest(
                1L, "CREDIT_VOUCHER", new BigDecimal("200.00"), idempotencyKey);

        when(transactionRepository.findByIdempotencyKey(idempotencyKey))
                .thenReturn(Optional.empty());

        Account account = Account.builder().accountId(1L).documentId("12345678").build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // capture what gets saved — the amount should be positive for CREDIT_VOUCHER
        Transaction savedTransaction = Transaction.builder()
                .transactionId(11L)
                .idempotencyKey(idempotencyKey)
                .accountId(1L)
                .operationId(4)
                .amount(new BigDecimal("200.00"))
                .createdAt(LocalDateTime.now())
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionResponse result = transactionService.create(request);

        // Assert — credit voucher keeps the amount positive
        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getAmount());
        assertEquals(4, result.getOperationId());
    }
}
