package com.zentrabank.bank_api.modules.profile.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileResponseDto;

import com.zentrabank.bank_api.modules.profile.dto.GetProfileResponseDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import com.zentrabank.bank_api.modules.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    // ---------------------------------------------------------
    // CREATE PROFILE
    // ---------------------------------------------------------
    @PostMapping
    @Operation(
            summary = "Create a new profile",
            description = "Creates a customer profile linked to an existing user.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Profile created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ApiResponseDto<CreateProfileResponseDto> createProfile(
            @Valid @RequestBody CreateProfileDto dto,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(profileService.createProfile(dto, userId));
    }

    // ---------------------------------------------------------
    // GET PROFILE BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Get profile by ID",
            description = "Fetches a customer profile using its unique identifier.",
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
    public ApiResponseDto<GetProfileResponseDto> getProfile(
            @PathVariable UUID id
    ) {
        return profileService.getProfile(id);
    }
}