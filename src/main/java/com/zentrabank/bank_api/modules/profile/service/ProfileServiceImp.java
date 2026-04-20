package com.zentrabank.bank_api.modules.profile.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ProfileServiceImp implements ProfileService {
    private final Logger logger = LoggerFactory.getLogger(ProfileServiceImp.class);
    public ApiResponseDto<ProfileDto> createProfile(){
        try {

        } catch (Exception error){
            this.logger.error("Error to create profile", error);
            throw  error;
        }
    }

    public ApiResponseDto<ProfileDto>  getProfile(UUID profileId){
            return  "";
    }
}