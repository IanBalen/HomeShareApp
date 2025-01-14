package com.homeshare.config;

import com.homeshare.domain.Property;
import com.homeshare.domain.User;
import com.homeshare.domain.enums.Role;
import com.homeshare.repository.PropertyRepository;
import com.homeshare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create a sample host user if not already present.
        User host = userRepository.findByEmail("host@example.com")
                .orElseGet(() -> {
                    User newHost = User.builder()
                            .firstName("Host")
                            .lastName("User")
                            .email("host@example.com")
                            .password(passwordEncoder.encode("password"))
                            .createdAt(java.time.LocalDateTime.now())
                            .role(Role.ROLE_USER)
                            .points(0)  // assuming you have this field
                            .build();
                    return userRepository.save(newHost);
                });

        // Create a few sample properties.
        if (propertyRepository.count() == 0) {
            Property p1 = Property.builder()
                    .state("California")
                    .city("Los Angeles")
                    .address("123 Sunset Blvd")
                    .floor(2)
                    .numberOfRooms(3)
                    .uniqueFeatures("Pool, Wifi")
                    .host(host)
                    .build();

            Property p2 = Property.builder()
                    .state("New York")
                    .city("New York")
                    .address("456 Broadway")
                    .floor(5)
                    .numberOfRooms(2)
                    .uniqueFeatures("Central Park View")
                    .host(host)
                    .build();

            propertyRepository.save(p1);
            propertyRepository.save(p2);
        }
    }
}
