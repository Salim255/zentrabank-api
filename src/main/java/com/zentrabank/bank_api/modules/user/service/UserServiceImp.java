package com.zentrabank.bank_api.modules.user.service;
import com.zentrabank.bank_api.modules.auth.dto.LoggedUserDto;
import com.zentrabank.bank_api.modules.user.dto.MeResponseDto;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;

    @Override
    public MeResponseDto getMe(UUID userId){
        try {

            User user = this.userRepository.findById(userId).orElse(null);
            LoggedUserDto dto = (user != null) ? new LoggedUserDto(
                    user.getId(),
                    user.getEmail(),
                    user.isFirstLogin(),
                    user.getRole()
            ) : null;

            return  new MeResponseDto((dto));
        } catch (Exception e) {
            this.logger.error("Error in getting me", e);
            throw e;
        }
    }
    @Override
    public Optional<User> getUser(UUID userId) {
        try {
            return this.userRepository.findById(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  String updateUser(){
        return "Hello from update user";
    }

    @Override
    public  String deleteUser(){
        return "hello from delete user";
    }
}