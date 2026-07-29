package com.zentrabank.bank_api.modules.transaction.dto;

import com.zentrabank.bank_api.modules.transaction.dto.TransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionsSummaryDto;

import java.util.List;

public record AccountTransactionsResponseDto(
        TransactionsSummaryDto summary,
        List<TransactionDto> transactions
) {
}