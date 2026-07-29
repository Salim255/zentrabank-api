package com.zentrabank.bank_api.modules.transaction.dto;

import java.math.BigDecimal;

public record TransactionsSummaryDto(

    long totalTransactions,

    BigDecimal moneyIn,

    BigDecimal moneyOut,

    BigDecimal currentBalance

) {}