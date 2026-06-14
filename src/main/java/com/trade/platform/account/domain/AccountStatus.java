package com.trade.platform.account.domain;

import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AccountStatus {
    ENABLE("활성화"),
    DISABLE("비활성화");

    private final String description;

    public static AccountStatus of(String status) {
        if (status == null || status.isBlank()) {
            throw new AccountException(ResponseMessage.INVALID_ACCOUNT_STATUS);
        }

        for (AccountStatus accountStatus : values()) {
            if (status.equals(accountStatus.name())) {
                return accountStatus;
            }
        }

        throw new AccountException(ResponseMessage.INVALID_ACCOUNT_STATUS);
    }
}
