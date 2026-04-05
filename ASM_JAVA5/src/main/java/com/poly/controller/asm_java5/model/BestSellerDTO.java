package com.poly.controller.asm_java5.model;

import com.poly.controller.asm_java5.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestSellerDTO {
    private MenuItem menuItem;
    private Long totalSold;
}