package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;

public class StockException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public StockException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
