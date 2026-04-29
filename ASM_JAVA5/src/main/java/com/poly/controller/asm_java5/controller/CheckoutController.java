package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.service.CartService;
import com.poly.controller.asm_java5.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    private User getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            user = (User) session.getAttribute("user");
        }
        return user;
    }

    @GetMapping
    public String checkoutForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = getSessionUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginError", "Vui lòng đăng nhập để thanh toán");
            return "redirect:/auth/login";
        }

        if (cartService.getCartItems(user.getUserId()).isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("user", user);
        model.addAttribute("cart", cartService.getCartItems(user.getUserId()));
        model.addAttribute("total", cartService.getCartTotal(user.getUserId()));
        return "checkout/checkout";
    }

    @PostMapping
    public String checkout(
            HttpSession session,
            @RequestParam("customerName") String customerName,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "paymentMethod", defaultValue = "CASH") String paymentMethod,
            RedirectAttributes redirectAttributes
    ) {
        User user = getSessionUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginError", "Vui lòng đăng nhập để thanh toán");
            return "redirect:/auth/login";
        }

        try {
            orderService.createOrderFromCart(user.getUserId(), customerName, phone, address, note, paymentMethod);
            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công");
            return "redirect:/";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("checkoutError", ex.getMessage());
            return "redirect:/checkout";
        }
    }
}
