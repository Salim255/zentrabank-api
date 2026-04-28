package com.zentrabank.bank_api.modules.transaction.repository;

import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByAccountAccountNumber(String accountNumber);
}