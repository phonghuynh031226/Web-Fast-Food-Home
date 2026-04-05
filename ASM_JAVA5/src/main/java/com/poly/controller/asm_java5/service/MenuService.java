package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.model.BestSellerDTO;
import com.poly.controller.asm_java5.repository.MenuItemRepository;
import com.poly.controller.asm_java5.repository.MenuRepository;
import com.poly.controller.asm_java5.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poly.controller.asm_java5.entity.Menu;
import java.util.Collections;
import java.util.List;


@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<BestSellerDTO> findBestSellers() {
        List<BestSellerDTO> bestSellers = orderItemRepository.findBestSellingProducts();
        return bestSellers.stream().limit(4).toList();
    }

    public List<MenuItem> findOtherProducts() {
        List<Integer> bestSellerIds = findBestSellers().stream()
                .map(dto -> dto.getMenuItem().getItemId())
                .toList();

        List<MenuItem> others;

        if (bestSellerIds.isEmpty()) {
            others = menuItemRepository.findByStatusTrue();
        } else {
            others = menuItemRepository.findByStatusTrueAndItemIdNotIn(bestSellerIds);
        }

        return others.stream().limit(8).toList();
    }

    public List<Menu> findAllMenus() {
        return menuRepository.findAll();
    }

    public List<MenuItem> findItems(Integer menuId, String sort) {
        boolean hasMenu = menuId != null;
        boolean hasSort = sort != null && !sort.isBlank();

        if (!hasMenu && !hasSort) {
            return menuItemRepository.findByStatusTrue();
        }

        if (hasMenu && !hasSort) {
            return menuItemRepository.findByStatusTrueAndMenu_MenuId(menuId);
        }

        if (!hasMenu) {
            switch (sort) {
                case "priceAsc":
                    return menuItemRepository.findByStatusTrueOrderByPriceAsc();
                case "priceDesc":
                    return menuItemRepository.findByStatusTrueOrderByPriceDesc();
                default:
                    return menuItemRepository.findByStatusTrue();
            }
        }

        switch (sort) {
            case "priceAsc":
                return menuItemRepository.findByStatusTrueAndMenu_MenuIdOrderByPriceAsc(menuId);
            case "priceDesc":
                return menuItemRepository.findByStatusTrueAndMenu_MenuIdOrderByPriceDesc(menuId);
            default:
                return menuItemRepository.findByStatusTrueAndMenu_MenuId(menuId);
        }
    }

    public MenuItem findById(Integer itemId) {
        return menuItemRepository.findByItemIdAndStatusTrue(itemId).orElse(null);
    }

    public List<MenuItem> findSuggestedItems(Integer itemId) {
        MenuItem currentItem = findById(itemId);

        if (currentItem == null || currentItem.getMenu() == null) {
            return Collections.emptyList();
        }

        List<MenuItem> suggested = menuItemRepository
                .findTop4ByStatusTrueAndMenu_MenuIdAndItemIdNotOrderByItemIdDesc(
                        currentItem.getMenu().getMenuId(),
                        itemId
                );

        if (!suggested.isEmpty()) {
            return suggested;
        }

        return menuItemRepository.findByStatusTrue().stream()
                .filter(i -> !i.getItemId().equals(itemId))
                .limit(4)
                .toList();
    }
}