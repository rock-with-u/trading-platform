package com.trade.platform.order.domain;

import com.trade.platform.common.exception.StockOrderException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderSide {
    BUY("매수"),
    SELL("매도");

    private final String description;

    public static OrderSide of(String requestedOrderSide) {
        if (requestedOrderSide == null || requestedOrderSide.isBlank()) {
            throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_SIDE);
        }

        for (OrderSide orderSide : values()) {
            if (requestedOrderSide.equals(orderSide.name())) {
                return orderSide;
            }
        }

        throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_SIDE);
    }
}
