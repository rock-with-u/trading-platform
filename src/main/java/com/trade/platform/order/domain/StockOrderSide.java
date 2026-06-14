package com.trade.platform.order.domain;

import com.trade.platform.common.exception.StockOrderException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StockOrderSide {
    BUY("매수"),
    SELL("매도");

    private final String description;

    public static StockOrderSide of(String orderSide) {
        if (orderSide == null || orderSide.isBlank()) {
            throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_SIDE);
        }

        for (StockOrderSide stockOrderSide : values()) {
            if (orderSide.equals(stockOrderSide.name())) {
                return stockOrderSide;
            }
        }

        throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_SIDE);
    }
}
