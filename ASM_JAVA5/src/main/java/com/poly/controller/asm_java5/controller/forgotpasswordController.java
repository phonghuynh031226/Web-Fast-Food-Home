package com.poly.controller.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class forgotpasswordController {

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password";
    }
}