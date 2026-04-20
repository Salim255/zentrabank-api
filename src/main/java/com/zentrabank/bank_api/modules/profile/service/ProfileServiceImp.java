package com.zentrabank.bank_api.modules.profile.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import com.zentrabank.bank_api.modules.profile.entity.Profile;
import com.zentrabank.bank_api.modules.profile.repository.ProfileRepository;
import com.zentrabank.bank_api.modules.profile.validation.ProfileValidator;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RequiredArgsConstructor
public class ProfileServiceImp implements ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileValidator profileValidator;

    private final Logger logger = LoggerFactory.getLogger(ProfileServiceImp.class);
    public ApiResponseDto<ProfileDto> createProfile(CreateProfileDto payload, UUID userId){
        try {
            // 1. Validate input
            this.profileValidator
                    .validateCreateProfile(payload, userId);

            // 2. Fetch user
            User user = this.userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3. Map DTO → Entity
            Profile profile = this.profileMapper.toEntity(payload);
            profile.setUser(user);

            // 4. Save profile
            Profile saved = this.profileRepository.save(profile);

            // 5. Map Entity → DTO
            ProfileDto response = this.profileMapper.toDto(saved);

            // 6. Wrap in ApiResponseDto
            return ApiResponseDto.success(response);
        } catch (Exception error){
            this.logger.error("Error to create profile", error);
            throw  error;
        }
    }

    public ApiResponseDto<ProfileDto>  getProfile(UUID profileId){
            try {

            } catch (Exception e) {
                this.logger.error(("Error in get profile"));
                throw e;
            }
    }
}