package com.example.bankSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "transfer_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferDetails {

    @Id
    @Column(name = "transfer_details_id")
    private UUID transferDetailsId;

    @OneToOne
    @JoinColumn(referencedColumnName = "account_id")
    private Account receiver;

    @OneToOne
    @JoinColumn(referencedColumnName = "transaction_id")
    private Transaction transaction;
}
