package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

//    @Autowired
//    private AuthService authService;
//
//    // ===== LOGIN =====
//    @GetMapping("/login")
//    public String loginForm() {
//        return "auth/auth";
//    }
//
//    @PostMapping("/login")
//    public String login(
//            @RequestParam String emailOrPhone,
//            @RequestParam String password,
//            HttpSession session,
//            Model model
//    ) {
//        User user = authService.login(emailOrPhone, password);
//
//        if (user != null) {
//            session.setAttribute("user", user);
//            return "redirect:/home";
//        }
//
//        model.addAttribute("loginError", "Sai tài khoản hoặc mật khẩu");
//        return "auth/auth";
//    }
//
//    // ===== REGISTER =====
//    @PostMapping("/register")
//    public String register(
//            @RequestParam("fullName") String fullName, // 👈 PHẢI ĐÚNG TÊN
//            @RequestParam String email,
//            @RequestParam String phone,
//            @RequestParam String password,
//            Model model
//    ) {
//        try {
//            authService.register(fullName, email, phone, password);
//            return "redirect:/auth/login";
//        } catch (RuntimeException e) {
//            model.addAttribute("registerError", e.getMessage());
//            return "auth/auth";
//        }
//    }
//
//    // ===== LOGOUT =====
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/auth/login";
//    }
}
