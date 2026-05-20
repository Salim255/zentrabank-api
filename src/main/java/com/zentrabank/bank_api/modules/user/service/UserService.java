package com.zentrabank.bank_api.modules.user.service;

import com.zentrabank.bank_api.modules.user.dto.MeResponseDto;
import com.zentrabank.bank_api.modules.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    public MeResponseDto getMe(UUID userId);
    public Optional<User> getUser(UUID userId);
    public String updateUser();
    public String deleteUser();
}