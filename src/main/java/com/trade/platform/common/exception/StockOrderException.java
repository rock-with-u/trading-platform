package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class StockOrderException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public StockOrderException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
