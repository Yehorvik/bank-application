package com.example.bankSystem.dto;

import com.example.bankSystem.model.Account;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A DTO for the {@link Account} entity
 */
@Data
public class AccountDto implements Serializable {
    private final UUID accountId;
    private final BigDecimal balance;
    private final UUID clientClientId;
}