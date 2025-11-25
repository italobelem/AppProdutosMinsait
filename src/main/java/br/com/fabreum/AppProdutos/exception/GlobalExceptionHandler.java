package br.com.fabreum.AppProdutos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<ErrorResponseDto.ValidationError> errors = new ArrayList<>();

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponseDto.ValidationError(x.getField(), x.getDefaultMessage()));
        }

        ErrorResponseDto err = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                "Erro de Validação",
                "Verifique os campos informados",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponseDto err = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                "Acesso Negado",
                "Você não tem permissão para realizar esta operação (Requer privilégios elevados).",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ErrorResponseDto err = new ErrorResponseDto(
                Instant.now(),
                status.value(),
                "Não Autorizado",
                "Erro de Autenticação: Usuário ou senha inválidos.",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessRule(RuntimeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (e.getMessage().toLowerCase().contains("não encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }

        ErrorResponseDto err = new ErrorResponseDto(
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