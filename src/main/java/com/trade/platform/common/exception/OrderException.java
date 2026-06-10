package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public OrderException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
