package com.example.bankSystem.service;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.ClientNotFoundException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Client;
import com.example.bankSystem.repository.AccountRepository;
import com.example.bankSystem.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public UUID createAccount(Account account) throws ClientNotFoundException {
//        Account account1 = new Account(acc)
        account.setClient(clientRepository.findById(account.getClient().getClientId()).orElseThrow(() -> new ClientNotFoundException("cannot find Client by given credentials")));
        Account account1 = accountRepository.save(account);
        return account1.getAccountId();
    }

    @Override
    public UUID createClient(Client client) {
        Client client1 = clientRepository.save(client);
        return client1.getClientId();
    }

    @Override
    public Account getAccountDetailsById(UUID accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("cannot find account by given id"));
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
