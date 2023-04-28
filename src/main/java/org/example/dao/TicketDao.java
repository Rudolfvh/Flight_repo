package org.example.dao;

import org.example.dto.TicketFilter;
import org.example.entity.Flight;
import org.example.entity.FlightStatus;
import org.example.entity.Ticket;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Long,Ticket>{
    private static final TicketDao INSTANCE = new TicketDao();
    private static final Configuration configuration = new Configuration();
    private static String SAVE_SQL = """
            INSERT INTO ticket (passport_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?, ?, ?, ?, ?)
            """;

    private static String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?
            """;

    private static String FIND_ALL = """
            SELECT t.id, 
            t.passport_no, 
            t.passenger_name, 
            t.flight_id, 
            t.seat_no, 
            t.cost,
            f.flight_no, 
            f.departure_date, 
            f.departure_airport_code, 
            f.arrival_date, 
            f.arrival_airport_code, 
            f.aircraft_id, 
            f.status            
            FROM ticket t
            LEFT JOIN flight f on f.id = t.flight_id
            """;

    private static String UPDATE_SQL = """
            UPDATE ticket SET
            passport_no = ?,
            passenger_name = ?,
            flight_id = ?,
            seat_no = ?,
            cost = ?
            WHERE id = ?
            """;

    private static String FIND_BY_ID = FIND_ALL + """
            WHERE t.id = ?
            """;

    @Override
    public boolean update(Ticket ticket) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(ticket);
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean delete(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(Ticket.class,id));
            return true;
        }
    }

    @Override
    public Ticket save(Ticket ticket) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(ticket);
            session.beginTransaction().commit();
            return ticket;
        }
    }

//    public Optional<Ticket> findById(Long id) {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_BY_ID)) {
//            Ticket ticket = null;
//            statement.setLong(1, id);
//            var result = statement.executeQuery();
//            if (result.next())
//                ticket = buildTicket(result);
//            return Optional.ofNullable(ticket);
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    private Ticket buildTicket(ResultSet result) throws SQLException {
//        var flight = new Flight(
//                result.getLong("flight_id"),
//                result.getString("flight_no"),
//                result.getTimestamp("departure_date").toLocalDateTime(),
//                result.getString("departure_airport_code"),
//                result.getTimestamp("arrival_date").toLocalDateTime(),
//                result.getString("arrival_airport_code"),
//                result.getInt("aircraft_id"),
//                FlightStatus.valueOf(result.getString("status"))
//        );
//        return new Ticket(
//                result.getLong("id"),
//                result.getString("passport_no"),
//                result.getString("passenger_name"),
//                flight,
//                result.getString("seat_no"),
//                result.getBigDecimal("cost")
//        );
//    }

//    public List<Ticket> findAll() {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_ALL)) {
//            List<Ticket> tickets = new ArrayList<>();
//            var result = statement.executeQuery();
//            while (result.next())
//                tickets.add(buildTicket(result));
//
//            return tickets;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    public List<Ticket> findAll(TicketFilter filter) {
//        List<Object> parameters = new ArrayList<>();
//        List<String> whereSql = new ArrayList<>();
//        if (filter.seatNo() != null) {
//            whereSql.add("seat_no like ?");
//            parameters.add("%" +filter.seatNo()+"%");
//        }
//        if (filter.passengerName() != null) {
//            whereSql.add("passenger_name = ?");
//            parameters.add(filter.passengerName());
//        }
//        parameters.add(filter.limit());
//        parameters.add(filter.offset());
//
//        var where = whereSql.stream().collect(Collectors.joining(
//                " AND ",
//                parameters.size() > 2 ? " WHERE " : " ",
//                " LIMIT ? OFFSET ?"
//        ));
//        String sql = FIND_ALL + where;
//
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(sql)) {
//            List<Ticket> tickets = new ArrayList<>();
//            for (int i = 0; i < parameters.size(); i++) {
//                statement.setObject(i + 1, parameters.get(i));
//            }
//            System.out.println(statement);
//            var result = statement.executeQuery();
//            while (result.next())
//                tickets.add(buildTicket(result));
//
//            return tickets;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }
}