package com.demo.service;

import com.demo.dto.request.OrderRequest;
import com.demo.dto.response.OrderResponse;
import com.demo.entity.*;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.MenuItemRepository;
import com.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public List<OrderResponse> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(OrderResponse::from)
            .toList();
    }

    public List<OrderResponse> findByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status).stream()
            .map(OrderResponse::from)
            .toList();
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Order order = Order.builder()
            .tableNumber(request.tableNumber())
            .status(OrderStatus.PENDING)
            .totalAmount(BigDecimal.ZERO)
            .build();

        List<OrderItem> items = new ArrayList<>();
        for (var itemReq : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", itemReq.menuItemId()));
            items.add(OrderItem.builder()
                .order(order)
                .menuItem(menuItem)
                .quantity(itemReq.quantity())
                .unitPrice(menuItem.getPrice())
                .build());
        }

        order.setOrderItems(items);
        order.setTotalAmount(items.stream()
            .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        order.setStatus(status);
        return OrderResponse.from(orderRepository.save(order));
    }
}
