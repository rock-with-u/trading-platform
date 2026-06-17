package com.trade.platform.order.domain;

import com.trade.platform.common.exception.StockOrderException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderPriceType {
    MARKET("시장가"),
    LIMIT("지정가");

    private final String description;

    public static OrderPriceType of(String type) {
        if (type == null || type.isBlank()) {
            throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_TYPE);
        }

        for (OrderPriceType stockOrderType : values()) {
            if (type.equals(stockOrderType.name())) {
                return stockOrderType;
            }
        }

        throw new StockOrderException(ResponseMessage.INVALID_STOCK_ORDER_TYPE);
    }
}
