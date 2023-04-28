package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flight", schema = "public")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "flight_no")
    private  String flightNo;
    @Column(name = "departure_date")
    private LocalDateTime departureDate;
    @Column(name = "departure_airport_code")
    private String departureAirportCode;
    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;
    @Column(name = "arrival_airport_code")
    private String arrivalAirportCode;
    @Column(name = "aircraft_id")
    private  Integer aircraftId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FlightStatus status;

}
