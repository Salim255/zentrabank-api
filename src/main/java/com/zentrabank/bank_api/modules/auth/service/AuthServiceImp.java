package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.config.BankApiConfigProperties;
import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.validation.AuthValidator;
import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.servce.RefreshTokenServiceImp;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.entity.UserRole;
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
    private final BankApiConfigProperties config;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenServiceImp refreshTokenServiceImp;

    public AuthServiceImp(
            BankApiConfigProperties config,
            RefreshTokenServiceImp refreshTokenServiceImp,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AuthValidator registerValidator
    ){
        this.config = config;
        this.refreshTokenServiceImp = refreshTokenServiceImp;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authValidator = registerValidator;
        this.jwtService = jwtService;
    }

    @Override
    public void createSuperAdmin(){
        try {
            boolean superAdminExists = userRepository.existsByRole(UserRole.SUPER_ADMIN);
            if (!superAdminExists){
                String email = this.config.superAdminEmail();
                String password = this.config.superAdminPassword();
                String firstName = this.config.superAdminFirstName();

                User superAdmin = User.builder()
                        .email(email)
                        .loginId(firstName)
                        .passwordHash(passwordEncoder.encode(password))
                        .role(UserRole.SUPER_ADMIN)
                        .build();

                this.userRepository.save(superAdmin);
                System.out.println("🔥 SUPER_ADMIN created");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RefreshAccessToken  refreshAccessToken(String refreshToken){
        try {

            // 1 Check token
            if (refreshToken == null) throw new UnauthorizedException("No refresh token");

            // 2 Parse token data
            UserTokenDetailsDto tokenData = this.jwtService.parseToken(refreshToken);

            // 3 Get user Id
            UUID userId = tokenData.userId();
            UserRole role = tokenData.role();

            // 4 Check token matches DB
            RefreshTokenDto storedToken = this.refreshTokenServiceImp.findTokenByToken(refreshToken);

            if (!refreshToken.equals(storedToken.token()) || storedToken.token().isBlank()) {
                throw new UnauthorizedException("Invalid refresh token");
            }

            // 5 Create access token
            String accessToken = this.jwtService.generateAccessToken(userId.toString(), role);

            // 6 return access token
            return  new RefreshAccessToken(new TokenDto(accessToken, refreshToken));
        } catch (Exception ex){
            this.logger.error("Error to refresh access token { }", ex);
            throw ex;
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
            createdUser.setPasswordHash(hashed);
            createdUser.setLoginId(userLoginId);


            // 5 Save the
            User user = this.userRepository.save(createdUser);

            // 6 Build RegisterDto
            RegisterResponseDto response = new RegisterResponseDto(
                   new UserDto(user.getId(),
                       user.getEmail(),
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

            // Create refresh token in DB
            CreateTokenDto tokenPayload = new CreateTokenDto(
                    user.getId(),
                    refreshToken,
                    this.jwtService.fromNow(config.refreshTokenExpiration()).toInstant()
            );

            this.refreshTokenServiceImp.createRefreshToken(tokenPayload);

            // Return user  + tokens
            // Built LoginResponseDto
            LoginResponseDto  result = new LoginResponseDto(
                    new LoggedUserDto(
                            user.getId().toString(),
                            user.getEmail(),
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