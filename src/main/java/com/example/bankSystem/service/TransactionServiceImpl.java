package com.example.bankSystem.service;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.NegativeBalanceException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.model.TransferDetails;
import com.example.bankSystem.model.enums.TransactionType;
import com.example.bankSystem.repository.AccountRepository;
import com.example.bankSystem.repository.TransactionRepository;
import com.example.bankSystem.repository.TransferDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferDetailsRepository transferDetailsRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, TransferDetailsRepository transferDetailsRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferDetailsRepository = transferDetailsRepository;
    }

    @Override
    @Transactional
    public void withdrawFundsFromAccount(Transaction transaction) throws AccountNotFoundException, NegativeBalanceException {
        transaction.setType(TransactionType.WITHDRAW);
        Account account = accountRepository.findById(transaction.getAccount().getAccountId()).orElseThrow(() -> new AccountNotFoundException("cannot find the account with a given name"));
        if(account.withdrawFunds(transaction.getAmount()).longValue() < 0)
            throw new NegativeBalanceException("balance of account after withdrawing is negative! aborting operation");
        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void depositFundsIntoAccount(Transaction transaction) throws AccountNotFoundException {
        transaction.setType(TransactionType.DEPOSIT);
        Account account = accountRepository.findById(transaction.getAccount().getAccountId()).orElseThrow(() -> new AccountNotFoundException("cannot find the account with a given name"));
        account.depositFunds(transaction.getAmount());
        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void transferFundsBetweenAccounts(UUID receiver, Transaction transaction) throws AccountNotFoundException, NegativeBalanceException {
        transaction.setType(TransactionType.TRANSFER);
        Account receiver1 = accountRepository.findById(receiver).orElseThrow(() -> new AccountNotFoundException("cannot find the account with a given name"));
        Account sender = accountRepository.findById(transaction.getAccount().getAccountId()).orElseThrow(() -> new AccountNotFoundException("cannot find the account with a given name"));
        if(sender.withdrawFunds(transaction.getAmount()).longValue() < 0)
            throw new NegativeBalanceException("balance of account after withdrawing is negative! aborting operation");
        receiver1.depositFunds(transaction.getAmount());
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setReceiver(receiver1);
        transaction.setTransferDetails(transferDetails);
        accountRepository.save(receiver1);
        accountRepository.save(sender);
        transactionRepository.save(transaction);
    }
}
