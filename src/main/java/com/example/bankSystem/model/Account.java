package com.example.bankSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID accountId;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public BigDecimal withdrawFunds(BigDecimal amount){
        return balance.subtract(amount);
    }

    public BigDecimal depositFunds(BigDecimal amount){
        return balance.add(amount);
    }
}
