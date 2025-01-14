package com.homeshare.domain;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;
    private String city;
    private String address;
    private int floor;
    private int numberOfRooms;
    private String uniqueFeatures;

    // For linking the property with the host (the owner of the property)
    @ManyToOne
    private User host;
}
