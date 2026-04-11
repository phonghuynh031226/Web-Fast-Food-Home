package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.service.AuthService;
import com.poly.controller.asm_java5.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @GetMapping("/login")
    public String authPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }
        return "auth/auth";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        String message = authService.register(fullName, email, password, confirmPassword);

        if ("Đăng ký thành công".equals(message)) {
            model.addAttribute("success", message);
            model.addAttribute("showRegister", false);
        } else {
            model.addAttribute("registerError", message);
            model.addAttribute("showRegister", true);
        }

        return "auth/auth";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }

        User user = authService.login(email, password);

        if (user == null) {
            model.addAttribute("loginError", "Sai email hoặc mật khẩu");
            return "auth/auth";
        }

        session.setAttribute("user", user);

        // Merge giỏ tạm trong session vào database sau khi đăng nhập
        cartService.mergeSessionCartToDatabase(session, user);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password/send-otp")
    @ResponseBody
    public String sendOtp(@RequestParam String email) {
        return authService.sendOtpToEmail(email);
    }

    @PostMapping("/forgot-password/reset")
    @ResponseBody
    public String resetPasswordByOtp(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        return authService.resetPasswordByOtp(email, otp, newPassword, confirmPassword);
    }
}