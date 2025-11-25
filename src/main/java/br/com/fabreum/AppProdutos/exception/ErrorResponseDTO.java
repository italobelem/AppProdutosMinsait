package br.com.fabreum.AppProdutos.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponseDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<ValidationError> validationErrors
) {
    public record ValidationError(String field, String message) {}
}