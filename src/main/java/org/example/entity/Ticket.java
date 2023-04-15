package org.example.entity;

import java.math.BigDecimal;

public class Ticket {
    private Long id;
    private String passport_no;
    private String passenger_name;
    private Flight flight;
    private String seat_no;
    private BigDecimal cost;

    public Ticket() {

    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", passport_no='" + passport_no + '\'' +
                ", passenger_name='" + passenger_name + '\'' +
                ", flight_id=" + flight +
                ", seat_no='" + seat_no + '\'' +
                ", cost=" + cost +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassport_no() {
        return passport_no;
    }

    public void setPassport_no(String passport_no) {
        this.passport_no = passport_no;
    }

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Ticket(Long id, String passport_no, String passenger_name, Flight flight_id, String seat_no, BigDecimal cost) {
        this.id = id;
        this.passport_no = passport_no;
        this.passenger_name = passenger_name;
        this.flight = flight_id;
        this.seat_no = seat_no;
        this.cost = cost;
    }
}
