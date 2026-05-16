package com.demo.controller;

import com.demo.common.ApiResponse;
import com.demo.dto.request.OrderRequest;
import com.demo.dto.request.UpdateOrderStatusRequest;
import com.demo.dto.response.OrderResponse;
import com.demo.entity.OrderStatus;
import com.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAll(
            @RequestParam(required = false) OrderStatus status) {
        if (status != null) {
            return ApiResponse.ok(orderService.findByStatus(status));
        }
        return ApiResponse.ok(orderService.findAll());
    }

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ApiResponse.ok(orderService.create(request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.ok(orderService.updateStatus(id, request.status()));
    }
}
