package com.zentrabank.bank_api.modules.profile.repository;

import com.zentrabank.bank_api.modules.profile.entity.Profile;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    @NullMarked
    Optional<Profile> findById(UUID profileId);
}

