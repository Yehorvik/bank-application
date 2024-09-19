package com.example.bankSystem.controller;

import com.example.bankSystem.dto.AccountDto;
import com.example.bankSystem.dto.ClientDto;
import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.ClientNotFoundException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Client;
import com.example.bankSystem.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/management")
public class AccountManagementController {

    public AccountManagementController(ModelMapper mapper, AccountService accountService) {
        this.mapper = mapper;
        this.accountService = accountService;
    }

    private final ModelMapper mapper;
    private final AccountService accountService;


    @PostMapping("/account")
    public ResponseEntity createAccount(@RequestBody AccountDto accountDto, HttpServletRequest req) throws ClientNotFoundException {
        Account createdAccount = mapper.map(accountDto, Account.class);
        UUID accountId = accountService.createAccount(createdAccount);

        return ResponseEntity.ok(Map.of("link", ServletUriComponentsBuilder.fromCurrentContextPath().path("/account/{id}").build(String.valueOf(accountId))));
    }


    @PostMapping("/client")
    public ResponseEntity createClient(@RequestBody ClientDto clientDto){
        Client createdClient = mapper.map(clientDto, Client.class);
        UUID clientId = accountService.createClient(createdClient);
        return ResponseEntity.ok(Map.of("link", ServletUriComponentsBuilder.fromCurrentContextPath().path("/account/{id}").build(String.valueOf(clientId))));
    }

    @GetMapping("/account")
    public ResponseEntity<Collection<AccountDto>> getAllAccounts(){
        Collection<AccountDto> accountList = accountService.getAllAccounts().stream().map(e->mapper.map(e, AccountDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") UUID id) throws AccountNotFoundException {
        Account account = accountService.getAccountDetailsById(id);
        return ResponseEntity.ok(mapper.map(account, AccountDto.class));
    }

}
