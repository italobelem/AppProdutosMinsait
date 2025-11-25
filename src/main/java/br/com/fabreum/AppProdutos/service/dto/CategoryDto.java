package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryDto(
        @Schema(description = "ID da categoria", example = "2")
        Long id,

        @Schema(description = "Nome da categoria", example = "Periféricos")
        String name,

        @Schema(description = "Nome da categoria pai (se houver)", example = "Informática")
        String parentName
) {}