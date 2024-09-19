package com.example.bankSystem.controller;

import com.example.bankSystem.dto.TransactionDto;
import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AccountTransactionsController.class)
@Import(ModelMapper.class)
class AccountTransactionsControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    TransactionDto validDTO;

    TransactionDto invalidDTO;

    @BeforeEach
    public void setup(){
        validDTO = new TransactionDto(UUID.randomUUID(), BigDecimal.valueOf(1000));
        invalidDTO = new TransactionDto(UUID.randomUUID(), BigDecimal.valueOf(-1000));
    }

    @Test
    void depositFunds() throws Exception, AccountNotFoundException {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/transferring/deposit").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(validDTO)));
        res.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.containsString("deposit")));
    }

    @Test
    void depositFundsWithError() throws Exception, AccountNotFoundException {

        Mockito.doThrow(new AccountNotFoundException("cannot find the account")).when(transactionService).depositFundsIntoAccount(Mockito.any());
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/transferring/withdraw").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(validDTO)));
        res.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ex", Matchers.containsString("cannot find the account")));
    }

    @Test
    void withdrawFunds() throws Exception {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/transferring/withdraw").
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(invalidDTO)));
        res.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void transferFunds() throws Exception {
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.post("/api/transferring/transfer/{id}", String.valueOf(UUID.randomUUID())).
                contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(validDTO)));
        res.andExpect(MockMvcResultMatchers.status().isOk());
    }
}