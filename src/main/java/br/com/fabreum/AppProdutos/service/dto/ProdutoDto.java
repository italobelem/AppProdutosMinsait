package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProdutoDto(
        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Código de barras", example = "COD-1234")
        String codigoBarras,

        @Schema(description = "Nome do produto", example = "Mouse Gamer")
        String nome,

        @Schema(description = "Preço atual", example = "150.00")
        BigDecimal preco
) {}