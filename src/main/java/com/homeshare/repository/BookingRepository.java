package com.homeshare.repository;

import com.homeshare.domain.Booking;
import com.homeshare.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Returns any bookings that overlap the given date range
    List<Booking> findByPropertyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Property property, LocalDate endDate, LocalDate startDate);
}
