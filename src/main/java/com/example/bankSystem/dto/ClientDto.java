package com.example.bankSystem.dto;

import com.example.bankSystem.model.Client;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Client} entity
 */
@Data
public class ClientDto implements Serializable {
    @NotBlank
    private final String firstName;
    @NotBlank
    private final String lastName;
}