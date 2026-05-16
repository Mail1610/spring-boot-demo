package com.demo.dto.response;

import com.demo.entity.Order;
import com.demo.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    String tableNumber,
    OrderStatus status,
    BigDecimal totalAmount,
    List<OrderItemResponse> items,
    LocalDateTime createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getTableNumber(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList(),
            order.getCreatedAt()
        );
    }
}
