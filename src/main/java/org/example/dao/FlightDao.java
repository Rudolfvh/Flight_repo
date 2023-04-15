package org.example.dao;

import org.example.ConnectionManager;
import org.example.entity.Flight;
import org.example.entity.FlightStatus;
import org.example.entity.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight>{
    private static final FlightDao INSTANCE = new FlightDao();

    private static String SAVE_SQL = """
            INSERT INTO flight (flight_no,departure_date,departure_airport_code,
            arrival_date,arrival_airport_code,aircraft_id,status)
            values (?,?,?,?,?,?,?)
            """;

    private static String DELETE_SQL = """
            DELETE FROM flight
            WHERE id = ?
            """;

    private static String FIND_ALL = """
            SELECT f.id,
            f.flight_no, 
            f.departure_date, 
            f.departure_airport_code, 
            f.arrival_date, 
            f.arrival_airport_code, 
            f.aircraft_id, 
            f.status            
            FROM flight f
            """;

    private static String UPDATE_SQL = """
            UPDATE flight SET
            flight_no = ?,
            departure_date = ?,
            departure_airport_code = ?,
            arrival_date = ?,
            arrival_airport_code = ?,
            aircraft_id = ?,
            status = ?
            WHERE id = ?
            """;

    private static String FIND_BY_ID = FIND_ALL + """
            WHERE f.id = ?
            """;

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public Optional<Flight> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID)) {
            Flight flight = null;
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next())
                flight = buildFlight(result);
            return Optional.ofNullable(flight);
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public boolean update(Flight flight) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, flight.getFlightNo());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            statement.setString(3, flight.getDepartureAirportCode());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
            statement.setString(5, flight.getArrivalAirportCode());
            statement.setLong(6, flight.getAircraftId());
            statement.setObject(7,flight.getStatus());
            statement.setLong(8,flight.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }


    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<Flight> flights = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                flights.add(buildFlight(result));

            return flights;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    private Flight buildFlight(ResultSet result) throws SQLException {
        return new Flight(
                result.getLong("id"),
                result.getString("flight_no"),
                result.getTimestamp("departure_date").toLocalDateTime(),
                result.getString("departure_airport_code"),
                result.getTimestamp("arrival_date").toLocalDateTime(),
                result.getString("arrival_airport_code"),
                result.getInt("aircraft_id"),
                FlightStatus.valueOf(result.getString("status"))
        );
    }

    @Override
    public Flight save(Flight flight) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, flight.getFlightNo());
            statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
            statement.setString(3, flight.getDepartureAirportCode());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
            statement.setString(5, flight.getArrivalAirportCode());
            statement.setLong(6, flight.getAircraftId());
            statement.setObject(7,flight.getStatus());

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                flight.setId(generatedKeys.getLong("id"));
            return flight;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public static FlightDao getInstance(){
        return INSTANCE;
    }

    private FlightDao(){
    }
}
