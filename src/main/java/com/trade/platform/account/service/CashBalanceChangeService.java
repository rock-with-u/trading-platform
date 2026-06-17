package com.trade.platform.account.service;

import com.trade.platform.account.domain.AccountPosting;
import com.trade.platform.account.domain.CashBalanceChange;
import com.trade.platform.account.domain.CashBucket;
import com.trade.platform.account.domain.result.BuyReservationResult;
import com.trade.platform.account.repository.CashBalanceChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashBalanceChangeService {

    private final CashBalanceChangeRepository cashBalanceChangeRepository;

    public void writeBuyReservation(
            AccountPosting posting,
            long settledBefore,
            long unsettledBefore,
            long reservedBefore,
            BuyReservationResult result
    ) {
        long unsettledAfter = unsettledBefore - result.usedUnsettledSellReceivableAmount();
        long settledAfter = settledBefore - result.usedSettledCashAmount();
        long reservedAfter = reservedBefore + result.reservedBuyAmount();

        if (result.usedUnsettledSellReceivableAmount() > 0) {
            cashBalanceChangeRepository.save(
                    CashBalanceChange.create(
                            posting,
                            CashBucket.UNSETTLED_SELL_RECEIVABLE,
                            unsettledBefore,
                            unsettledAfter
                    )
            );
        }

        if (result.usedSettledCashAmount() > 0) {
            cashBalanceChangeRepository.save(
                    CashBalanceChange.create(
                            posting,
                            CashBucket.SETTLED_CASH,
                            settledBefore,
                            settledAfter
                    )
            );
        }

        cashBalanceChangeRepository.save(
                CashBalanceChange.create(
                        posting,
                        CashBucket.RESERVED_BUY,
                        reservedBefore,
                        reservedAfter
                )
        );
    }
}
