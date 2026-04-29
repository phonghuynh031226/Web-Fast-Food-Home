package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.repository.OrderRepository;
import com.poly.controller.asm_java5.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }

        User user = userRepository.findById(loggedInUser.getUserId()).orElse(loggedInUser);
        List<Order> orders = orderRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String phone,
                                @RequestParam String address,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/auth/login";
        }

        User user = userRepository.findById(loggedInUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);

        session.setAttribute("loggedInUser", user);
        session.setAttribute("user", user);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");
        return "redirect:/profile";
    }
}
