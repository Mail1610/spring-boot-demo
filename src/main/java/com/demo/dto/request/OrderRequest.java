package com.demo.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
    @NotBlank String tableNumber,
    @NotEmpty List<OrderItemRequest> items
) {}
