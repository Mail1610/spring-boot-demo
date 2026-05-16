package com.demo.service;

import com.demo.dto.request.MenuItemRequest;
import com.demo.dto.response.MenuItemResponse;
import com.demo.entity.MenuItem;
import com.demo.exception.ResourceNotFoundException;
import com.demo.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public List<MenuItemResponse> findAllAvailable() {
        return menuItemRepository.findByAvailableTrue().stream()
            .map(MenuItemResponse::from)
            .toList();
    }

    public List<MenuItemResponse> findAll() {
        return menuItemRepository.findAll().stream()
            .map(MenuItemResponse::from)
            .toList();
    }

    public MenuItemResponse create(MenuItemRequest request) {
        MenuItem item = MenuItem.builder()
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .category(request.category())
            .imageUrl(request.imageUrl())
            .available(request.available())
            .build();
        return MenuItemResponse.from(menuItemRepository.save(item));
    }

    public MenuItemResponse update(Long id, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MenuItem", id));
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setCategory(request.category());
        item.setImageUrl(request.imageUrl());
        item.setAvailable(request.available());
        return MenuItemResponse.from(menuItemRepository.save(item));
    }

    public void delete(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem", id);
        }
        menuItemRepository.deleteById(id);
    }
}
