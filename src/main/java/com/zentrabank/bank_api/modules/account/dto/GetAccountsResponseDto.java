package com.zentrabank.bank_api.modules.account.dto;
import java.util.List;

public record GetAccountsResponseDto(
       List<AccountDto> accounts
) { }