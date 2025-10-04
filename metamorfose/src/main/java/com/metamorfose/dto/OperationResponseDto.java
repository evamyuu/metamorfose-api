package com.metamorfose.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class OperationResponseDto {
    
    private boolean success;
    private String message;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonProperty("operation_type")
    private String operationType;
    
    private Object data;
    
    // Construtores
    public OperationResponseDto() {
        this.timestamp = LocalDateTime.now();
    }
    
    public OperationResponseDto(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }
    
    public OperationResponseDto(boolean success, String message, String operationType) {
        this(success, message);
        this.operationType = operationType;
    }
    
    // Métodos estáticos para facilitar criação
    public static OperationResponseDto success(String message) {
        return new OperationResponseDto(true, message);
    }
    
    public static OperationResponseDto success(String message, Object data) {
        OperationResponseDto response = new OperationResponseDto(true, message);
        response.setData(data);
        return response;
    }
    
    public static OperationResponseDto error(String message) {
        return new OperationResponseDto(false, message);
    }
    
    // Getters e Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}