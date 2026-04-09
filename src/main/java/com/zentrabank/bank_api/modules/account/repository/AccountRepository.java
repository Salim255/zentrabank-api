package com.zentrabank.bank_api.modules.account.repository;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    // Tell Spring Data JPA to use a PESSIMISTIC WRITE lock on this query
    // This means: when this account is fetched, the database row will be locked
    // for writing until the current transaction commits or rolls back.
    // Other transactions trying to update the same row will wait.
    // This is crucial for banking operations to prevent race conditions
    // like double withdrawals or balance inconsistencies.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    // Define a custom query to fetch an Account by its ID
    // 'a' is just an alias for the Account entity
    // The lock above will apply to this row
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    // Map the query parameter ':id' to the method argument 'id'
    // Returns the Account entity with the given ID
    // This should be called inside a @Transactional service method
    // to ensure the lock is held until the transaction completes.
    Account findAccountForUpdate(@Param("id") UUID id);

    Optional<Account> findAccountByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByUserId(UUID userId);
}