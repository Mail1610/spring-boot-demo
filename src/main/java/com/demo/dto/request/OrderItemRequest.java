package com.demo.dto.request;

import jakarta.validation.constraints.*;

public record OrderItemRequest(
    @NotNull Long menuItemId,
    @NotNull @Min(1) Integer quantity
) {}
