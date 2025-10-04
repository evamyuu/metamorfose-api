package com.metamorfose.exception;

import com.metamorfose.dto.OperationResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manipulador global de exceções
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<OperationResponseDto> handleDatabaseException(DatabaseException e) {
        logger.error("Erro de banco de dados", e);
        
        OperationResponseDto response = OperationResponseDto.error(
            "Erro de banco de dados: " + e.getMessage());
        response.setOperationType("DATABASE_ERROR");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<OperationResponseDto> handleIllegalArgument(IllegalArgumentException e) {
        logger.warn("Argumento inválido", e);
        
        OperationResponseDto response = OperationResponseDto.error(
            "Parâmetro inválido: " + e.getMessage());
        response.setOperationType("VALIDATION_ERROR");
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OperationResponseDto> handleValidation(MethodArgumentNotValidException e) {
        logger.warn("Erro de validação", e);
        
        StringBuilder message = new StringBuilder("Erro de validação: ");
        e.getBindingResult().getFieldErrors().forEach(error -> 
            message.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; "));
        
        OperationResponseDto response = OperationResponseDto.error(message.toString());
        response.setOperationType("VALIDATION_ERROR");
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OperationResponseDto> handleGenericException(Exception e) {
        logger.error("Erro interno do servidor", e);
        
        OperationResponseDto response = OperationResponseDto.error(
            "Erro interno do servidor");
        response.setOperationType("INTERNAL_ERROR");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
