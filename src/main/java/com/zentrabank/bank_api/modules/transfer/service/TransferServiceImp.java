package com.zentrabank.bank_api.modules.transfer.service;

import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferDto;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferResponseDto;
import com.zentrabank.bank_api.modules.transfer.dto.GetTransferResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public  class TransferServiceImp implements TransferService {
    private final Logger logger = LoggerFactory.getLogger(TransferServiceImp.class);

    @Override
    public String listTransfers(UUID userId) {

    }

    @Override
    public GetTransferResponseDto getTransfer(UUID id, UUID userId) {
        try {

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
}