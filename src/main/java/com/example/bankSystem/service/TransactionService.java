package com.example.bankSystem.service;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.NegativeBalanceException;
import com.example.bankSystem.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {
    void withdrawFundsFromAccount(Transaction transaction) throws AccountNotFoundException, NegativeBalanceException;
    void depositFundsIntoAccount(Transaction transaction) throws AccountNotFoundException;
    void transferFundsBetweenAccounts(UUID receiver, Transaction transaction) throws AccountNotFoundException, NegativeBalanceException;
}
