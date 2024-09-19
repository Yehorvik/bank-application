package com.example.bankSystem.dto;

import com.example.bankSystem.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * A DTO for the {@link Account} entity
 */
@Data
@AllArgsConstructor
public class AccountDto implements Serializable {
    private final Optional<UUID> accountId;
    @Positive
    private final BigDecimal balance;
    @NotNull
    private final UUID clientId;
}