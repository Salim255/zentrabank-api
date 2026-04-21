package com.zentrabank.bank_api.modules.application.dto;

import com.zentrabank.bank_api.modules.account.dto.AccountDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;

public record ApplicationResponseDto(AccountDto account, ProfileDto profile) {
}