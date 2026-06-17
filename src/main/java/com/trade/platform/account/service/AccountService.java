package com.trade.platform.account.service;

import com.trade.platform.account.domain.Account;

public interface AccountService {

    Account getByAccountNumber(String accountNumber);
}
