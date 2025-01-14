package com.homeshare.controller;

import com.homeshare.domain.Booking;
import com.homeshare.domain.Property;
import com.homeshare.domain.User;
import com.homeshare.service.BookingService;
import com.homeshare.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final BookingService bookingService;

    // 1. Display the search form.
    @GetMapping("/search")
    public String showSearchForm() {
        return "search-properties";  // This renders search-properties.html
    }

    // 2. Process the search form submission.
    @GetMapping("/results")
    public String searchProperties(
            @RequestParam String state,
            @RequestParam String city,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer numberOfRooms,
            Model model) {

        List<Property> properties = propertyService.searchProperties(state, city, startDate, endDate, numberOfRooms);
        model.addAttribute("properties", properties);
        return "property-search-results"; // This renders property-search-results.html
    }

    @GetMapping("/{propertyId}/book")
    public String showBookingForm(@PathVariable Long propertyId, Model model) {
        model.addAttribute("propertyId", propertyId);
        return "book-property";  // Renders book-property.html
    }

    @PostMapping("/{propertyId}/book")
    public String bookProperty(@PathVariable Long propertyId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               @RequestParam String paymentInfo,   // Added parameter
                               Model model) {

        // Ensure the user is logged in.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        User guest = (User) auth.getPrincipal();

        // Call createBooking with the paymentInfo.
        Booking booking = bookingService.createBooking(propertyId, guest, startDate, endDate, paymentInfo);
        model.addAttribute("booking", booking);
        return "booking-confirmation";  // renders booking-confirmation.html
    }


    // For host approvals (this could be part of a separate BookingController as well).
    @PostMapping("/bookings/{bookingId}/approve")
    public String approveBooking(@PathVariable Long bookingId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }
        User host = (User) auth.getPrincipal();

        Booking booking = bookingService.approveBooking(bookingId, host);
        model.addAttribute("booking", booking);
        return "booking-approval-confirmation";  // a page confirming the approval
    }
}
