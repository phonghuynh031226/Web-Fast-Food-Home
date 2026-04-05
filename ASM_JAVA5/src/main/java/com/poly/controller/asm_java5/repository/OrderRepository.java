package com.poly.controller.asm_java5.repository;

import com.poly.controller.asm_java5.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserId(Integer userId);
}