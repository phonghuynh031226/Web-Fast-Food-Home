package com.poly.controller.asm_java5.repository;

import com.poly.controller.asm_java5.entity.Cart;
import com.poly.controller.asm_java5.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartId> {

    List<Cart> findByUser_UserId(Integer userId);

    void deleteByUser_UserId(Integer userId);

    Optional<Cart> findByUser_UserIdAndMenuItem_ItemId(Integer userId, Integer itemId);

    void deleteByUser_UserIdAndMenuItem_ItemId(Integer userId, Integer itemId);


}