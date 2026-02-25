package com.pismo.transactionroutine.controller;

import com.pismo.transactionroutine.DTO.AccountCreateRequest;
import com.pismo.transactionroutine.DTO.AccountCreateResponse;
import com.pismo.transactionroutine.DTO.AccountResponse;
import com.pismo.transactionroutine.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountsControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountsController accountsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- GET /accounts/{accountId} ----------

    @Test
    void getAccountById_returnsAccount_whenFound() {
        // Arrange — use builder (Lombok @Builder on AccountResponse)
        AccountResponse response = AccountResponse.builder()
                .accountId(1L)
                .documentId("12345678")
                .build();

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(response));

        // Act
        ResponseEntity<AccountResponse> result = accountsController.getAccountByDocumentId(1L);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getAccountId());
        assertEquals("12345678", result.getBody().getDocumentId());

        verify(accountService, times(1)).getAccountById(1L);
    }

    @Test
    void getAccountById_returns404_whenNotFound() {
        // Arrange
        when(accountService.getAccountById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<AccountResponse> result = accountsController.getAccountByDocumentId(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(accountService, times(1)).getAccountById(99L);
    }

    // ---------- POST /accounts ----------

    @Test
    void createAccount_returnsCreatedAccount() {
        // Arrange — use setter (Lombok @Data generates no-arg + setters)
        AccountCreateRequest request = new AccountCreateRequest();
        request.setDocumentId("12345678");

        // use builder (Lombok @Builder on AccountCreateResponse)
        AccountCreateResponse response = AccountCreateResponse.builder()
                .accountId(1L)
                .documentId("12345678")
                .build();

        when(accountService.createAccount(request)).thenReturn(response);

        // Act
        ResponseEntity<AccountCreateResponse> result = accountsController.createAccountWithDocumentId(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getAccountId());
        assertEquals("12345678", result.getBody().getDocumentId());

        verify(accountService, times(1)).createAccount(request);
    }
}
