package br.com.java_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCepNotFoundException(
            CepNotFoundException ex, WebRequest request) {
        
        Map<String, Object> errorDetails = createErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "CEP não encontrado",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<Map<String, Object>> handleExternalApiException(
            ExternalApiException ex, WebRequest request) {
        
        Map<String, Object> errorDetails = createErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Erro no serviço externo",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        Map<String, Object> errorDetails = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Dados inválidos",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        
        Map<String, Object> errorDetails = createErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Dados inválidos",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        Map<String, Object> errorDetails = createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor",
            "Ocorreu um erro inesperado",
            request.getDescription(false)
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorResponse(int status, String error, String message, String path) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status);
        errorDetails.put("error", error);
        errorDetails.put("message", message);
        errorDetails.put("path", path.replace("uri=", ""));
        return errorDetails;
    }
}