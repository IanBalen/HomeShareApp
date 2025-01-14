package com.homeshare.controller;

import com.homeshare.domain.Booking;
import com.homeshare.domain.User;
import com.homeshare.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookingService bookingService;

    @GetMapping("/home")
    public String homePage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());

        if (isLoggedIn) {
            if (auth.getPrincipal() instanceof User customUser) {
                model.addAttribute("email", customUser.getEmail());
                model.addAttribute("name", customUser.getFirstName());

                // Bookings where you are the host and need to approve.
                List<Booking> pendingForApproval = bookingService.getPendingBookingsForHost(customUser);
                model.addAttribute("pendingForApproval", pendingForApproval);

                // Bookings where you are the guest and waiting for approval.
                List<Booking> pendingForMe = bookingService.getPendingBookingsForGuest(customUser);
                model.addAttribute("pendingForMe", pendingForMe);

                // Confirmed bookings for this user.
                List<Booking> confirmedBookings = bookingService.getConfirmedBookingsForUser(customUser);
                model.addAttribute("confirmedBookings", confirmedBookings);
            }
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "home";  // renders home.html
    }
}
