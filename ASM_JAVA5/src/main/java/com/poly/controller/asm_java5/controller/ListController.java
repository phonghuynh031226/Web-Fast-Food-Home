package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {

    @Autowired
    private MenuService menuService;

    @GetMapping({"/product/list", "/product/search"})
    public String productList(
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        model.addAttribute("menus", menuService.findAllMenus());
        model.addAttribute("items", menuService.findItems(menuId, sort, keyword));
        model.addAttribute("selectedMenuId", menuId);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("keyword", keyword);
        return "product/list";
    }
}
