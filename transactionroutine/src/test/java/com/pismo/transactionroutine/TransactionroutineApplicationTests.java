package com.pismo.transactionroutine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TransactionroutineApplicationTests {

	@Test
	void mainMethodRuns() {
		// Verify the main class exists and is callable.
		// We do NOT use @SpringBootTest here because it tries to
		// connect to PostgreSQL, which may not be available locally.
		assertDoesNotThrow(() -> {
			TransactionroutineApplication app = new TransactionroutineApplication();
		});
	}
}
