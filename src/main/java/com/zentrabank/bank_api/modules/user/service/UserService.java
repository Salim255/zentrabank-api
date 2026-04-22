package com.zentrabank.bank_api.modules.user.service;

import com.zentrabank.bank_api.modules.user.dto.MeResponseDto;

import java.util.UUID;

public interface UserService {
    public MeResponseDto getMe(UUID userId);
    public String getUser();
    public String updateUser();
    public String deleteUser();
}