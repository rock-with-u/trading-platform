package com.trade.platform.common.exception;

import com.trade.platform.common.response.ApiResponse;
import com.trade.platform.common.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockOrderException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderException(StockOrderException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiResponse<String>> handleAccountException(AccountException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(TradeExecutionException.class)
    public ResponseEntity<ApiResponse<String>> handleTradeExecutionException(TradeExecutionException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MarketException.class)
    public ResponseEntity<ApiResponse<String>> handleMarketException(MarketException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberException(MemberException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse(ResponseMessage.INVALID_REQUEST_FORMAT.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(
                        ResponseMessage.INVALID_REQUEST_FORMAT.getCode(),
                        message
                ));
    }
}
