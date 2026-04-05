package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addToCart(
            @RequestParam("itemId") Integer itemId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity
    ) {
        cartService.addToCart(itemId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("")
    public String cartPage(Model model) {
        model.addAttribute("cart", cartService.getCartItems());
        model.addAttribute("total", cartService.getCartTotal());
        return "cart/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable("id") Integer itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable("id") Integer itemId) {
        cartService.increaseQuantity(itemId);
        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable("id") Integer itemId) {
        cartService.decreaseQuantity(itemId);
        return "redirect:/cart";
    }
}