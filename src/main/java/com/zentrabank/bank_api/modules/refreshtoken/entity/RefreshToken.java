package com.zentrabank.bank_api.modules.refreshtoken.entity;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    // Link to user (many tokens per user)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The actual refresh token string
    @Column(nullable = false, unique = true)
    private String token;

    // Expiration date of the token
    @Column(nullable = false)
    private Instant expiresAt;

    // Track if token is revoked (logout, security)
    @Column(nullable = false)
    private boolean revoked = false;

    // When token was created
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

    @PreUpdate
    // JPA: runs BEFORE the entity is updated in the database
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}