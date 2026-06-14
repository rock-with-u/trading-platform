package com.trade.platform.execution.domain;

import com.trade.platform.common.entity.BaseEntity;
import com.trade.platform.common.exception.TradeExecutionException;
import com.trade.platform.common.response.ResponseMessage;
import com.trade.platform.order.domain.StockOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "trade_execution")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeExecution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_execution_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_order_id", nullable = false)
    private StockOrder stockOrder;

    @Column(name = "execution_quantity", nullable = false)
    private Long executionQuantity;

    @Column(name = "execution_unit_price", nullable = false)
    private Long executionUnitPrice;

    @Column(name = "execution_price", nullable = false)
    private Long executionAmount;

    @Column(name = "executed_at", nullable = false)
    private LocalDateTime executedAt;

    @Column(name = "execution_id", nullable = false, unique = true, length = 100)
    private String executionId;

    private TradeExecution(
            StockOrder stockOrder,
            long quantity,
            long unitPrice,
            LocalDateTime executedAt,
            String executionId
    ) {
        if (quantity <= 0) {
            throw new TradeExecutionException(ResponseMessage.INVALID_TRADE_EXECUTION_QUANTITY);
        }
        if (unitPrice <= 0) {
            throw new TradeExecutionException(ResponseMessage.INVALID_TRADE_EXECUTION_UNIT_PRICE);
        }
        this.stockOrder = stockOrder;
        this.executionQuantity = quantity;
        this.executionUnitPrice = unitPrice;
        this.executionAmount = quantity * unitPrice;
        this.executedAt = executedAt;
        this.executionId = executionId;
    }

    public static TradeExecution create(
            StockOrder stockOrder,
            long quantity,
            long unitPrice,
            LocalDateTime executedAt,
            String executionId
    ) {
        return new TradeExecution(stockOrder, quantity, unitPrice, executedAt, executionId);
    }
}
