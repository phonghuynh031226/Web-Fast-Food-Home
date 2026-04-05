package com.poly.controller.asm_java5.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Menus")
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @OneToMany(mappedBy = "menu")
    private List<MenuItem> menuItems;
}