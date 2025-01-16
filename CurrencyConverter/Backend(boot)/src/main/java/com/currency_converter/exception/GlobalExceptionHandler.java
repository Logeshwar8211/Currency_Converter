package com.currency_converter.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(InvalidCurrencyException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidCurrencyException(InvalidCurrencyException ex, WebRequest request){
		Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Invalid Currency");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Server Error");
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
