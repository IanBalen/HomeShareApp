package com.homeshare.service;

import com.homeshare.domain.Booking;
import com.homeshare.domain.Property;
import com.homeshare.domain.User;
import com.homeshare.repository.BookingRepository;
import com.homeshare.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    // Existing createBooking and approveBooking methods remain here.
    public Booking createBooking(Long propertyId, User guest,
                                 LocalDate startDate, LocalDate endDate,
                                 String paymentInfo) {

        if (paymentInfo == null || !paymentInfo.toLowerCase().contains("valid")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment information");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        List<Booking> overlaps = bookingRepository
                .findByPropertyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(property, endDate, startDate);
        if (!overlaps.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The property is already booked for that date range");
        }

        Booking booking = Booking.builder()
                .property(property)
                .guest(guest)
                .host(property.getHost())
                .startDate(startDate)
                .endDate(endDate)
                .status("PENDING")
                .build();

        return bookingRepository.save(booking);
    }

    public Booking approveBooking(Long bookingId, User host) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        // Ensure that the logged-in user is the host of this booking.
        if (!booking.getHost().getEmail().equals(host.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to approve this booking");
        }

        booking.setStatus("CONFIRMED");

        // Award points (example: 10 points each)
        userService.awardPoints(booking.getGuest(), 10);
        userService.awardPoints(booking.getHost(), 10);

        return bookingRepository.save(booking);
    }

    // Retrieve bookings where you are the host and need to approve.
    public List<Booking> getPendingBookingsForHost(User host) {
        return bookingRepository.findAll().stream()
                .filter(b -> "PENDING".equals(b.getStatus())
                        && b.getHost().getEmail().equals(host.getEmail()))
                .collect(Collectors.toList());
    }

    // Retrieve bookings where you are the guest and still waiting for approval.
    public List<Booking> getPendingBookingsForGuest(User guest) {
        return bookingRepository.findAll().stream()
                .filter(b -> "PENDING".equals(b.getStatus())
                        && b.getGuest().getEmail().equals(guest.getEmail()))
                .collect(Collectors.toList());
    }

    // Retrieve confirmed bookings where you are involved (either as guest or host).
    public List<Booking> getConfirmedBookingsForUser(User user) {
        return bookingRepository.findAll().stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()) &&
                        (b.getGuest().getEmail().equals(user.getEmail()) ||
                                b.getHost().getEmail().equals(user.getEmail())))
                .collect(Collectors.toList());
    }
}
