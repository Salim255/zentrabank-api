package com.zentrabank.bank_api.modules.transfer.service;

import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferDto;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.GetTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.TransferDto;
import com.zentrabank.bank_api.modules.transfer.entity.Transfer;
import com.zentrabank.bank_api.modules.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public  class TransferServiceImp implements TransferService {
    private final TransferRepository transferRepository;
    private final Logger logger = LoggerFactory.getLogger(TransferServiceImp.class);

    @Override
    public String listTransfers(UUID userId) {

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
    public CreateTransferResponseDto createTransfer(CreateTransferDto dto, UUID userId){
        try {

        } catch (Exception e) {
            this.logger.error("Error in create transfer", e);
            throw e;
        }
    }

    public TransferDto buildTransferDto(Transfer data){
        return new TransferDto(
                data.getId(),
                data.getFromAccount(),
                data.getToAccount(),
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
}