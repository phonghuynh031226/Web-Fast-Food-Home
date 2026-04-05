package com.poly.controller.asm_java5.controller;

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

//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private CartService cartService;
//
//    // ===================== GET =====================
//    @GetMapping
//    public String checkoutForm(HttpSession session, Model model) {
//
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        if (cartService.getCart(session).isEmpty()) {
//            return "redirect:/cart";
//        }
//
//        model.addAttribute("cart", cartService.getCart(session));
//        model.addAttribute("total", cartService.getTotalAmount(session));
//
//        return "checkout/checkout";
//    }
//
//    // ===================== POST =====================
//    @PostMapping
//    public String checkout(
//            HttpSession session,
//            @RequestParam("customerName") String customerName,
//            @RequestParam("phone") String phone,
//            @RequestParam("address") String address
//    ) {
//
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login";
//        }
//
//        // 🔥 tạo đơn hàng (KHÔNG dùng User trong OrderCustomer)
//        orderService.createOrderFromCart(
//                cartService.getCart(session),
//                customerName,
//                phone,
//                address
//        );
//
//        // 🔥 clear cart
//        cartService.clear(session);
//
//        return "redirect:/home";
//    }
}
