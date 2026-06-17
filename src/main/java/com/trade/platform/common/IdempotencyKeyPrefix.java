package com.trade.platform.common;

import com.trade.platform.common.exception.StockOrderException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IdempotencyKeyPrefix {

    STOCK_ORDER("ORD"),
    ACCOUNT_POSTING("ACCOUNT_POSTING");

    public static IdempotencyKeyPrefix of(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            throw new StockOrderException(ResponseMessage.INVALID_IDEMPOTENCY_KEY);
        }

        for (IdempotencyKeyPrefix idempotencyKeyPrefix : values()) {
            if (prefix.equals(idempotencyKeyPrefix.name())) {
                return idempotencyKeyPrefix;
            }
        }

        throw new StockOrderException(ResponseMessage.INVALID_IDEMPOTENCY_KEY);
    }

    private final String code;
}
