package com.poly.controller.asm_java5.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Cart")
@Data
public class Cart {

    @EmbeddedId
    private CartId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private MenuItem menuItem;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}