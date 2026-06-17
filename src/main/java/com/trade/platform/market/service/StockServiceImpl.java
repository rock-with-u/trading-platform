package com.trade.platform.market.service;

import com.trade.platform.common.exception.StockException;
import com.trade.platform.common.response.ResponseMessage;
import com.trade.platform.market.domain.Stock;
import com.trade.platform.market.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional(readOnly = true)
    public Stock getByStockCode(String stockCode) {
        return stockRepository
                .findByCode(stockCode)
                .orElseThrow(() -> new StockException(ResponseMessage.STOCK_NOT_FOUND));
    }
}
