package com.trade.platform.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SUCCESS("SUCCESS-001", "요청을 성공척으로 처리했습니다"),
    FAIL("FAIL-001", "요청을 실패했습니다");

    private final String code;
    private final String message;
}
