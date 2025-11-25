package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartUpdateDto(
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        @Schema(description = "Nova quantidade desejada para o item", example = "5")
        Integer quantity
) {}