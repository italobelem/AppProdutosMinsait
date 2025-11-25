package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemDto(
        @Schema(description = "ID do produto a ser adicionado", example = "1")
        @NotNull
        Long productId,

        @Schema(description = "Quantidade do produto", example = "2")
        @NotNull @Min(1)
        Integer quantity
) {}