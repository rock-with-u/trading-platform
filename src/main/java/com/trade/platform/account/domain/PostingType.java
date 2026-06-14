package com.trade.platform.account.domain;

import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostingType {
    DEPOSIT("입금"),
    WITHDRAWAL("출금"),

    BUY_ORDER_RESERVED("매수 대금 예약"),
    BUY_ORDER_RELEASED("매수 예약금 해제"),
    BUY_EXECUTED("매수 체결"),

    SELL_QUANTITY_RESERVED("매도 수량 예약"),
    SELL_QUANTITY_RELEASED("매도 예약 수량 해제"),
    SELL_EXECUTED("매도 체결"),

    TRADE_SETTLED("결제");

    private final String description;

    public static PostingType of(String type) throws AccountException {
        if (type == null || type.isBlank()) {
            throw new AccountException(ResponseMessage.INVALID_POSTING_STATUS);
        }

        for (PostingType postingType : PostingType.values()) {
            if (type.equals(postingType.name())) {
                return postingType;
            }
        }

        throw new AccountException(ResponseMessage.INVALID_POSTING_STATUS);
    }
}
