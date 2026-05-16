package com.demo.dto.response;

import com.demo.entity.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    String menuItemName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
            item.getId(),
            item.getMenuItem().getName(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
