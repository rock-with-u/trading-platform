package com.trade.platform.order.repository;

import com.trade.platform.order.domain.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
}
