package org.example.dao;

import org.example.dto.TicketFilter;
import org.example.entity.Flight;
import org.example.entity.FlightStatus;
import org.example.entity.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Long,Ticket>{
    private static final TicketDao INSTANCE = new TicketDao();

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

    public boolean update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, ticket.getPassport_no());
            statement.setString(2, ticket.getPassenger_name());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeat_no());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID)) {
            Ticket ticket = null;
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next())
                ticket = buildTicket(result);
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    private Ticket buildTicket(ResultSet result) throws SQLException {
        var flight = new Flight(
                result.getLong("flight_id"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                FlightStatus.valueOf(result.getString("status"))
        );
        return new Ticket(
                result.getLong("id"),
                result.getString("passport_no"),
                result.getString("passenger_name"),
                flight,
                result.getString("seat_no"),
                result.getBigDecimal("cost")
        );
    }

    public List<Ticket> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<Ticket> tickets = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                tickets.add(buildTicket(result));

            return tickets;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.seatNo() != null) {
            whereSql.add("seat_no like ?");
            parameters.add("%" +filter.seatNo()+"%");
        }
        if (filter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            parameters.add(filter.passengerName());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        var where = whereSql.stream().collect(Collectors.joining(
                " AND ",
                parameters.size() > 2 ? " WHERE " : " ",
                " LIMIT ? OFFSET ?"
        ));
        String sql = FIND_ALL + where;

        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            List<Ticket> tickets = new ArrayList<>();
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(statement);
            var result = statement.executeQuery();
            while (result.next())
                tickets.add(buildTicket(result));

            return tickets;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getPassport_no());
            statement.setString(2, ticket.getPassenger_name());
            statement.setLong(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeat_no());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                ticket.setId(generatedKeys.getLong("id"));
            return ticket;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }
}