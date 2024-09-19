package com.example.bankSystem.dto;

import com.example.bankSystem.model.Client;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Client} entity
 */
@Data
public class ClientDto implements Serializable {
    private final String firstName;
    private final String lastName;
}