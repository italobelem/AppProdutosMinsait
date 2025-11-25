package br.com.fabreum.AppProdutos.service.dto;

public record ApiResponseDto<T>(
        String message,
        T data
) {
    public ApiResponseDto(String message) {
        this(message, null);
    }
}