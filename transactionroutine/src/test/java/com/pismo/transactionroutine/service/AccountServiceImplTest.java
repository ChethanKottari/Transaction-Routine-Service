package com.pismo.transactionroutine.service;

import com.pismo.transactionroutine.DTO.AccountCreateRequest;
import com.pismo.transactionroutine.DTO.AccountCreateResponse;
import com.pismo.transactionroutine.DTO.AccountResponse;
import com.pismo.transactionroutine.exception.DuplicateDocumentIdException;
import com.pismo.transactionroutine.models.Account;
import com.pismo.transactionroutine.repository.AccountRepository;
import com.pismo.transactionroutine.service.serviceImpl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- getAccountById ----------

    @Test
    void getAccountById_returnsAccount_whenFound() {
        // Arrange — use builder (Lombok @Builder on Account)
        Account account = Account.builder()
                .accountId(1L)
                .documentId("12345678")
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        Optional<AccountResponse> result = accountService.getAccountById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getAccountId());
        assertEquals("12345678", result.get().getDocumentId());

        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void getAccountById_returnsEmpty_whenNotFound() {
        // Arrange
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<AccountResponse> result = accountService.getAccountById(99L);

        // Assert
        assertTrue(result.isEmpty());

        verify(accountRepository, times(1)).findById(99L);
    }

    // ---------- createAccount ----------

    @Test
    void createAccount_savesAndReturnsResponse() {
        // Arrange — use setter (Lombok @Data generates no-arg + setters)
        AccountCreateRequest request = new AccountCreateRequest();
        request.setDocumentId("12345678");

        // account returned after save — use builder
        Account savedAccount = Account.builder()
                .accountId(1L)
                .documentId("12345678")
                .build();

        when(accountRepository.existsByDocumentId("12345678")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        AccountCreateResponse result = accountService.createAccount(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
        assertEquals("12345678", result.getDocumentId());

        verify(accountRepository, times(1)).existsByDocumentId("12345678");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_throwsException_whenDocumentIdAlreadyExists() {
        // Arrange
        AccountCreateRequest request = new AccountCreateRequest();
        request.setDocumentId("12345678");

        when(accountRepository.existsByDocumentId("12345678")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateDocumentIdException.class,
                () -> accountService.createAccount(request));

        verify(accountRepository, times(1)).existsByDocumentId("12345678");
        verify(accountRepository, never()).save(any(Account.class));
    }
}
