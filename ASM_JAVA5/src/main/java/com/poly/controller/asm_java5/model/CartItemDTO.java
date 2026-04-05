package com.poly.controller.asm_java5.model;

import com.poly.controller.asm_java5.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private MenuItem item;
    private Integer quantity;
    private Double totalPrice;
}