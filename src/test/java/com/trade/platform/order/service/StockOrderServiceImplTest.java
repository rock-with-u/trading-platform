package com.trade.platform.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.trade.platform.account.domain.Account;
import com.trade.platform.account.domain.AccountPosting;
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
import com.trade.platform.common.exception.AccountException;
import com.trade.platform.common.response.ResponseMessage;
import com.trade.platform.market.domain.Stock;
import com.trade.platform.market.service.StockService;
import com.trade.platform.member.domain.Member;
import com.trade.platform.order.domain.StockOrder;
import com.trade.platform.order.dto.OrderRequestDto.CreateOrderDto;
import com.trade.platform.order.repository.StockOrderRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StockOrderServiceImplTest {

    @Mock
    private StockOrderRepository stockOrderRepository;

    @Mock
    private AccountPostingRepository accountPostingRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private StockService stockService;

    @Mock
    private CashBalanceChangeService cashBalanceChangeService;

    @Mock
    private StockPositionService stockPositionService;

    @Mock
    private StockPositionChangeService stockPositionChangeService;

    @Mock
    private IdempotencyKeyGenerator idempotencyKeyGenerator;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private StockOrderServiceImpl stockOrderService;

    private Account account;
    private Stock stock;
    private LocalDateTime orderedAt;

    @BeforeEach
    void setUp() {
        orderedAt = LocalDateTime.of(2026, 6, 17, 10, 0);

        Member member = Member.create(
                "test@email.com",
                "tester"
        );

        account = Account.create(
                member,
                "1234-1234"
        );

        stock = Stock.create(
                "005930",
                "삼성전자"
        );

        ReflectionTestUtils.setField(
                account,
                "availableSettledCashAmount",
                1_000_000L
        );

        ReflectionTestUtils.setField(
                account,
                "unsettledSellReceivableAmount",
                200_000L
        );
    }

    private CreateOrderDto createBuyLimitOrderRequest() {
        return new CreateOrderDto(
                "LIMIT",
                "005930",
                "BUY",
                10L,
                70_000L,
                "1234-1234"
        );
    }

    private CreateOrderDto createSellLimitOrderRequest() {
        return new CreateOrderDto(
                "LIMIT",
                "005930",
                "SELL",
                3L,
                75_000L,
                "1234-1234"
        );
    }

    @Test
    @DisplayName("신규 지정가 매수 주문을 생성한다")
    void createBuyLimitOrderSuccess() {
        // given
        CreateOrderDto request = createBuyLimitOrderRequest();

        given(stockService.getByStockCode("005930"))
                .willReturn(stock);

        given(accountService.getByAccountNumber("1234-1234"))
                .willReturn(account);

        given(timeProvider.now())
                .willReturn(orderedAt);

        given(idempotencyKeyGenerator.generate(any(IdempotencyKeyPrefix.class)))
                .willReturn(
                        "STOCK_ORDER-001",
                        "ACCOUNT_POSTING-001"
                );

        given(stockOrderRepository.save(any(StockOrder.class)))
                .willAnswer(invocation -> {
                    StockOrder savedOrder = invocation.getArgument(0);

                    ReflectionTestUtils.setField(
                            savedOrder,
                            "id",
                            1L
                    );

                    return savedOrder;
                });

        given(accountPostingRepository.save(any(AccountPosting.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        stockOrderService.createNewOrder(request);

        // then
        ArgumentCaptor<BuyReservationResult> reservationCaptor =
                ArgumentCaptor.forClass(BuyReservationResult.class);

        verify(stockService)
                .getByStockCode("005930");

        verify(accountService)
                .getByAccountNumber("1234-1234");

        verify(stockOrderRepository)
                .save(any(StockOrder.class));

        verify(accountPostingRepository)
                .save(any(AccountPosting.class));

        verify(cashBalanceChangeService).writeBuyReservation(
                any(AccountPosting.class),
                eq(1_000_000L),
                eq(200_000L),
                eq(0L),
                reservationCaptor.capture()
        );

        BuyReservationResult reservationResult =
                reservationCaptor.getValue();

        assertThat(reservationResult.usedUnsettledSellReceivableAmount())
                .isEqualTo(200_000L);

        assertThat(reservationResult.usedSettledCashAmount())
                .isEqualTo(500_000L);

        assertThat(reservationResult.reservedBuyAmount())
                .isEqualTo(700_000L);

        assertThat(account.getUnsettledSellReceivableAmount())
                .isZero();

        assertThat(account.getAvailableSettledCashAmount())
                .isEqualTo(500_000L);

        assertThat(account.getReservedBuyAmount())
                .isEqualTo(700_000L);

        verifyNoInteractions(
                stockPositionService,
                stockPositionChangeService
        );
    }

    @Test
    @DisplayName("계좌번호가 존재하지 않으면 주문을 실패한다")
    void createNewOrderFailWhenAccountNotFound() {
        // given
        CreateOrderDto request = createBuyLimitOrderRequest();

        given(stockService.getByStockCode("005930"))
                .willReturn(stock);

        given(accountService.getByAccountNumber("1234-1234"))
                .willThrow(
                        new AccountException(ResponseMessage.ACCOUNT_NOT_FOUND)
                );

        // when, then
        assertThatThrownBy(
                () -> stockOrderService.createNewOrder(request)
        ).isInstanceOf(AccountException.class);

        verify(stockService)
                .getByStockCode("005930");

        verify(accountService)
                .getByAccountNumber("1234-1234");

        verifyNoInteractions(
                timeProvider,
                idempotencyKeyGenerator,
                stockOrderRepository,
                accountPostingRepository,
                cashBalanceChangeService,
                stockPositionService,
                stockPositionChangeService
        );
    }

    @Test
    @DisplayName("매수 가능 금액이 부족하면 지정가 매수 주문을 실패한다")
    void createBuyLimitOrderFailWhenDepositDeficient() {
        // given
        CreateOrderDto request = new CreateOrderDto(
                "LIMIT",
                "005930",
                "BUY",
                20L,
                70_000L,
                "1234-1234"
        );

        /*
         * 주문금액: 70,000 × 20 = 1,400,000원
         * 매수 가능 금액: 1,000,000 + 200,000 = 1,200,000원
         */
        given(stockService.getByStockCode("005930"))
                .willReturn(stock);

        given(accountService.getByAccountNumber("1234-1234"))
                .willReturn(account);

        given(timeProvider.now())
                .willReturn(orderedAt);

        given(idempotencyKeyGenerator.generate(any(IdempotencyKeyPrefix.class)))
                .willReturn("STOCK_ORDER-001");

        // when & then
        assertThatThrownBy(
                () -> stockOrderService.createNewOrder(request)
        ).isInstanceOf(AccountException.class);

        assertThat(account.getAvailableSettledCashAmount())
                .isEqualTo(1_000_000L);

        assertThat(account.getUnsettledSellReceivableAmount())
                .isEqualTo(200_000L);

        assertThat(account.getReservedBuyAmount())
                .isZero();

        verify(stockOrderRepository, never())
                .save(any(StockOrder.class));

        verify(accountPostingRepository, never())
                .save(any(AccountPosting.class));

        verifyNoInteractions(
                cashBalanceChangeService,
                stockPositionService,
                stockPositionChangeService
        );
    }

    @Test
    @DisplayName("신규 지정가 매도 주문을 생성한다")
    void createSellLimitOrderSuccess() {
        // given
        CreateOrderDto request = createSellLimitOrderRequest();

        StockPosition stockPosition = mock(StockPosition.class);
        SellReservationResult sellReservationResult =
                mock(SellReservationResult.class);

        given(stockService.getByStockCode("005930"))
                .willReturn(stock);

        given(accountService.getByAccountNumber("1234-1234"))
                .willReturn(account);

        given(timeProvider.now())
                .willReturn(orderedAt);

        given(idempotencyKeyGenerator.generate(any(IdempotencyKeyPrefix.class)))
                .willReturn(
                        "STOCK_ORDER-002",
                        "ACCOUNT_POSTING-002"
                );

        given(stockPositionService.getByAccount(account, stock))
                .willReturn(stockPosition);

        given(stockPosition.reserveSell(3L))
                .willReturn(sellReservationResult);

        given(stockOrderRepository.save(any(StockOrder.class)))
                .willAnswer(invocation -> {
                    StockOrder savedOrder = invocation.getArgument(0);

                    ReflectionTestUtils.setField(
                            savedOrder,
                            "id",
                            2L
                    );

                    return savedOrder;
                });

        given(accountPostingRepository.save(any(AccountPosting.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        stockOrderService.createNewOrder(request);

        // then
        verify(stockPositionService)
                .getByAccount(account, stock);

        verify(stockPosition)
                .reserveSell(3L);

        verify(stockOrderRepository)
                .save(any(StockOrder.class));

        verify(accountPostingRepository)
                .save(any(AccountPosting.class));

        verify(stockPositionChangeService).writeSellReservation(
                any(AccountPosting.class),
                eq(stock),
                eq(stockPosition),
                eq(sellReservationResult)
        );

        verifyNoInteractions(cashBalanceChangeService);
    }
}
