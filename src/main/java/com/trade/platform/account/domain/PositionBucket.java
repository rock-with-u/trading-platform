package com.trade.platform.account.domain;

import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PositionBucket {
    HOLDING_QUANTITY("보유 수량"),
    SELL_RESERVED_QUANTITY("매도 예약 수량");

    private final String description;

    public static PositionBucket of(String bucket) {
        if (bucket == null || bucket.isBlank()) {
            throw new AccountException(ResponseMessage.INVALID_POSITION_BUCKET);
        }

        for (PositionBucket positionBucket : values()) {
            if (bucket.equals(positionBucket.name())) {
                return positionBucket;
            }
        }

        throw new AccountException(ResponseMessage.INVALID_POSITION_BUCKET);
    }
}
