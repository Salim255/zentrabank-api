package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.validation.AuthValidator;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class AuthServiceImp implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImp(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AuthValidator registerValidator
    ){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authValidator = registerValidator;
        this.jwtService = jwtService;
    }

    @Override
    public String getStoredRefreshToken(String userId){
        try {
          return this.userRepository.getRefreshToken(userId);
        } catch (){

        }
    }
    @Override
    public ApiResponseDto<String> resetPassword(ResetPasswordDto payload, UUID userId){
        try {
            // 1 Get USer
            User user = this.userRepository.findById(userId).orElseThrow(
                    () -> new UnauthorizedException("Invalid authentication state")
            );

            // 2 Get user password
            String oldHashedPassword = user.getPasswordHash();

            // 3 Validate user inout
            this.authValidator.comparePassword(payload.oldPassword(), oldHashedPassword);

            // 3 Hash passwords

            // 4 Compare user's password with db password

            return  ApiResponseDto.success("Password updated with success");
        } catch (DataIntegrityViolationException ex){
            logger.error("Database constraint violation during user login", ex);
            throw  ex;
        }
    }


    @Override
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload){
        try{
            // 1 Validate user input;
            this.authValidator.registerValidate(payload);
            // 2 Generated temp password
            String tmpPassword = this.generateTempPassword();

            // 3 Hash password
            String hashed = this.passwordEncoder.encode(tmpPassword);

            // 4 Generate user loginId
            String userLoginId = this.generateUniqueLoginId();

            // 5 Create User class
            User createdUser = new User();
            createdUser.setEmail(payload.email());
            createdUser.setFirstName(payload.firstName());
            createdUser.setLastName(payload.lastName());
            createdUser.setPasswordHash(hashed);
            createdUser.setLoginId(userLoginId);


            // 5 Save the
            User user = this.userRepository.save(createdUser);

            // 6 Build RegisterDto
            RegisterResponseDto response = new RegisterResponseDto(
                   new UserDto( user.getId(),
                       user.getEmail(),
                       user.getFirstName(),
                       user.getLastName(),
                       user.getRole(),
                       user.getLoginId(),
                       tmpPassword,
                       user.getCreatedAt(),
                       user.getUpdatedAt()
                   )
            );

            // 7 Return data
            return  ApiResponseDto.success(response);
        } catch (DataIntegrityViolationException ex){
            logger.error("Database constraint violation during user registration", ex);
            // Unknown constraint → let GlobalExceptionHandler handle it
            throw ex;
        }
    }

    public  ApiResponseDto<LoginResponseDto> login(LoginDto payload)
    {
        try {
            // 1 Validate input
            User user = this.authValidator.loginValidate(payload);
            String userId = user.getId().toString();

            // 2 Create tokens
            String accessToken = this.jwtService.generateAccessToken(userId, user.getRole());
            String refreshToken = this.jwtService.generateRefreshToken(userId, user.getRole());

            // Store refresh token in DB
            int updated = this.userRepository.updateRefreshToken(user.getId(), refreshToken);

            if (updated == 0) throw new NotFoundException("User not found");

            // Return user  + tokens
            // Built LoginResponseDto
            LoginResponseDto  result = new LoginResponseDto(
                    new LoggedUserDto(
                            user.getId().toString(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.isFirstLogin(),
                            user.getRole()
                    ),
                    new TokenDto(accessToken, refreshToken)
            );
            return  ApiResponseDto.success(result);
        } catch (DataIntegrityViolationException ex){
            logger.error("Database constraint violation during user login", ex);
            // Unknown constraint → let GlobalExceptionHandler handle it
            throw ex;
        }
    }

    private String generateUniqueLoginId() {
        String id;
        do {
            id = generate9DigitNumber();
        } while (userRepository.existsByLoginId(id));
        return id;
    }

    private String generate9DigitNumber() {
        int min = 100_000_000;
        int max = 999_999_999;
        int number = min + new Random().nextInt(max - min);
        return String.valueOf(number);
    }

    private String generateTempPassword() {
        int min = 100_000; // smallest 6-digit number
        int max = 999_999; // largest 6-digit number
        int number = min + new Random().nextInt(max - min);
        return String.valueOf(number);
    }
}