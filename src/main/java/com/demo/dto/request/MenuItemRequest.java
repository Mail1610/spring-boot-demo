package com.demo.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MenuItemRequest(
    @NotBlank String name,
    String description,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @NotBlank String category,
    String imageUrl,
    boolean available
) {}
