package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.service.CartService;
import jakarta.servlet.http.HttpSession;
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
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            HttpSession session
    ) {
        cartService.addToCart(itemId, quantity, session);
        return "redirect:/cart";
    }

    @GetMapping("")
    public String cartPage(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getCartItems(session));
        model.addAttribute("total", cartService.getCartTotal(session));
        return "cart/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.removeItem(itemId, session);
        return "redirect:/cart";
    }

    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.increaseQuantity(itemId, session);
        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.decreaseQuantity(itemId, session);
        return "redirect:/cart";
    }
}