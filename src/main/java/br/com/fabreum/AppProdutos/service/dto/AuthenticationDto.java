package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationDto(
        @Schema(description = "E-mail do usuário", example = "admin@loja.com")
        String login,

        @Schema(description = "Senha", example = "123456")
        String password
) {}