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
    private Long id;
    private String passport_no;
    private String passenger_name;
    @Convert(converter = Flight.class)
    @Column(name = "flight_id")
    private Flight flight;
    private String seat_no;
    private BigDecimal cost;

}
