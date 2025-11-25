package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "Token JWT para autenticação nas requisições protegidas")
        String token
) {}