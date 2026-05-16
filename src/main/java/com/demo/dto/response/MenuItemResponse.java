package com.demo.dto.response;

import com.demo.entity.MenuItem;
import java.math.BigDecimal;

public record MenuItemResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    String category,
    String imageUrl,
    boolean available
) {
    public static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getPrice(),
            item.getCategory(),
            item.getImageUrl(),
            item.isAvailable()
        );
    }
}
