package com.example.bankSystem.service;

import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.ClientNotFoundException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Client;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

public interface AccountService {
    public UUID createAccount(Account account) throws ClientNotFoundException;
    public UUID createClient(Client client);
    public Account getAccountDetailsById(UUID accountId) throws AccountNotFoundException;
    public Collection<Account> getAllAccounts();
}
