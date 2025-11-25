package br.com.fabreum.AppProdutos.service.dto;

public record CategoryDto(
        Long id,
        String name,
        String parentName
) {}