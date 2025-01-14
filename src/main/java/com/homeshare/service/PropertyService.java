package com.homeshare.service;

import com.homeshare.domain.Property;
import com.homeshare.repository.PropertyRepository;
import com.homeshare.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final BookingRepository bookingRepository;


    public List<Property> searchProperties(String state, String city, LocalDate startDate,
                                             LocalDate endDate, Integer numberOfRooms) {
        // First, filter by city and state.
        List<Property> properties = propertyRepository.findByCityAndState(city, state);

        // Filter by number of rooms if required.
        if (numberOfRooms != null) {
            properties = properties.stream()
                    .filter(p -> p.getNumberOfRooms() >= numberOfRooms)
                    .collect(Collectors.toList());
        }

        // Remove properties with conflicting bookings.
        properties = properties.stream()
                .filter(property -> bookingRepository
                        .findByPropertyAndStartDateLessThanEqualAndEndDateGreaterThanEqual(property, endDate, startDate)
                        .isEmpty())
                .collect(Collectors.toList());
        return properties;
    }
}
