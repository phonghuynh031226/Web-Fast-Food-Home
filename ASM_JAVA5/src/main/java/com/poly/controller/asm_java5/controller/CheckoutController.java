package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.service.CartService;
import com.poly.controller.asm_java5.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }

        if (cartService.isCartEmpty(session)) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cartService.getCartItems(session));
        model.addAttribute("total", cartService.getCartTotal(session));
        model.addAttribute("user", user);

        return "checkout/checkout";
    }

    @PostMapping
    public String placeOrder(
            @RequestParam String customerName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) String note,
            @RequestParam String paymentMethod,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }

        if (cartService.isCartEmpty(session)) {
            return "redirect:/cart";
        }

        Order order = orderService.createOrder(
                session,
                customerName,
                phone,
                address,
                note,
                paymentMethod
        );

        if (order == null) {
            model.addAttribute("error", "Không thể tạo đơn hàng");
            model.addAttribute("cart", cartService.getCartItems(session));
            model.addAttribute("total", cartService.getCartTotal(session));
            model.addAttribute("user", user);
            return "checkout/checkout";
        }

        session.setAttribute("message", "Đặt hàng thành công!");
        return "redirect:/";
    }

    @GetMapping("/success")
    public String checkoutSuccess(@RequestParam Integer orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "checkout/success";
    }
}