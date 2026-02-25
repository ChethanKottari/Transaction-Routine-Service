package com.pismo.transactionroutine.controller;

import com.pismo.transactionroutine.DTO.TransactionRequest;
import com.pismo.transactionroutine.DTO.TransactionResponse;
import com.pismo.transactionroutine.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionsControllerTest {

    @Mock
    private TransactionService transactionService;

    // TransactionsController uses @RequiredArgsConstructor (Lombok),
    // so we pass the mock via constructor instead of @InjectMocks
    private TransactionsController transactionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionsController = new TransactionsController(transactionService);
    }

    @Test
    void create_returnsCreatedTransaction() {
        // Arrange — use all-args constructor (Lombok @AllArgsConstructor on
        // TransactionRequest)
        UUID idempotencyKey = UUID.randomUUID();
        TransactionRequest request = new TransactionRequest(
                1L,
                "PURCHASE",
                new BigDecimal("100.50"),
                idempotencyKey);

        // use builder (Lombok @Builder on TransactionResponse)
        LocalDateTime now = LocalDateTime.now();
        TransactionResponse response = TransactionResponse.builder()
                .transactionId(10L)
                .accountId(1L)
                .operationId(1)
                .amount(new BigDecimal("-100.50"))
                .eventDate(now)
                .build();

        when(transactionService.create(request)).thenReturn(response);

        // Act
        ResponseEntity<TransactionResponse> result = transactionsController.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(10L, result.getBody().getTransactionId());
        assertEquals(1L, result.getBody().getAccountId());
        assertEquals(1, result.getBody().getOperationId());
        assertEquals(new BigDecimal("-100.50"), result.getBody().getAmount());
        assertEquals(now, result.getBody().getEventDate());

        verify(transactionService, times(1)).create(request);
    }
}
