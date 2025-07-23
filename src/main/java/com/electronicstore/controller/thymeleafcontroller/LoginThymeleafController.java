package com.electronicstore.controller.thymeleafcontroller;

import com.electronicstore.dto.AuthRequest;
import com.electronicstore.dto.AuthResponse;
import com.electronicstore.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public String processLogin(@ModelAttribute AuthRequest authRequest, Model model, HttpServletResponse response) {
        try {
            Cookie oldCookie = new Cookie("jwtToken", null);
            oldCookie.setPath("/");
            oldCookie.setHttpOnly(true);
            oldCookie.setMaxAge(0); // Fshin cookie-n menjëherë
            response.addCookie(oldCookie);
            AuthResponse authResponse = userService.loginUser(authRequest);
            Cookie tokenCookie = new Cookie("jwtToken", authResponse.getToken());
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            //tokenCookie.setMaxAge(0);
            response.addCookie(tokenCookie);
            model.addAttribute("token", authResponse.getToken());
            model.addAttribute("email", authResponse.getEmail());
            model.addAttribute("role", authResponse.getRole());
            return "main-list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login?logout=true";
    }
}