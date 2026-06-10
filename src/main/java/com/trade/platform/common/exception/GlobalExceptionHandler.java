package com.trade.platform.common.exception;

import com.trade.platform.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderException(OrderException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }
}
