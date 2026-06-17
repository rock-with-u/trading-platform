package com.trade.platform.order.controller;

import com.trade.platform.common.response.ApiResponse;
import com.trade.platform.common.response.ResponseMessage;
import com.trade.platform.order.dto.OrderRequestDto;
import com.trade.platform.order.service.StockOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class StockOrderController {

    private final StockOrderService stockOrderService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createOrder(
            @Valid
            @RequestBody
            OrderRequestDto.CreateOrderDto createOrderDto
    ) {

        stockOrderService.createNewOrder(createOrderDto);

        return ResponseEntity
                .ok()
                .body(ApiResponse.succeed(ResponseMessage.SUCCESS.getCode(), ResponseMessage.SUCCESS.getMessage(),
                        null));
    }
}
