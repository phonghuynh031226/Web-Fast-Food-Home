package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bestSellers", menuService.findBestSellers());
        model.addAttribute("otherProducts", menuService.findOtherProducts());
        return "home/index";
    }
}