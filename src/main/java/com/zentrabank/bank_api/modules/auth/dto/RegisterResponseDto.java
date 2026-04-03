package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponseDto(UserDto user) { }