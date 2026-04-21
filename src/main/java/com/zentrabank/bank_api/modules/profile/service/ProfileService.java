package com.zentrabank.bank_api.modules.profile.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.GetProfileResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;

import java.util.UUID;

public interface ProfileService {
    public ApiResponseDto<GetProfileResponseDto> getProfileByUserId(UUID userId);
    public ApiResponseDto<GetProfileResponseDto> getProfile(UUID profileId);
    public CreateProfileResponseDto createProfile(CreateProfileDto payload, UUID userId);
}