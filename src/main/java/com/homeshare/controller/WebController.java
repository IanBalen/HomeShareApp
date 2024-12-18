package com.homeshare.controller;

import com.homeshare.domain.request.CreateUserRequest;
import com.homeshare.domain.request.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // corresponds to login.html in templates directory
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "register"; // corresponds to register.html in templates directory
    }
}
