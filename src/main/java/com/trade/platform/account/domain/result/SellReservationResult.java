package com.trade.platform.account.domain.result;

public record SellReservationResult(
        long sellReservedQuantityBefore,
        long sellReservedQuantityAfter,
        long reservedSellQuantity
) {
}
