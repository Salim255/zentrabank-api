package com.zentrabank.bank_api.modules.transfer.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    // ---------------------------------------------------------
    // CREATE TRANSFER
    // ---------------------------------------------------------
    @PostMapping
    @Operation(
            summary = "Create a new transfer",
            description = """
                    Creates a money transfer from the authenticated user's account 
                    to another internal account or an external IBAN/BIC.
                    This endpoint triggers the transfer lifecycle:
                    - Create transfer (PENDING)
                    - Validate balance & rules
                    - Create debit & credit transactions
                    - Mark transfer COMPLETED
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transfer created and processed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid transfer data or insufficient balance",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ApiResponseDto<CreateTransferResponseDto> createTransfer(
            @Valid @RequestBody CreateTransferDto dto,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(transferService.createTransfer(dto, userId));
    }

    // ---------------------------------------------------------
    // GET TRANSFER BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(
            summary = "Get transfer by ID",
            description = "Fetches a transfer using its unique identifier, including status and metadata.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transfer retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Transfer not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ApiResponseDto<GetTransferResponseDto> getTransfer(
            @PathVariable UUID id,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(transferService.getTransfer(id, userId));
    }

    // ---------------------------------------------------------
    // LIST USER TRANSFERS
    // ---------------------------------------------------------
    @GetMapping
    @Operation(
            summary = "List all transfers for the authenticated user",
            description = "Returns all transfers initiated by the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transfers retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    public ApiResponseDto<?> listTransfers(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(transferService.listTransfers(userId));
    }
}