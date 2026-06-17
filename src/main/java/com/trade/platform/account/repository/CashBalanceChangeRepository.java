package com.trade.platform.account.repository;

import com.trade.platform.account.domain.CashBalanceChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashBalanceChangeRepository extends JpaRepository<CashBalanceChange, Long> {
}
