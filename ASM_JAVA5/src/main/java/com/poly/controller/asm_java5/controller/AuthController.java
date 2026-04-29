package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping({"/login", ""})
    public String authPage() {
        return "auth/auth";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam(required = false) Boolean acceptTerms,
            Model model
    ) {
        String message = authService.register(fullName, email, password, confirmPassword, acceptTerms);

        if ("Đăng ký thành công".equals(message)) {
            model.addAttribute("success", message);
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
            @RequestParam(required = false) Boolean remember,
            HttpSession session,
            HttpServletResponse response,
            Model model
    ) {
        User user = authService.login(email, password);

        if (user == null) {
            model.addAttribute("loginError", "Sai email hoặc mật khẩu");
            return "auth/auth";
        }

        session.setAttribute("loggedInUser", user);
        session.setAttribute("user", user);

        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");

        if (Boolean.TRUE.equals(remember)) {
            int sevenDays = 7 * 24 * 60 * 60;
            session.setMaxInactiveInterval(sevenDays);
            sessionCookie.setMaxAge(sevenDays);
        } else {
            session.setMaxInactiveInterval(30 * 60);
            sessionCookie.setMaxAge(-1);
        }

        response.addCookie(sessionCookie);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie sessionCookie = new Cookie("JSESSIONID", "");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
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
