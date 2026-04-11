package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        // chưa login → đá về login
        if (user == null) {
            return "redirect:/auth/login";
        }

        // lấy đơn hàng theo user
        List<Order> orders = orderRepository.findByUser_UserId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);

        return "user/profile";
    }
}