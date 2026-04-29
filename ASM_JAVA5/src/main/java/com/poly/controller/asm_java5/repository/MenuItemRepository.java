package com.poly.controller.asm_java5.repository;

import com.poly.controller.asm_java5.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    List<MenuItem> findByStatusTrue();

    List<MenuItem> findByStatusTrueAndItemIdNotIn(List<Integer> excludeIds);

    List<MenuItem> findByStatusTrueAndMenu_MenuId(Integer menuId);

    List<MenuItem> findByStatusTrueOrderByPriceAsc();

    List<MenuItem> findByStatusTrueOrderByPriceDesc();

    List<MenuItem> findByStatusTrueAndMenu_MenuIdOrderByPriceAsc(Integer menuId);

    List<MenuItem> findByStatusTrueAndMenu_MenuIdOrderByPriceDesc(Integer menuId);

    List<MenuItem> findByStatusTrueAndItemNameContainingIgnoreCase(String keyword);

    List<MenuItem> findByStatusTrueAndMenu_MenuIdAndItemNameContainingIgnoreCase(Integer menuId, String keyword);

    List<MenuItem> findByStatusTrueAndItemNameContainingIgnoreCaseOrderByPriceAsc(String keyword);

    List<MenuItem> findByStatusTrueAndItemNameContainingIgnoreCaseOrderByPriceDesc(String keyword);

    List<MenuItem> findByStatusTrueAndMenu_MenuIdAndItemNameContainingIgnoreCaseOrderByPriceAsc(Integer menuId, String keyword);

    List<MenuItem> findByStatusTrueAndMenu_MenuIdAndItemNameContainingIgnoreCaseOrderByPriceDesc(Integer menuId, String keyword);

    Optional<MenuItem> findByItemIdAndStatusTrue(Integer itemId);

    List<MenuItem> findTop4ByStatusTrueAndMenu_MenuIdAndItemIdNotOrderByItemIdDesc(Integer menuId, Integer itemId);
}
