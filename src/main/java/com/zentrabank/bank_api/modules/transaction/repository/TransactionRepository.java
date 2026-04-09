package com.zentrabank.bank_api.modules.transaction.repository;

import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}