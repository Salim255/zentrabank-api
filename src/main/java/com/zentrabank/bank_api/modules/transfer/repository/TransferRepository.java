package com.zentrabank.bank_api.modules.transfer.repository;

import com.zentrabank.bank_api.modules.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}