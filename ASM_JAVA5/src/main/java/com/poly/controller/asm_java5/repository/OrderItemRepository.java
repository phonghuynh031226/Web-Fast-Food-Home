package com.poly.controller.asm_java5.repository;

import com.poly.controller.asm_java5.entity.OrderItem;
import com.poly.controller.asm_java5.model.BestSellerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
        SELECT new com.poly.controller.asm_java5.model.BestSellerDTO(
            oi.menuItem,
            SUM(oi.quantity)
        )
        FROM OrderItem oi
        WHERE oi.menuItem.status = true
        GROUP BY oi.menuItem
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<BestSellerDTO> findBestSellingProducts();

    List<OrderItem> findByOrder_OrderId(Integer orderId);

    void deleteByOrder_OrderId(Integer orderId);
}
