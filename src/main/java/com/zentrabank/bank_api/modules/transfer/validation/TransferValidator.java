package com.zentrabank.bank_api.modules.transfer.validation;

import com.zentrabank.bank_api.exceptions.BadRequestException;
import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferValidator {

    private final AccountService accountService;

    // ---------------------------------------------------------
    // MAIN VALIDATION ENTRYPOINT
    // ---------------------------------------------------------
    public void validate(CreateTransferDto dto, Account sender, Account receiver) {

        validateAmount(dto.amount());
        validateCurrency(dto.currency());
        validateSenderOwnership(sender, dto.fromAccountId());
        validateInternalVsExternal(dto, receiver);
        validateNotSelfTransfer(sender, receiver);
        validateBalance(sender, dto.amount());
        validateCurrencyMatch(sender, receiver, dto);
    }

    // ---------------------------------------------------------
    // VALIDATION RULES
    // ---------------------------------------------------------

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new BadRequestException("Currency is required");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new BadRequestException("Currency must be a 3-letter ISO code");
        }
    }

    private void validateSenderOwnership(Account sender, java.util.UUID fromAccountId) {
        if (sender == null) {
            throw new NotFoundException("Sender account not found");
        }
        if (!sender.getId().equals(fromAccountId)) {
            throw new BadRequestException("Sender account mismatch");
        }
    }

    private void validateInternalVsExternal(CreateTransferDto dto, Account receiver) {
        boolean isInternal = dto.toAccountId() != null;

        if (isInternal) {
            if (receiver == null) {
                throw new NotFoundException("Receiver account not found");
            }
        } else {
            // External transfer → IBAN/BIC/Name required
            if (dto.externalIban() == null || dto.externalIban().isBlank()) {
                throw new BadRequestException("External IBAN is required for external transfers");
            }
            if (dto.externalBic() == null || dto.externalBic().isBlank()) {
                throw new BadRequestException("External BIC is required for external transfers");
            }
            if (dto.externalRecipientName() == null || dto.externalRecipientName().isBlank()) {
                throw new BadRequestException("External recipient name is required for external transfers");
            }
        }
    }

    private void validateNotSelfTransfer(Account sender, Account receiver) {
        if (receiver != null && sender.getId().equals(receiver.getId())) {
            throw new BadRequestException("Cannot transfer to the same account");
        }
    }

    private void validateBalance(Account sender, BigDecimal amount) {
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance");
        }
    }

    private void validateCurrencyMatch(Account sender, Account receiver, CreateTransferDto dto) {
        // External transfer → skip currency match (FX possible later)
        if (receiver == null) return;

        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            throw new BadRequestException("Currency mismatch between accounts");
        }

        if (!sender.getCurrency().equals(dto.currency())) {
            throw new BadRequestException("Transfer currency does not match account currency");
        }
    }
}
