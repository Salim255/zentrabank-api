import com.zentrabank.bank_api.modules.account.entity.Account.Account;
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

    // Many-to-One relation: each transaction belongs to one bank account
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Type of transaction: DEPOSIT, WITHDRAWAL, TRANSFER
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Amount involved in the transaction
    @Column(nullable = false)
    private BigDecimal amount;

    // Currency of the transaction (for multi-currency support)
    @Column(length = 3, nullable = false)
    private String currency = "USD";

    // Timestamp when the transaction occurred
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // Optional reference for transfers (from/to account numbers)
    @Column(length = 20)
    private String referenceAccountNumber;

    // Optional description or memo for the transaction
    @Column(length = 255)
    private String description;

    // Balance after the transaction (optional but useful for audit/logging)
    @Column(nullable = false)
    private BigDecimal postTransactionBalance;

    // Pre-update hook to automatically update timestamps (if needed)
    @PreUpdate
    public void preUpdate() {
        // could add updatedAt if you track updates for transactions
    }
}