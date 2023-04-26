package org.example.dto;

import lombok.*;

@Value
public class TicketDto {
    private Long id;
    private Long flightId;
    private String seatNo;
}
