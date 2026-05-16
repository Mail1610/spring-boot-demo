package com.demo.config;

import com.demo.entity.MenuItem;
import com.demo.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;

    @Override
    public void run(String... args) {
        if (menuItemRepository.count() > 0) return;

        menuItemRepository.save(MenuItem.builder().name("招牌漢堡").description("牛肉漢堡配生菜番茄").price(new BigDecimal("120")).category("主食").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("炸雞套餐").description("香酥炸雞附薯條").price(new BigDecimal("150")).category("主食").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("番茄義大利麵").description("新鮮番茄醬汁寬麵").price(new BigDecimal("130")).category("主食").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("凱薩沙拉").description("新鮮蘿蔓生菜佐凱薩醬").price(new BigDecimal("90")).category("輕食").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("薯條").description("黃金酥脆薯條").price(new BigDecimal("50")).category("小食").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("可樂").description("冰涼可口可樂").price(new BigDecimal("35")).category("飲料").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("柳橙汁").description("現打鮮榨柳橙汁").price(new BigDecimal("60")).category("飲料").available(true).build());
        menuItemRepository.save(MenuItem.builder().name("巧克力蛋糕").description("濃郁巧克力熔岩蛋糕").price(new BigDecimal("80")).category("甜點").available(true).build());
    }
}
