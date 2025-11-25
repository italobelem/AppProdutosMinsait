package br.com.fabreum.AppProdutos.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Envelope padrão de resposta da API")
public record ApiResponseDto<T>(
        @Schema(description = "Mensagem informativa sobre a operação", example = "Operação realizada com sucesso")
        String message,

        @Schema(description = "Dados retornados (Pode ser nulo em caso de deleção ou erro)")
        T data
) {
    public ApiResponseDto(String message) {
        this(message, null);
    }
}