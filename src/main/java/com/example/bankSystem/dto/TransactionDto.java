package com.example.bankSystem.dto;

import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.model.enums.TransactionType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A DTO for the {@link Transaction} entity
 */
@Data
public class TransactionDto implements Serializable {
    private final UUID accountAccountId;
    private final BigDecimal amount;
    private TransactionType type;
}