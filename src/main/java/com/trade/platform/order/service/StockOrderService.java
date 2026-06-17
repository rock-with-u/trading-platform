package com.trade.platform.order.service;

import com.trade.platform.order.dto.OrderRequestDto;
import com.trade.platform.order.dto.OrderRequestDto.CreateOrderDto;
import jakarta.validation.Valid;

public interface StockOrderService {
    void createNewOrder(OrderRequestDto.CreateOrderDto createOrderDto);
}
