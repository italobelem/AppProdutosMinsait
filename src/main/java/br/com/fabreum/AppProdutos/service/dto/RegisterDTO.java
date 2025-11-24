package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}