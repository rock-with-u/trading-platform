package com.trade.platform.common.exception;

import com.trade.platform.common.response.ResponseMessage;

public class AccountException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public AccountException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getErrorCode() {
        return responseMessage.getCode();
    }
}
