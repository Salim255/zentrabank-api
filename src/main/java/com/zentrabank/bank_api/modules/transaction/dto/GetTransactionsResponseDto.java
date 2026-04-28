package com.zentrabank.bank_api.modules.transaction.dto;

import java.util.List;

public record GetTransactionsResponseDto(
        List<TransactionDto> transactions
) {
}