package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;

public class IdempotencyException extends RuntimeException {

    private final ResponseMessage responseMessage;


    public IdempotencyException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
