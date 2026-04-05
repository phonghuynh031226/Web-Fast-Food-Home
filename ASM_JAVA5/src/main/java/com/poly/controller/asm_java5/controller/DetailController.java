package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        MenuItem item = menuService.findById(id);

        if (item == null) {
            return "redirect:/product/list";
        }

        model.addAttribute("item", item);

        model.addAttribute("suggestedItems", menuService.findSuggestedItems(id));

        return "product/detail";
    }
}