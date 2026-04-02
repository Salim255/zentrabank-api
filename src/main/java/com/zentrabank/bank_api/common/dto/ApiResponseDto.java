package com.zentrabank.bank_api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A generic API response wrapper used to standardize all responses.
 *
 * @param status A string describing the result ("success", "error", etc.)
 * @param data   The actual payload returned by the API (generic type T)
 */

public record ApiResponseDto<T>(
        // Shown in Swagger UI as an example value
        @Schema(example = "success")
        String status,

        // Generic payload returned by the API
        T data
){
    /**
     * Factory method for successful responses.
     *
     * @param data The payload to return
     * @return A standardized success response
     */
    public static <T> ApiResponseDto<T> success(T data){
        return new ApiResponseDto<>("success",data);
    }

}