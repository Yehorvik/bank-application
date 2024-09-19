package com.example.bankSystem.controller;

import com.example.bankSystem.dto.TransactionDto;
import com.example.bankSystem.exceptions.AccountNotFoundException;
import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.model.enums.TransactionType;
import com.example.bankSystem.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transferring")
public class AccountTransactionsController {
    private final ModelMapper mapper;
    private final TransactionService transactionService;

    public AccountTransactionsController(ModelMapper mapper, TransactionService transactionService) {
        this.mapper = mapper;
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@RequestBody TransactionDto transactionDto) throws AccountNotFoundException {
        transactionDto.setType(TransactionType.DEPOSIT);
        transactionService.depositFundsIntoAccount(mapper.map(transactionDto, Transaction.class));
        return ResponseEntity.ok("transaction of depositing " + transactionDto.getAmount().toString() + " successfully completed");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@RequestBody TransactionDto transactionDto) throws AccountNotFoundException {
        transactionDto.setType(TransactionType.WITHDRAW);
        transactionService.depositFundsIntoAccount(mapper.map(transactionDto, Transaction.class));
        return ResponseEntity.ok("transaction of withdrawing " + transactionDto.getAmount().toString() + " successfully completed");
    }

    @PostMapping("/transfer/{id}")
    public ResponseEntity<String> transferFunds(@PathVariable(name = "id") UUID id, @RequestBody TransactionDto transactionDto) throws AccountNotFoundException {
        transactionDto.setType(TransactionType.WITHDRAW);
        transactionService.depositFundsIntoAccount(mapper.map(transactionDto, Transaction.class));
        return ResponseEntity.ok("transaction of sending of " + transactionDto.getAmount().toString() + " to "  + id + " successfully completed" );
    }

}
