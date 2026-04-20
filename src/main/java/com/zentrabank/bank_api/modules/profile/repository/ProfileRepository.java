package com.zentrabank.bank_api.modules.profile.repository;

import com.zentrabank.bank_api.modules.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
}

