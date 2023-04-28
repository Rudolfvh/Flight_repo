package org.example.service;

import org.example.dao.TicketDao;
import org.example.dto.TicketDto;

import java.util.List;
import java.util.stream.Collectors;

public class TicketService {
    private final static TicketService INSTANCE = new TicketService();
    private final TicketDao ticketDao = TicketDao.getInstance();

//    public List<TicketDto> findAllByFlightId(Long flightId) {
//        return ticketDao.findAllByFlightId(flightId).stream().map(
//                e -> new TicketDto(
//                        e.getId(),
//                        e.getFlight().getId(),
//                        e.getSeatNo()
//                )
//        ).collect(Collectors.toList());
//    }

    public static TicketService getInstance() {
        return INSTANCE;
    }

    private TicketService() {
    }
}
