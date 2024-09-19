package com.example.bankSystem.dto;

import com.example.bankSystem.model.Transaction;
import com.example.bankSystem.model.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A DTO for the {@link Transaction} entity
 */
@Data
public class TransactionDto implements Serializable {
    @NotNull
    private final UUID accountId;
    @Positive
    private final BigDecimal amount;
}