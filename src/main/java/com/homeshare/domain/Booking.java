package com.homeshare.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Property property;

    @ManyToOne
    private User guest;
    
    // The host is usually derived from the property’s owner.
    @ManyToOne
    private User host;

    private LocalDate startDate;
    private LocalDate endDate;

    // Status values can be "PENDING", "CONFIRMED", "CANCELLED"
    private String status;
}
