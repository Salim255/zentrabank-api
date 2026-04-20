package com.zentrabank.bank_api.modules.profile.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;

import java.util.UUID;

public interface ProfileService {
    public ApiResponseDto<ProfileDto> getProfile(UUID profileId);
    public ApiResponseDto<ProfileDto>  createProfile(CreateProfileDto payload);
}