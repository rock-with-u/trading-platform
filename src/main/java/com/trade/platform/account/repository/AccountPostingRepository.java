package com.trade.platform.account.repository;

import com.trade.platform.account.domain.AccountPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPostingRepository extends JpaRepository<AccountPosting, Long> {
}
