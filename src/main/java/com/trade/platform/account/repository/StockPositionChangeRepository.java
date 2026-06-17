package com.trade.platform.account.repository;

import com.trade.platform.account.domain.StockPositionChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPositionChangeRepository extends JpaRepository<StockPositionChange, Long> {
}
