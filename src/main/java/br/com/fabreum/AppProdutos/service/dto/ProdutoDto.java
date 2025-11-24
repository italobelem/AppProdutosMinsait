package br.com.fabreum.AppProdutos.service.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProdutoDto(Long id, String codigoBarras, String nome, BigDecimal preco) {
}
