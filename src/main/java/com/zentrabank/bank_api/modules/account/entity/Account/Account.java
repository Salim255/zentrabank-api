package com.zentrabank.bank_api.modules.account.entity.Account;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "accounts",
        indexes = {
                // Index for fast lookup by account number (common query)
                @Index(
                        name = "idx_account_number",
                        columnList = "accountNumber",
                        unique = true
                )
        }
)
public class Account {
    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    // Unique human-readable account number (bank-wide unique)
    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    // Account balance stored as BigDecimal for precise monetary operations
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // Type of account (CHECKING, SAVINGS, CREDIT, LOAN) - allows different product handling
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    // Status of the account (ACTIVE, BLOCKED, CLOSED, FROZEN)
    // Useful for locking, freezing, or closing accounts without deleting them
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    // Link to the owner of the account
    // Many accounts can belong to one user
    // Many-to-One relationship: Many BankAccounts can belong to one User
    // Example: A user can have multiple accounts (checking, savings, etc.)
    @ManyToOne(
            fetch = FetchType.LAZY, // LAZY loading: the User object is only
            // fetched from DB when explicitly accessed
            // This improves performance, especially when fetching many accounts

            optional = false // optional=false means every BankAccount MUST have an associated User
    )
    @JoinColumn(
            name = "user_id", // The foreign key column in the 'bank_accounts'
            // table that references User's primary key
            nullable = false // Database-level constraint: cannot create a BankAccount without a user
    )
    private User user; // The User object that owns this account

    // Timestamp of account creation, set automatically
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();


    // Timestamp of last update, automatically updated via @PreUpdate
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    // Currency of the account (USD, EUR, etc.), required for multi-currency support
    @Column(length = 3, nullable = false)
    private String currency = "USD";

    // Optional IBAN for international transfers; unique to prevent duplicates
    @Column(length = 34, unique = true)
    private String iban;

    // Flag to indicate if overdraft is allowed for this account (common in checking accounts)
    @Column(nullable = false)
    private boolean overdraftEnabled = false;

    // Maximum overdraft limit if overdraft is enabled
    @Column(nullable = false)
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    // Lifecycle hook to update `updatedAt` automatically on entity update
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters & Setters omitted for brevity; can use Lombok to reduce boilerplate
}