package com.zentrabank.bank_api.modules.user.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.account.dto.GetAccountsResponseDto;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.ResetPasswordDto;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import com.zentrabank.bank_api.modules.profile.dto.GetProfileResponseDto;
import com.zentrabank.bank_api.modules.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;
    private final AuthService authService;
    private final ProfileService profileService;

    public UserController(
            AccountService accountService,
            ProfileService profileService,
            AuthService authService
            ){
        this.authService = authService;
        this.profileService = profileService;
         this.accountService = accountService;
    }

    userService;

    @GetMapping("/me")
    @Operation(
            summary = "Get authenticated user information",
            description = "Returns the authenticated user's basic account information. Used for session restore on page refresh.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User information retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MeResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – user is not authenticated"
                    )
            }
    )
    public ApiResponseDto<MeResponseDto> getMe(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        MeResponseDto response = userService.getMe(userId);

    }
        @GetMapping("/accounts")
    @Operation(
            summary = "Get authenticated user's accounts",
            description = "Retrieves all bank accounts associated with the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Accounts retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    public ApiResponseDto<GetAccountsResponseDto> getUserAccounts(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();

        GetAccountsResponseDto response = accountService.getAccountsByUserId(userId);
        return ApiResponseDto.success(response);
    }


    @GetMapping("/profile")
    @Operation(
            summary = "Get authenticated user's profile",
            description = "Retrieves the profile associated with the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ApiResponseDto<GetProfileResponseDto> getUserProfile(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(profileService.getProfileByUserId(userId));
    }


    @GetMapping("/{userId}")
    public String getUser(@PathVariable String userId){
        return  "Hello form get user" + userId;
    }

    @PatchMapping("/reset-password")
    public ApiResponseDto<String> resetPassword(
            @Valid @RequestBody ResetPasswordDto body,
            HttpServletResponse response,
            Authentication auth
    ){

        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
        }

        UUID userId = (UUID) auth.getPrincipal();
        return  this.authService.resetPassword(body, userId);
    }

}