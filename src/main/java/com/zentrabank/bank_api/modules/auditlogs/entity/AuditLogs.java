package com.zentrabank.bank_api.modules.auditlogs.entity;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "audit_logs",
        indexes = {
                // Fast lookup by user (who did what)
                @Index(name = "idx_audit_user", columnList = "user_id"),

                // Fast lookup by action type
                @Index(name = "idx_audit_action", columnList = "action")
        }
)
public class AuditLogs {
    @Id // JPA: marks this field as the primary key
    // JPA: tells Hibernate to auto-generate the ID using a generator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            columnDefinition = "UUID", // Ensures PostgreSQL stores it as a real UUID type
            updatable = false, // ID cannot be changed after creation
            nullable = false  // ID must always be present
    )
    private UUID id;

    // User who performed the action (can be null for system actions)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Action performed (LOGIN, TRANSFER, LOGOUT, etc.)
    @Column(nullable = false)
    private String action;

    // Additional details (JSON or text)
    // Example: {"amount":100,"to":"ACC123"}
    @Column(columnDefinition = "TEXT")
    private String metadata;

    // IP address of the request (important for security)
    @Column(length = 45)
    private String ipAddress;

    // User agent (browser/device info)
    @Column(length = 255)
    private String userAgent;

    // Timestamp of the action
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}