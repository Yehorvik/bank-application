package com.example.bankSystem.model;

import com.example.bankSystem.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private UUID transactionId;

    @CreationTimestamp
    @Column(name = "transaction_time")
    LocalDateTime time;

    @ManyToOne
    @JoinColumn(referencedColumnName = "account_id", name = "account_id", nullable = false)
    private Account account;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @OneToOne(optional = true)
    private TransferDetails transferDetails;
}
