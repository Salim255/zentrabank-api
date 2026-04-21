package com.zentrabank.bank_api.modules.user.repository;

import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.entity.UserRole;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * UserRepository
 *
 * This interface is the DATA ACCESS LAYER for the User entity.
 * It uses Spring Data JPA, which automatically generates the implementation
 * at runtime using Hibernate under the hood.
 *
 * Key concepts:
 * -------------
 * 1. JpaRepository<User, UUID>
 *    - User  → the entity type managed by this repository
 *    - UUID  → the type of the primary key (@Id field)
 *
 * 2. Spring Data JPA automatically implements:
 *    - save()
 *    - findById()
 *    - findAll()
 *    - delete()
 *    - count()
 *
 * 3. Custom query methods:
 *    Spring Data JPA generates queries based on method names.
 *    Example: existsByLoginId(String loginId)
 *    → SELECT COUNT(*) FROM users WHERE login_id = ?
 *
 * 4. Hibernate is the JPA provider:
 *    - It translates Java entities into SQL queries
 *    - It manages the persistence context (1st-level cache)
 *    - It tracks entity state (transient, persistent, detached)
 *
 * This repository contains ONLY data-access logic.
 * No business logic should ever be placed here.
 */

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByRole(UserRole role);

    @Query(value = "SELECT u.refresh_token_hash FROM users AS u  WHERE u.id = :userId;", nativeQuery = true)
    String getRefreshToken(UUID userId);

    @Modifying // required for update/delete queries.
    @Transactional // ensures the update is committed.
    @Query(
            value = "UPDATE users SET refresh_token_hash = :refreshToken WHERE id = :userId;",
            nativeQuery = true
    )
    int updateRefreshToken(UUID userId, String refreshToken );

    /**
     * Checks if a loginId already exists in the database.
     * Used when generating a unique 9-digit banking login ID.
     *
     * Spring Data JPA automatically generates:
     * SELECT COUNT(*) > 0 FROM users WHERE login_id = ?
     */
    boolean existsByUserName(String loginId);



    /**
     * Check if an email is already used.
     * Useful for registration validation.
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by email.
     * Useful for password reset flows, notifications, etc.
     */
    Optional<User> findByUserName(String userName);

    /**NullMarked:
    Makes parameters and return values non‑null by default
    Is only for static analysis
    Does not affect Spring, JPA, Hibernate, or runtime behavior
    Removes the warning about overriding a null‑annotated method
     */
    @NullMarked
    Optional<User> findById(UUID id);
}