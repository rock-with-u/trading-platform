package com.trade.platform.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SUCCESS("SUCCESS-001", "요청을 성공척으로 처리했습니다"),
    FAIL("FAIL-001", "요청을 실패했습니다"), 
    INVALID_ACCOUNT_STATUS("ACCOUNT-ERROR-001", "옳바르지 않는 계좌 상태입니다."),
    INVALID_POSTING_STATUS("ACCOUNT-ERROR-002", "지원하지 않는 전표 상태입니다."),
    INVALID_CASH_BUCKET("ACCOUNT-ERROR-003", "지원하지 않는 금액 상태입니다."),
    INVALID_CASH_TRANSACTION_TYPE("ACCOUNT-ERROR-004", "지원하지 않는 입출금 상태입니다."),
    INVALID_POSTING_SOURCE_TYPE("ACCOUNT-ERROR-005", "지원하지 않는 소스 타입입니다."),
    INVALID_CASH_TRANSACTION_AMOUNT("ACCOUNT-ERROR-006", "입출금 금액은 0보다 커야 합니다.");

    private final String code;
    private final String message;
}
