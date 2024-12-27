package com.homeshare.controller;

import com.homeshare.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.homeshare.util.Helper.log;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());

        if (isLoggedIn) {
            if (auth.getPrincipal() instanceof User customUser) {
                log("User is logged in: " + customUser.getEmail());
                model.addAttribute("email", customUser.getEmail());
                model.addAttribute("name", customUser.getFirstName());
            }
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "home";  // home.html
    }
}
