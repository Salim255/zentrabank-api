package com.zentrabank.bank_api.modules.application.service;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.application.dto.ApplicationResponseDto;
import com.zentrabank.bank_api.modules.application.dto.CreateApplicationDto;
import com.zentrabank.bank_api.modules.application.validation.ApplicationValidator;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileResponseDto;
import com.zentrabank.bank_api.modules.profile.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImp implements ApplicationService {
    private final ApplicationValidator applicationValidator;
    private final AccountService accountService;
    private final ProfileService profileService;
    private final Logger logger = LoggerFactory.getLogger(ApplicationServiceImp.class);
    @Transactional
    @Override
    public ApplicationResponseDto createApplication(CreateApplicationDto payload, UUID userId){
       try {
           // Validate payload
           applicationValidator.validate(payload);
           // 1. Create profile
           CreateProfileResponseDto profileDto = profileService.createProfile(payload.toProfileDto(), userId);

           // 2. Create account linked to profile
           CreateAccountResponseDto accountDto = accountService.createAccount(payload.toAccountDto(), userId);

           // 3. Return combined response
           return new ApplicationResponseDto(accountDto.account(), profileDto.profile());
       } catch (Exception e) {
            this.logger.error("Error to create application", e);
           throw e;
       }
    }
}