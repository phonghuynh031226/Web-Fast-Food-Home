package com.poly.controller.asm_java5.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Menu_Items")
@Data
public class MenuItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "item_id")
        private Integer itemId;

        @ManyToOne
        @JoinColumn(name = "menu_id", nullable = false)
        private Menu menu;

        @Column(name = "item_name", nullable = false, length = 150)
        private String itemName;

        @Column(name = "description", length = 255)
        private String description;

        @Column(name = "image", length = 255)
        private String image;

        @Column(name = "price", nullable = false)
        private Double price;

        @Column(name = "status")
        private Boolean status;
}