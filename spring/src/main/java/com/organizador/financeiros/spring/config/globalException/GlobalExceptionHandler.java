package com.organizador.financeiros.spring.config.globalException;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.organizador.financeiros.spring.config.globalException.exceotion.BadRequestException;
import com.organizador.financeiros.spring.config.globalException.exceotion.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult result = ex.getBindingResult();
        List<String> details = new ArrayList<>();

        for (FieldError error : result.getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }

        log.warn("Falha de validação em {}: {}", request.getRequestURI(), details);
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação nos campos", details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonErrors(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        String message = "Requisição malformada ou tipo de dado inválido";
        List<String> details = new ArrayList<>();

        if (cause instanceof InvalidFormatException ife) {
            details.add(handleInvalidFormat(ife));
        } else if (cause instanceof JsonParseException) {
            message = "JSON malformado";
            details.add("Erro de sintaxe no JSON. Verifique vírgulas, aspas e chaves.");
        } else if (cause instanceof MismatchedInputException mie) {
            String path = extractFieldPath(mie.getPath());
            details.add("Tipo de dado incompatível no campo '" + path + "'.");
        } else {
            details.add(ex.getMessage().split(";")[0]); // Pega apenas a primeira parte da msg técnica
        }

        log.warn("Erro de leitura JSON em {}: {}", request.getRequestURI(), details);
        return buildResponse(HttpStatus.BAD_REQUEST, message, details);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        log.warn("Regra de negócio violada em {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Erro interno não tratado em " + request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor.", List.of("Contate o suporte."));
    }

    // --- Métodos Auxiliares --- //

    private String handleInvalidFormat(InvalidFormatException ex) {
        String path = extractFieldPath(ex.getPath());
        Object invalidValue = ex.getValue();
        Class<?> targetType = ex.getTargetType();

        if (targetType.isEnum()) {
            String validValues = Arrays.toString(targetType.getEnumConstants());
            return String.format("%s: valor '%s' inválido. Valores aceitos: %s", path, invalidValue, validValues);
        }

        if (targetType.getSimpleName().equals("Boolean")) {
            return String.format("%s: valor '%s' inválido. Boolean esperado: true ou false.", path, invalidValue);
        }

        if (targetType.getSimpleName().equals("LocalDate") || targetType.getSimpleName().equals("LocalDateTime")) {
            return String.format("%s: valor '%s' não pôde ser convertido. Formato esperado: yyyy-MM-dd.", path, invalidValue);
        }

        return String.format("%s: valor '%s' inválido para o tipo %s.", path, invalidValue, targetType.getSimpleName());
    }

    private String extractFieldPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[index]")
                .collect(Collectors.joining("."));
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, List<String> details) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .details(details)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> details;
    }
}