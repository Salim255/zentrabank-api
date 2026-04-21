package com.zentrabank.bank_api.modules.application.dto;

import com.zentrabank.bank_api.modules.account.dto.AccountDto;
import com.zentrabank.bank_api.modules.profile.dto.ProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ApplicationResponseDto",
        description = "Response returned after successfully creating a new bank account application. " +
                "Contains the created account and associated customer profile."
)
public record ApplicationResponseDto(
        @Schema(
                description = "Details of the newly created bank account.",
                implementation = AccountDto.class
        )
        AccountDto account,

        @Schema(
                description = "Customer profile associated with the application.",
                implementation = ProfileDto.class
        )
        ProfileDto profile
) {
}