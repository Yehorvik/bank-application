package com.example.bankSystem.controller;

import com.example.bankSystem.dto.AccountDto;
import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.exceptions.ClientNotFoundException;
import com.example.bankSystem.model.Account;
import com.example.bankSystem.model.Client;
import com.example.bankSystem.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AccountManagementController.class)
@Import(ModelMapper.class)
class AccountManagementControllerWebMockTest {

    public static final String CANNOT_FIND_CLIENT_WITH_GIVEN_ID = "cannot find client with given Id!";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    private AccountService service;

    AccountDto account;
    AccountDto invalidAccount;

    AccountDto invalidBalanceAccount;

    List<Account> accounts;

    @BeforeEach
    public void setup(){
        account = new AccountDto(Optional.of(UUID.randomUUID()),  BigDecimal.valueOf( 1000), UUID.randomUUID());
        Client client = new Client(UUID.randomUUID(),"Mary", "Stivens");
        accounts = List.of(new Account(UUID.randomUUID(), BigDecimal.valueOf(1000), client),
                new Account(UUID.randomUUID(), BigDecimal.valueOf(0), client),
        new Account(UUID.randomUUID(), BigDecimal.valueOf(200), client));
        invalidAccount = new AccountDto(null, BigDecimal.valueOf( 1000), null);
        invalidBalanceAccount = new AccountDto(null, BigDecimal.valueOf( -1000), UUID.randomUUID());
    }

    @Test
    void givenValidAccount_whenPerformingPost_returnCreatedAccountUUID() throws Exception, ClientNotFoundException {
        UUID randomId = UUID.randomUUID();
        Mockito.when(service.createAccount(any())).thenReturn(randomId);
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.
                post("/api/management/account").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(account)));
        res.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.link",
                        CoreMatchers.containsString(randomId.toString()))
                );

    }

    @SneakyThrows
    @Test
    void creatingInValidAccount_whenPerformingPost_returnError() {
        Mockito.when(service.createAccount(any())).thenThrow(new ClientNotFoundException("invalid account name"));
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.
                post("/api/management/account").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(account)));
        res.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.ex",
                        CoreMatchers.notNullValue()
                ));
    }

    @Test
    void getAllAccounts() throws Exception {

        Mockito.when(service.getAllAccounts()).thenReturn(accounts);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.
                get("/api/management/account"));
        res.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$",
                        hasSize(3)
                ));
    }

    @Test
    void getAccountById() throws AccountNotFoundException, Exception {
        Mockito.when(service.getAccountDetailsById(any(UUID.class))).thenReturn(accounts.get(0));
        mockMvc.perform(MockMvcRequestBuilders.
                get("/api/management/account/{id}",String.valueOf(UUID.randomUUID())))
                .andExpect(jsonPath("$.accountId", is(String.valueOf( accounts.get(0).getAccountId()))))
                .andExpect(jsonPath("$.balance", is(accounts.get(0).getBalance().intValue())))
                .andExpect(jsonPath("$.clientId", is(accounts.get(0).getClient().getClientId().toString())));
    }
}