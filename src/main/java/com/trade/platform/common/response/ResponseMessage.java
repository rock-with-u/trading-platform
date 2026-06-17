package com.trade.platform.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    SUCCESS("SUCCESS-001", "요청을 성공척으로 처리했습니다"),
    FAIL("FAIL-001", "요청을 실패했습니다"),
    INVALID_REQUEST_FORMAT("FORMAT-001", "올바르지 않은 입력 형식입니다."),
    INVALID_IDEMPOTENCY_KEY("IDEMPOTENCY-001", "올바르지 않은 멱등 타입 입니다."),

    // ACCOUNT
    INVALID_ACCOUNT_STATUS("ACCOUNT-ERROR-001", "올바르지 않는 계좌 상태입니다."),
    INVALID_POSTING_STATUS("ACCOUNT-ERROR-002", "지원하지 않는 전표 상태입니다."),
    INVALID_CASH_BUCKET("ACCOUNT-ERROR-003", "지원하지 않는 금액 상태입니다."),
    INVALID_CASH_TRANSACTION_TYPE("ACCOUNT-ERROR-004", "지원하지 않는 입출금 상태입니다."),
    INVALID_POSTING_SOURCE_TYPE("ACCOUNT-ERROR-005", "지원하지 않는 소스 타입입니다."),
    INVALID_CASH_TRANSACTION_AMOUNT("ACCOUNT-ERROR-006", "입출금 금액은 0보다 커야 합니다."),
    INVALID_POSITION_BUCKET("ACCOUNT-ERROR-007", "올바르지 않은 "),
    ACCOUNT_NOT_FOUND("ACCOUNT-ERROR-008", "계좌를 찾을 수 없습니다."),
    DEPOSIT_DEFICIENT("ACCOUNT-ERROR-009", "예수금이 충분하지 않습니다."),
    STOCK_POSITION_NOT_FOUND("ACCOUNT-ERROR-010", "보유 주식이 없습니다."),
    STOCK_QUANTITY_DEFICIENT("ACCOUNT-ERROR-011", "보유 수량이 부족합니다"),

    // ORDER
    INVALID_STOCK_ORDER_SIDE("STOCK-ORDER-SIDE-001", "구매 방식은 매도 또는 매수만 지원합니다."),
    INVALID_STOCK_ORDER_TYPE("STOCK-ORDER-SIDE-002", "가격 지정 방식은 시장가 또는 지정가만 지원합니다."),
    INVALID_STOCK_ORDER_ACTION("STOCK-ORDER-003", "주문은 신규 주문, 정정, 취소를 지원합니다."),
    INVALID_STOCK_ORDER_STATUS("STOCK-ORDER-004", "주문 상태는 전량 체결, 부분 체결, 대기, 주문 취소, 주문 거절만 지원합니다"),
    INVALID_MARKET_ORDER_PRICE("STOCK-ORDER-005", "주문 가격이 올바르지 않습니다."),

    INVALID_TRADE_EXECUTION_QUANTITY("TRADE-EXECUTION-001", "체결수량은 0보다 커야합니다."),
    INVALID_TRADE_EXECUTION_UNIT_PRICE("TRADE-EXECUTION-002", "체결 단가는 0보다 커야 합니다."),

    // CANDLE
    INVALID_CANDLE_TIMEFRAME("MARKET-001", "지원하지 않는 시간 단위입니다"),

    // STOCK
    STOCK_NOT_FOUND("STOCK-ERROR-001", "지원하지 않는 주식입니다.");


    private final String code;
    private final String message;
}
