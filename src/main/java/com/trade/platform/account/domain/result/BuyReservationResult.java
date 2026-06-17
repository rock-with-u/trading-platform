package com.trade.platform.account.domain.result;

public record BuyReservationResult(
        long usedSettledCashAmount,
        long usedUnsettledSellReceivableAmount,
        long reservedBuyAmount
) {
}
