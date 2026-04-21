package com.zentrabank.bank_api.modules.application.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.application.dto.ApplicationResponseDto;
import com.zentrabank.bank_api.modules.application.dto.CreateApplicationDto;
import com.zentrabank.bank_api.modules.application.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Operation(
            summary = "Create a new application",
            description = """
            Creates a new bank account application based on the provided customer information,
            account preferences, and regulatory declarations.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Application successfully created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request payload.",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected server error.",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PostMapping
    public ApiResponseDto<ApplicationResponseDto> createApplication(
            @Parameter(
                    description = "Application details including personal information, account type, " +
                            "source of wealth and electronic signature.",
                    required = true
            )
            @Valid @RequestBody CreateApplicationDto body,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();

        ApplicationResponseDto response = applicationService.createApplication(body, userId);
        return  ApiResponseDto.success(response);
    }
}