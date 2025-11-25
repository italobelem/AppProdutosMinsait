package br.com.fabreum.AppProdutos.service.dto;

import br.com.fabreum.AppProdutos.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDto(
        @Schema(description = "E-mail para cadastro", example = "cliente@gmail.com")
        String login,

        @Schema(description = "Senha segura", example = "123456")
        String password,

        @Schema(description = "Perfil de acesso", example = "CUSTOMER")
        UserRole role
) {}