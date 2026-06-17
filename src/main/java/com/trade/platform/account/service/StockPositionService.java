package com.trade.platform.account.service;

import com.trade.platform.account.domain.Account;
import com.trade.platform.account.domain.StockPosition;
import com.trade.platform.account.repository.StockPositionRepository;
import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import com.trade.platform.market.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPositionService {
    private final StockPositionRepository stockPositionRepository;

    public StockPosition getByAccount(Account account, Stock stock) {
        return stockPositionRepository
                .findByAccountAndStock(account, stock)
                .orElseThrow(() -> new AccountException(ResponseMessage.STOCK_POSITION_NOT_FOUND));
    }
}
