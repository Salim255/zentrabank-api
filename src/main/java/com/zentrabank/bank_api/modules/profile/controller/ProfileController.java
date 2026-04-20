package com.zentrabank.bank_api.modules.profile.controller;

import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import com.zentrabank.bank_api.modules.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
                                    schema = @Schema(implementation = ProfileDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<ProfileDto> createProfile(
            @Valid @RequestBody CreateProfileDto dto
    ) {
        ProfileDto created = profileService.createProfile(dto);
        return ResponseEntity.status(201).body(created);
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
                                    schema = @Schema(implementation = ProfileDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<ProfileDto> getProfile(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    // ---------------------------------------------------------
    // UPDATE PROFILE
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing profile",
            description = "Updates customer profile information.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProfileDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProfileDto dto
    ) {
        return ResponseEntity.ok(profileService.updateProfile(id, dto));
    }
}