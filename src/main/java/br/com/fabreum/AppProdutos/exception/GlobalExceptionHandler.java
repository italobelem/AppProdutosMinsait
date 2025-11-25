package br.com.fabreum.AppProdutos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400
        List<ErrorResponseDTO.ValidationError> errors = new ArrayList<>();

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponseDTO.ValidationError(x.getField(), x.getDefaultMessage()));
        }

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Erro de Validação",
                "Verifique os campos informados",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRule(RuntimeException e, HttpServletRequest request) {
        if (e instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponseDTO err = new ErrorResponseDTO(
                Instant.now(),
                status.value(),
                "Erro no Processamento",
                e.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(status).body(err);
    }
}