package com.zentrabank.bank_api.modules.auth.dto;

public record ResetPasswordDto (
        String oldPassword,
        String newPassword
){

}