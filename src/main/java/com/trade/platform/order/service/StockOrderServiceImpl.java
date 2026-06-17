package com.trade.platform.order.service;

import com.trade.platform.account.domain.Account;
import com.trade.platform.account.domain.AccountPosting;
import com.trade.platform.account.domain.PostingSourceType;
import com.trade.platform.account.domain.PostingType;
import com.trade.platform.account.domain.StockPosition;
import com.trade.platform.account.domain.result.BuyReservationResult;
import com.trade.platform.account.domain.result.SellReservationResult;
import com.trade.platform.account.repository.AccountPostingRepository;
import com.trade.platform.account.service.AccountService;
import com.trade.platform.account.service.CashBalanceChangeService;
import com.trade.platform.account.service.StockPositionChangeService;
import com.trade.platform.account.service.StockPositionService;
import com.trade.platform.common.IdempotencyKeyGenerator;
import com.trade.platform.common.IdempotencyKeyPrefix;
import com.trade.platform.common.TimeProvider;
import com.trade.platform.market.domain.Stock;
import com.trade.platform.market.service.StockService;
import com.trade.platform.order.domain.OrderPriceType;
import com.trade.platform.order.domain.OrderSide;
import com.trade.platform.order.domain.StockOrder;
import com.trade.platform.order.dto.OrderRequestDto.CreateOrderDto;
import com.trade.platform.order.repository.StockOrderRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockOrderServiceImpl implements StockOrderService {

    private final StockService stockService;
    private final AccountService accountService;
    private final CashBalanceChangeService cashBalanceChangeService;
    private final StockPositionService stockPositionService;
    private final StockPositionChangeService stockPositionChangeService;

    private final StockOrderRepository stockOrderRepository;
    private final AccountPostingRepository accountPostingRepository;

    private final IdempotencyKeyGenerator idempotencyKeyGenerator;
    private final TimeProvider timeProvider;

    @Override
    @Transactional
    public void createNewOrder(CreateOrderDto createOrderDto) {
        Stock stock = stockService.getByStockCode(createOrderDto.stockCode());
        Account account = accountService.getByAccountNumber(createOrderDto.accountNumber());
        OrderSide orderSide = OrderSide.of(createOrderDto.orderSide());
        OrderPriceType orderPriceType = OrderPriceType.of(createOrderDto.priceType());
        LocalDateTime orderedAt = timeProvider.now();

        StockOrder stockOrder = StockOrder.createNew(
                stock,
                account,
                orderSide,
                orderPriceType,
                createOrderDto.orderQuantity(),
                createOrderDto.limitPrice(),
                idempotencyKeyGenerator.generate(IdempotencyKeyPrefix.of("STOCK_ORDER")),
                orderedAt
        );

        if (OrderPriceType.LIMIT == orderPriceType) {
            if (OrderSide.BUY == orderSide) {
                buyLimitPrice(stockOrder, account, createOrderDto, orderedAt);
            } else {
                sellLimitPrice(stockOrder, account, createOrderDto, stock, orderedAt);
            }
        } else {
            // 시장가
        }
    }

    private void buyLimitPrice(StockOrder stockOrder, Account account, CreateOrderDto createOrderDto,
                       LocalDateTime orderedAt) {
        long orderAmount = createOrderDto.limitPrice() * createOrderDto.orderQuantity();
        long settledBefore = account.getAvailableSettledCashAmount();
        long unsettledBefore = account.getUnsettledSellReceivableAmount();
        long reservedBefore = account.getReservedBuyAmount();

        BuyReservationResult reservationResult = account.reserveBuyAmount(orderAmount);
        StockOrder savedOrder = stockOrderRepository.save(stockOrder);

        AccountPosting posting = accountPostingRepository.save(
                AccountPosting.create(
                        account,
                        PostingType.BUY_ORDER_RESERVED,
                        savedOrder.getId(),
                        PostingSourceType.STOCK_ORDER,
                        idempotencyKeyGenerator.generate(IdempotencyKeyPrefix.ACCOUNT_POSTING),
                        orderedAt
                )
        );

        cashBalanceChangeService.writeBuyReservation(
                posting,
                settledBefore,
                unsettledBefore,
                reservedBefore,
                reservationResult
        );
    }

    private void sellLimitPrice(StockOrder stockOrder, Account account, CreateOrderDto createOrderDto,
                        Stock stock, LocalDateTime orderedAt) {
        StockPosition stockPosition = stockPositionService.getByAccount(account, stock);

        SellReservationResult sellReservationResult =
                stockPosition.reserveSell(createOrderDto.orderQuantity());

        StockOrder savedOrder = stockOrderRepository.save(stockOrder);

        AccountPosting posting = accountPostingRepository.save(
                AccountPosting.create(
                        account,
                        PostingType.SELL_QUANTITY_RESERVED,
                        savedOrder.getId(),
                        PostingSourceType.STOCK_ORDER,
                        idempotencyKeyGenerator.generate(IdempotencyKeyPrefix.ACCOUNT_POSTING),
                        orderedAt
                )
        );

        stockPositionChangeService.writeSellReservation(
                posting,
                stock,
                stockPosition,
                sellReservationResult
        );
    }
}
