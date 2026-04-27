package com.zentrabank.bank_api.modules.transfer.repository;

import com.zentrabank.bank_api.modules.transfer.entity.Transfer;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @NullMarked
    @Override
    Optional<Transfer> findById(UUID transferId);
}