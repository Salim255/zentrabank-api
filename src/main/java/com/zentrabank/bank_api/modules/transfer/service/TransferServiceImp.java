package com.zentrabank.bank_api.modules.transfer.service;

import com.zentrabank.bank_api.exceptions.BadRequestException;
import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.repository.AccountRepository;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import com.zentrabank.bank_api.modules.transaction.repository.TransactionRepository;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferDto;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.GetTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.TransferDto;
import com.zentrabank.bank_api.modules.transfer.entity.Transfer;
import com.zentrabank.bank_api.modules.transfer.entity.TransferStatus;
import com.zentrabank.bank_api.modules.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public  class TransferServiceImp implements TransferService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final Logger logger = LoggerFactory.getLogger(TransferServiceImp.class);

    @Override
    public String listTransfers(UUID userId) {
        return  "";
    }

    @Override
    public GetTransferResponseDto getTransfer(UUID id, UUID userId) {
        try {
            Transfer trans = this.transferRepository.findById(id).orElse(null);
            // TransferDto builder
            if(trans == null) return new GetTransferResponseDto(null);
            return new GetTransferResponseDto(buildTransferDto(trans)) ;
        } catch (Exception e) {
            this.logger.error("Error in getting transfers", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CreateTransferResponseDto createTransfer(CreateTransferDto dto, UUID userId){
        try {
            // ---------------------------------------------------------
            // 1. Load sender account & validate ownership
            // ---------------------------------------------------------
            Account sender = accountRepository.findAccountByAccountNumber(dto.fromAccountNumber())
                    .orElseThrow(() ->  new NotFoundException("Sender account not found"));

            if (!sender.getUser().getId().equals(userId)) {
                throw  new NotFoundException("You do not own this account");
            }

            // ---------------------------------------------------------
            // 2. Determine internal vs external transfer
            // ---------------------------------------------------------
            Account receiver = accountRepository.findAccountByAccountNumber(dto.toAccountNumber())
                        .orElseThrow(() -> new NotFoundException("Receiver account not found"));


            // ---------------------------------------------------------
            // 3. Validate balance
            // ---------------------------------------------------------
            if (sender.getBalance().compareTo(dto.amount()) < 0) {
                throw new BadRequestException("Insufficient balance");
            }

            // ---------------------------------------------------------
            // 4. Create Transfer (PENDING)
            // ---------------------------------------------------------
            Transfer transfer = Transfer.builder()
                    .fromAccount(sender)
                    .toAccount(receiver)
                    .externalIban(dto.externalIban())
                    .externalBic(dto.externalBic())
                    .externalRecipientName(dto.externalRecipientName())
                    .amount(dto.amount())
                    .currency(dto.currency())
                    .status(TransferStatus.PENDING)
                    .referenceId(generateReferenceId())
                    .description(dto.description())
                    .build();

            transferRepository.save(transfer);

            // ---------------------------------------------------------
            // 5. Update balances
            // ---------------------------------------------------------
            sender.setBalance(sender.getBalance().subtract(dto.amount()));
            accountRepository.save(sender);


            receiver.setBalance(receiver.getBalance().add(dto.amount()));
            accountRepository.save(receiver);


            // ---------------------------------------------------------
            // 6. Create debit transaction
            // ---------------------------------------------------------
            transactionRepository.save(
                    Transaction.builder()
                            .account(sender)
                            .transfer(transfer)
                            .type(TransactionType.TRANSFER_DEBIT)
                            .amount(dto.amount().negate())
                            .postTransactionBalance(sender.getBalance())
                            .build()
            );

            // ---------------------------------------------------------
            // 7. Create credit transaction (internal only)
            // ---------------------------------------------------------
            transactionRepository.save(
                    Transaction.builder()
                            .account(receiver)
                            .transfer(transfer)
                            .type(TransactionType.TRANSFER_CREDIT)
                            .amount(dto.amount())
                            .postTransactionBalance(receiver.getBalance())
                            .build()
            );


            // ---------------------------------------------------------
            // 8. Mark transfer as COMPLETED
            // ---------------------------------------------------------
            transfer.setStatus(TransferStatus.COMPLETED);
            transfer.setCompletedAt(Instant.now());
            transferRepository.save(transfer);

            // ---------------------------------------------------------
            // 9. Return response
            // ---------------------------------------------------------
            return new CreateTransferResponseDto(buildTransferDto(transfer));
        } catch (Exception e) {
            this.logger.error("Error in create transfer", e);
            throw e;
        }
    }

    public TransferDto buildTransferDto(Transfer data){
        return new TransferDto(
                data.getId(),
                data.getFromAccount().getId(),
                data.getToAccount().getId(),
                data.getExternalIban(),
                data.getExternalBic(),
                data.getExternalRecipientName(),
                data.getAmount(),
                data.getCurrency(),
                data.getStatus(),
                data.getReferenceId(),
                data.getDescription(),
                data.getCreatedAt(),
                data.getCompletedAt()
        );
    }

    private String generateReferenceId() {
        String timestamp = java.time.format.DateTimeFormatter
                .ofPattern("yyyyMMdd-HHmmss")
                .withZone(java.time.ZoneId.of("UTC"))
                .format(java.time.Instant.now());

        String random = java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "TRF-" + timestamp + "-" + random;
    }
}