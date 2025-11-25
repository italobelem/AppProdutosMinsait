package br.com.fabreum.AppProdutos.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemDto(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
) {}