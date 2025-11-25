package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.UserRole;

public record RegisterDto(String login, String password, UserRole role) {
}