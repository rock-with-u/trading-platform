package com.trade.platform.account.service;

import com.trade.platform.account.domain.AccountPosting;
import com.trade.platform.account.domain.PositionBucket;
import com.trade.platform.account.domain.StockPosition;
import com.trade.platform.account.domain.StockPositionChange;
import com.trade.platform.account.domain.result.SellReservationResult;
import com.trade.platform.account.repository.StockPositionChangeRepository;
import com.trade.platform.market.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPositionChangeService {

    private final StockPositionChangeRepository stockPositionChangeRepository;

    public void writeSellReservation(
            AccountPosting posting,
            Stock stock,
            StockPosition stockPosition,
            SellReservationResult result
    ) {
        stockPositionChangeRepository.save(
                StockPositionChange.create(
                        posting,
                        stock,
                        PositionBucket.SELL_RESERVED_QUANTITY,
                        result.sellReservedQuantityBefore(),
                        result.sellReservedQuantityAfter(),
                        stockPosition.getAveragePrice(),
                        stockPosition.getAveragePrice()
                )
        );
    }

}
