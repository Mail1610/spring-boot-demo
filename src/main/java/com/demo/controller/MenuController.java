package com.demo.controller;

import com.demo.common.ApiResponse;
import com.demo.dto.request.MenuItemRequest;
import com.demo.dto.response.MenuItemResponse;
import com.demo.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ApiResponse<List<MenuItemResponse>> getAvailable() {
        return ApiResponse.ok(menuService.findAllAvailable());
    }

    @GetMapping("/all")
    public ApiResponse<List<MenuItemResponse>> getAll() {
        return ApiResponse.ok(menuService.findAll());
    }

    @PostMapping
    public ApiResponse<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        return ApiResponse.ok(menuService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<MenuItemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return ApiResponse.ok(menuService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ApiResponse.ok(null);
    }
}
