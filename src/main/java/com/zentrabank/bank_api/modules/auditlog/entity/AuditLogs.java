package com.zentrabank.bank_api.modules.auditlog.entity;

import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * AuditLog
 *
 * A banking‑grade audit trail entry.
 * Every action performed in the system MUST be traceable:
 * - who did it
 * - what they did
 * - when they did it
 * - from where
 * - what changed (before/after)
 * - correlation ID for distributed tracing
 *
 * This entity is immutable after creation.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Table(name = "audit_logs", indexes = {
                // Fast lookup by user (who did what)
                @Index(name = "idx_audit_user", columnList = "user_id"),

                // Fast lookup by action type
                @Index(name = "idx_audit_action", columnList = "action")
})
public class AuditLogs {

        // -------------------------------------------------------------------------
        // PRIMARY KEY
        // -------------------------------------------------------------------------

        /**
         * Unique identifier for the audit entry.
         * UUID ensures global uniqueness across distributed systems.
         *
         * Without this:
         * - logs cannot be uniquely referenced
         * - merging logs from multiple services becomes impossible
         */
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        // -------------------------------------------------------------------------
        // ACTOR (WHO DID THE ACTION)
        // -------------------------------------------------------------------------

        /**
         * The user who performed the action.
         * LAZY fetch avoids loading the entire User object unless needed.
         *
         * Without this:
         * - you cannot know who triggered the event
         * - no accountability
         * - no fraud investigation possible
         */
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        // -------------------------------------------------------------------------
        // ACTION TYPE
        // -------------------------------------------------------------------------

        /**
         * Strongly typed action (LOGIN, TRANSFER, UPDATE_PROFILE, etc.)
         * Enum ensures consistency and prevents typos.
         *
         * Without this:
         * - logs become inconsistent ("login", "Login", "LOG_IN")
         * - impossible to filter or analyze reliably
         */
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ActionType action;

        // -------------------------------------------------------------------------
        // CORRELATION ID (TRACE REQUEST ACROSS SYSTEMS)
        // -------------------------------------------------------------------------

        /**
         * Correlation ID links all logs belonging to the same request.
         * Critical for microservices and debugging.
         *
         * Without this:
         * - impossible to trace a request across services
         * - debugging distributed flows becomes a nightmare
         */
        @Column(name = "correlation_id", nullable = false, length = 100)
        private String correlationId;

        // -------------------------------------------------------------------------
        // REQUEST CONTEXT (WHERE DID IT HAPPEN)
        // -------------------------------------------------------------------------

        /**
         * Name of the service that generated the log.
         * Example: "auth-service", "transaction-service".
         *
         * Without this:
         * - logs from different services mix together
         */
        private String serviceName;

        /**
         * API endpoint that was called.
         * Example: "/api/v1/transfer".
         *
         * Without this:
         * - you cannot know what part of the system was touched
         */
        private String endpoint;

        /**
         * HTTP method used (GET, POST, PUT, DELETE).
         *
         * Without this:
         * - incomplete request context
         */
        private String httpMethod;

        // -------------------------------------------------------------------------
        // SECURITY CONTEXT
        // -------------------------------------------------------------------------

        /**
         * IP address of the requester.
         * 45 chars supports IPv6.
         *
         * Without this:
         * - no fraud detection
         * - no security investigation
         */
        @Column(length = 45)
        private String ipAddress;

        /**
         * User agent string (browser, device, OS).
         *
         * Without this:
         * - no device fingerprinting
         * - no anomaly detection
         */
        @Column(length = 255)
        private String userAgent;

        // -------------------------------------------------------------------------
        // BEFORE / AFTER STATE (CRITICAL FOR BANKING)
        // -------------------------------------------------------------------------

        /**
         * JSON snapshot of the entity BEFORE the action.
         * Stored as JSONB for efficient querying.
         *
         * Without this:
         * - impossible to know what changed
         * - no rollback capability
         * - no audit trail for regulators
         */
        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private String beforeState;

        /**
         * JSON snapshot of the entity AFTER the action.
         *
         * Without this:
         * - no visibility into the result of the action
         * - cannot detect unauthorized modifications
         */
        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private String afterState;

        // -------------------------------------------------------------------------
        // EXTRA METADATA
        // -------------------------------------------------------------------------

        /**
         * Additional structured metadata (JSON).
         * Example:
         * - {"amount": 200, "currency": "EUR"}
         * - {"failedReason": "Insufficient funds"}
         *
         * Without this:
         * - logs become rigid and cannot evolve
         */
        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private String metadata;

        // -------------------------------------------------------------------------
        // TIMESTAMP
        // -------------------------------------------------------------------------

        /**
         * Immutable creation timestamp.
         * Automatically set on insert.
         *
         * Without this:
         * - no timeline of events
         * - impossible to reconstruct sequences
         */
        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @PrePersist
        public void onCreate() {
                this.createdAt = Instant.now();
        }
}