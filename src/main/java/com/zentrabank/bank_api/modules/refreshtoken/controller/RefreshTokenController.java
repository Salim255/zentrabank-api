package com.zentrabank.bank_api.modules.refreshtoken.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh-tokens")
public class RefreshTokenController {

    @PatchMapping("/logout")
    public ApiResponseDto<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        return ApiResponseDto.success("Logout with success");
    }
}