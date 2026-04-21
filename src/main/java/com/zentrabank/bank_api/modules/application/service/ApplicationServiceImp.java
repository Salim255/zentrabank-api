package com.zentrabank.bank_api.modules.application.service;

import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.application.dto.ApplicationResponseDto;
import com.zentrabank.bank_api.modules.application.dto.CreateApplicationDto;
import com.zentrabank.bank_api.modules.profile.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ApplicationServiceImp implements ApplicationService {
    private final AccountService accountService;
    private final ProfileService profileService;
    private final Logger logger = LoggerFactory.getLogger(ApplicationServiceImp.class);
    @Transactional
    @Override
    public ApplicationResponseDto createApplication(CreateApplicationDto payload){
       try {

       } catch (Exception e) {
            this.logger.error("Error to create application", e);
           throw e;
       }
    }
}