package com.demo.service;

import com.demo.entity.Item;
import com.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> search(String keyword) {
        return itemRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> update(Long id, Item updated) {
        return itemRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            return itemRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!itemRepository.existsById(id)) return false;
        itemRepository.deleteById(id);
        return true;
    }
}
