package com.trade.platform.account.service;

import com.trade.platform.account.domain.Account;
import com.trade.platform.account.repository.AccountRepository;
import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ResponseMessage.ACCOUNT_NOT_FOUND));
    }
}
