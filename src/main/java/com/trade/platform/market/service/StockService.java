package com.trade.platform.market.service;

import com.trade.platform.market.domain.Stock;

public interface StockService {

    Stock getByStockCode(String stockCode);
}
