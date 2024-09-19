package com.example.bankSystem.service;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.NegativeBalanceException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Client;
import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.model.TransferDetails;
import com.example.bankSystem.model.enums.TransactionType;
import com.example.bankSystem.repository.AccountRepository;
import com.example.bankSystem.repository.TransactionRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup(){

    }


    @SneakyThrows
    @DisplayName("testing of withdrawing logic")
    @Test
    void givenPositiveBalance_whenWithdrawingCorrectAmount_thenSubstractFromBalance() {

        Account account = new Account(UUID.randomUUID(), BigDecimal.valueOf(1000), new Client(UUID.randomUUID(), "Henry", "Smith"));
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
//        Mockito.when(accountRepository.save(Mockito.any())).thenReturn();
        transactionService.withdrawFundsFromAccount(transaction);
        Mockito.verify(transactionRepository).save(transaction);
        Mockito.verify(accountRepository).save(account);
    }

    @SneakyThrows
    @DisplayName("testing of withdrawing logic throw exception")
    @Test
    void givenWrongAccount_whenWithdrawingCorrectAmount_thenThrowException() {
        Account fakeAccount = new Account();
        fakeAccount.setAccountId(UUID.randomUUID());
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccount(fakeAccount);
        transaction.setType(TransactionType.WITHDRAW);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
//        Mockito.when(accountRepository.save(Mockito.any())).thenReturn();
        assertThrows(AccountNotFoundException.class, () ->transactionService.withdrawFundsFromAccount(transaction));
        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
    }

    @SneakyThrows
    @DisplayName("testing of withdrawing logic with negative funds after withdrawing")
    @Test
    void givenAccountWithLackOfFunds_whenWithdrawingIncorrectAmount_thenThrowException() {
        Account account = Mockito.spy(new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
//        Mockito.when(accountRepository.save(Mockito.any())).thenReturn();
        assertThrows(NegativeBalanceException.class, () ->transactionService.withdrawFundsFromAccount(transaction));
        Mockito.verify(account).withdrawFunds(BigDecimal.valueOf(100));
        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
        Mockito.verify(accountRepository, Mockito.never()).save(account);
    }

    @Test
    void givenValidAccount_whenDepositFunds_increaseAccountBalance() throws AccountNotFoundException {

        Account account = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
//        Mockito.when(accountRepository.save(Mockito.any())).thenReturn();
        transactionService.depositFundsIntoAccount(transaction);


        Mockito.verify(transactionRepository).save(transaction);
        Mockito.verify(accountRepository).save(account);
        Mockito.verify(account).depositFunds(BigDecimal.valueOf(100));
    }

    @Test
    void givenInvalidAccount_whenDepositFunds_thenThrowException() {
        Account account = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccount(account);
        transaction.setType(TransactionType.WITHDRAW);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
//        Mockito.when(accountRepository.save(Mockito.any())).thenReturn();
        assertThrows(AccountNotFoundException.class, () -> transactionService.depositFundsIntoAccount(transaction));

        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
        Mockito.verify(accountRepository, Mockito.never()).save(account);
        Mockito.verify(account, Mockito.never()).depositFunds(BigDecimal.valueOf(100));
    }

    @SneakyThrows
    @Test
    @DisplayName("testing of transferring funds")
    void givenValidSenderAndReceiver_whenTransferFunds_thenUpdateBalancesAndMakeATransaction(){
        Account sender = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Account receiver = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Joel")));
        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setAmount(BigDecimal.valueOf( 40));
        transaction.setTransferDetails(new TransferDetails(UUID.randomUUID(), receiver));
        Mockito.when(accountRepository.findById(sender.getAccountId())).thenReturn(Optional.of(sender));
        Mockito.when(accountRepository.findById(receiver.getAccountId())).thenReturn(Optional.of(receiver));
        transactionService.transferFundsBetweenAccounts(receiver.getAccountId(), transaction);

        Mockito.verify(sender).withdrawFunds(BigDecimal.valueOf(40));
        Mockito.verify(receiver).depositFunds(BigDecimal.valueOf(40));
        Mockito.verify(transactionRepository).save(transaction);
        Mockito.verify(accountRepository).save(receiver);
        Mockito.verify(accountRepository).save(sender);
    }
    @SneakyThrows
    @Test
    @DisplayName("testing of transferring funds with in")
    void givenValidSenderAndReceiver_whenTransferIncorrectFunds_throwException(){
        Account sender = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Account receiver = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Joel")));
        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setAmount(BigDecimal.valueOf( 60));
        transaction.setTransferDetails(new TransferDetails(UUID.randomUUID(), receiver));
        Mockito.when(accountRepository.findById(sender.getAccountId())).thenReturn(Optional.of(sender));
        Mockito.when(accountRepository.findById(receiver.getAccountId())).thenReturn(Optional.of(receiver));
        assertThrows(NegativeBalanceException.class, () -> transactionService.transferFundsBetweenAccounts(receiver.getAccountId(), transaction));

        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
        Mockito.verify(accountRepository,Mockito.never()).save(receiver);
        Mockito.verify(accountRepository,Mockito.never()).save(sender);
    }

    @SneakyThrows
    @Test
    @DisplayName("testing of transferring funds with invalid sender")
    void givenInvalidSenderAndReceiver_whenTransferFunds_throwException(){
        Account sender = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Account receiver = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Joel")));
        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setAmount(BigDecimal.valueOf(40));
        transaction.setTransferDetails(new TransferDetails(UUID.randomUUID(), receiver));
        Mockito.when(accountRepository.findById(sender.getAccountId())).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findById(receiver.getAccountId())).thenReturn(Optional.of(receiver));
        assertThrows(AccountNotFoundException.class, () -> transactionService.transferFundsBetweenAccounts(receiver.getAccountId(), transaction));

        Mockito.verify(sender, Mockito.never()).withdrawFunds(BigDecimal.valueOf(40));
        Mockito.verify(receiver, Mockito.never()).depositFunds(BigDecimal.valueOf(40));
        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
        Mockito.verify(accountRepository, Mockito.never()).save(receiver);
        Mockito.verify(accountRepository, Mockito.never()).save(sender);
    }

    @SneakyThrows
    @Test
    @DisplayName("testing of transferring funds")
    void givenValidSenderAndInvalidReceiver_whenTransferFunds_throwException(){
        Account sender = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Smith")));
        Account receiver = Mockito.spy( new Account(UUID.randomUUID(), BigDecimal.valueOf(50), new Client(UUID.randomUUID(), "Henry", "Joel")));
        Transaction transaction = new Transaction();
        transaction.setAccount(sender);
        transaction.setAmount(BigDecimal.valueOf( 40));
        transaction.setTransferDetails(new TransferDetails(UUID.randomUUID(), receiver));
//        Mockito.when(accountRepository.findById(sender.getAccountId())).thenReturn(Optional.of(sender));
        Mockito.when(accountRepository.findById(receiver.getAccountId())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transactionService.transferFundsBetweenAccounts(receiver.getAccountId(), transaction));

        Mockito.verify(sender, Mockito.never()).withdrawFunds(BigDecimal.valueOf(40));
        Mockito.verify(receiver,Mockito.never()).depositFunds(BigDecimal.valueOf(40));
        Mockito.verify(transactionRepository, Mockito.never()).save(transaction);
        Mockito.verify(accountRepository, Mockito.never()).save(receiver);
        Mockito.verify(accountRepository, Mockito.never()).save(sender);
    }
}