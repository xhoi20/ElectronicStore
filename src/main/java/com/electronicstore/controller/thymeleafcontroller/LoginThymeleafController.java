package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginThymeleafController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute AuthRequest authRequest, Model model) {
        try {
            AuthResponse authResponse = userService.loginUser(authRequest);
            model.addAttribute("token", authResponse.getToken());
            model.addAttribute("email", authResponse.getEmail());
            model.addAttribute("role", authResponse.getRole());
            return "login-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}