package com.zentrabank.bank_api.modules.transfer.dto;

import com.zentrabank.bank_api.modules.transfer.dto.TransferDto;

import java.util.List;

public record GetTransfersResponseDto(List<TransferDto> tranfers) {
}