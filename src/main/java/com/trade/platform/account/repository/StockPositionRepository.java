package com.trade.platform.account.repository;

import com.trade.platform.account.domain.Account;
import com.trade.platform.account.domain.StockPosition;
import com.trade.platform.market.domain.Stock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPositionRepository extends JpaRepository<StockPosition, Long> {

    Optional<StockPosition> findByAccountAndStock(Account account, Stock stock);
}
