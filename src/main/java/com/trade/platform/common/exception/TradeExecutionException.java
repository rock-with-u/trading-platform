package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;

public class TradeExecutionException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public TradeExecutionException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
