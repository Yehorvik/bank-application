package com.example.bankSystem;

import com.example.bankSystem.controller.AccountTransactionsController;
import com.example.bankSystem.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankSystemApplicationTests {

	@Autowired
	private AccountTransactionsController transactionsController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(transactionsController);
	}

}
