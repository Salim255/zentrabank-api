package com.zentrabank.bank_api.modules.user.dto;

import com.zentrabank.bank_api.modules.auth.dto.LoggedUserDto;

public record MeResponseDto(LoggedUserDto user) { }