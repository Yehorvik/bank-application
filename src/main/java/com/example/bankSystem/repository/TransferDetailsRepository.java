package com.example.bankSystem.repository;

import com.example.bankSystem.model.TransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferDetailsRepository extends JpaRepository<TransferDetails, UUID> {
}