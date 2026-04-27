package com.zentrabank.bank_api.modules.transaction.entity;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.transfer.entity.Transfer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_account_id", columnList = "account_id" )
        }
)
public class Transaction {
    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    // ONE account per transaction (mandatory)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Link back to the transfer (optional for deposits/withdrawals)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    // Type of transaction: DEPOSIT, WITHDRAWAL, TRANSFER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Amount involved in the transaction
    @Column(nullable = false)
    private BigDecimal amount;

    // Balance after the transaction (optional but useful for audit/logging)
    @Column(nullable = false)
    private BigDecimal postTransactionBalance;

    // Timestamp when the transaction occurred
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    // JPA: runs BEFORE the entity is inserted into the database
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }


    // Pre-update hook to automatically update timestamps (if needed)
    @PreUpdate
    public void preUpdate() {
        // could add updatedAt if you track updates for transactions
        this.updatedAt = Instant.now();
    }
}
