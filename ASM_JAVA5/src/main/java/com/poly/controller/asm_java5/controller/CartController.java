package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.User;
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

    private Integer getUserId(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("user");
        }
        return user != null ? user.getUserId() : null;
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam("itemId") Integer itemId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            HttpSession session
    ) {
        cartService.addToCart(getUserId(session), itemId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("")
    public String cartPage(Model model, HttpSession session) {
        Integer userId = getUserId(session);
        model.addAttribute("cart", cartService.getCartItems(userId));
        model.addAttribute("total", cartService.getCartTotal(userId));
        return "cart/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.removeItem(getUserId(session), itemId);
        return "redirect:/cart";
    }

    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.increaseQuantity(getUserId(session), itemId);
        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable("id") Integer itemId, HttpSession session) {
        cartService.decreaseQuantity(getUserId(session), itemId);
        return "redirect:/cart";
    }
}
