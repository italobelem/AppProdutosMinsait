package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockDto(
        @Schema(description = "ID do produto", example = "1")
        @NotNull
        Long productId,

        @Schema(description = "Quantidade para adicionar ou remover", example = "50")
        @NotNull @Min(1)
        Integer quantity
) {}