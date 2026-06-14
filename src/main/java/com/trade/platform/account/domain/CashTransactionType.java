package com.trade.platform.account.domain;

import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CashTransactionType {
    DEPOSIT("입금"),
    WITHDRAWAL("출금");

    private final String description;

    public static CashTransactionType of(String type) {
        if (type == null || type.isBlank()) {
            throw new AccountException(ResponseMessage.INVALID_CASH_TRANSACTION_TYPE);
        }

        for (CashTransactionType transactionType : values()) {
            if (type.equals(transactionType.name())) {
                return transactionType;
            }
        }

        throw new AccountException(ResponseMessage.INVALID_CASH_TRANSACTION_TYPE);
    }
}
