package com.zentrabank.bank_api.modules.application.service;

import com.zentrabank.bank_api.modules.application.dto.ApplicationResponseDto;
import com.zentrabank.bank_api.modules.application.dto.CreateApplicationDto;

import java.util.UUID;

public interface ApplicationService {
    public ApplicationResponseDto createApplication(CreateApplicationDto payload, UUID userId);
}