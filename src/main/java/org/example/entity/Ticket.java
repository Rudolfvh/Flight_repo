package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket", schema = "public")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passport_no")
    private String passport_no;
    @Column(name = "passenger_name")
    private String passenger_name;
    @Convert(converter = Flight.class)
    @Column(name = "flight_id")
    private Flight flight;
    @Column(name = "seat_no")
    private String seat_no;
    @Column(name = "cost")
    private BigDecimal cost;

}
