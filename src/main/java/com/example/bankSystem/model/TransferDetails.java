package com.example.bankSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.UUID;

@Entity(name = "transfer_details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferDetails {

    @Id
    @Column(name = "transfer_details_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transferDetailsId;

    @OneToOne
    @JoinColumn(referencedColumnName = "account_id")
    private Account receiver;

}
