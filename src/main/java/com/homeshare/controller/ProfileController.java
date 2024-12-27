package com.homeshare.controller;

import com.homeshare.domain.User;
import com.homeshare.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";
        }

        User user = (User) authentication.getPrincipal();

        model.addAttribute("user", user);
        return "edit-profile";  // Thymeleaf template
    }

    @PostMapping("/profile/update")
    public String updateProfile(User formUser) {
        User user = userService.findByEmail(formUser.getEmail());
        if (user == null) {
            // or throw an exception; handle how you see fit
            return "redirect:/login";
        }

        user.setFirstName(formUser.getFirstName());
        user.setLastName(formUser.getLastName());
        userService.updateUser(user);

        return "redirect:/home";
    }
}
