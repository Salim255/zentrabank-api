package com.zentrabank.bank_api.modules.transfer.service;

import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferDto;
import com.zentrabank.bank_api.modules.transfer.dto.CreateTransferResponseDto;

import java.util.UUID;

public  interface TransferService {
    public String getTransfer(UUID id, UUID userId);
    public CreateTransferResponseDto createTransfer(CreateTransferDto dto, UUID userId);
}