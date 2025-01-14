package com.homeshare.repository;

import com.homeshare.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    // A basic method to find properties by city and state.
    List<Property> findByCityAndState(String city, String state);
}
