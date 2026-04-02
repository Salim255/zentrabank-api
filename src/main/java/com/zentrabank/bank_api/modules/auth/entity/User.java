package com.zentrabank.bank_api.modules.auth.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

/**
 * Core user entity for the banking application.
 * Represents an authenticated customer with secure credentials
 * and basic profile information.
 */
// Lombok is a Java library that automatically
// generates common, repetitive code for you at compile: get, set ...
@Getter  // Lombok: generates getters for all fields
@Setter // Lombok: generates setters for all fields
@NoArgsConstructor // Lombok: generates a no-args constructor (required by JPA)
@AllArgsConstructor // Lombok: generates a constructor with all fields
@Builder // Lombok: enables the builder pattern for clean object creation
@Entity  // JPA: marks this class as a database entity
@Table(name = "users") // JPA: maps this entity to the "users" table
public class User {

    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // BCrypt hashed password

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    @Column(nullable = false, unique = true, length = 9)
    private String loginId;

    @Column(nullable = false)
    private boolean firstLogin = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

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
