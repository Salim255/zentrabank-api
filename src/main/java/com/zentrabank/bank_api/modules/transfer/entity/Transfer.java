package com.zentrabank.bank_api.modules.transfer.entity;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.transfer.entity.TransferStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Entity
@Table(
        name = "transfers",
        indexes = {
                // Fast lookup of transfers by sender account (very common)
                @Index(name = "idx_from_account_id", columnList = "from_account_id"),

                // Fast lookup of transfers by receiver account
                @Index(name = "idx_to_account_id", columnList = "to_account_id")
        }
)
public class Transfer {
    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    // Sender account (money goes OUT from here)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    // Receiver account (money goes INTO here)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    // Amount being transferred
    @Column(nullable = false)
    private BigDecimal amount;

    // Currency (important for future multi-currency support)
    @Column(length = 3, nullable = false)
    private String currency = "USD";

    // Status of transfer lifecycle
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    // EXTERNAL TRANSFER FIELDS
    @Column(name = "external_iban", length = 34)
    private String externalIban; // null for internal transfers

    @Column(name = "external_bic", length = 11)
    private String externalBic; // null for internal transfers

    @Column(name = "external_recipient_name", length = 80)
    private String externalRecipientName; // null for internal transfers

    // Unique reference for external systems / idempotency
    @Column(unique = true, nullable = false)
    private String referenceId;

    // Optional description (e.g., "Rent payment")
    @Column(length = 255)
    private String description;

    // When transfer was created
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // When transfer was completed (if success)
    private Instant completedAt;

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