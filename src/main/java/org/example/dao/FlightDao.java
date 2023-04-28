package org.example.dao;

import org.example.entity.Flight;
import org.example.entity.FlightStatus;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Long, Flight>{
    private static final FlightDao INSTANCE = new FlightDao();
    private static final Configuration configuration = new Configuration();
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
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(Flight.class,id));
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean update(Flight flight) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(flight);
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public Flight save(Flight flight) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(flight);
            session.beginTransaction().commit();

            return flight;
        }
    }

//    @Override
//    public Optional<Flight> findById(Long id) {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_BY_ID)) {
//            Flight flight = null;
//            statement.setLong(1, id);
//            var result = statement.executeQuery();
//            if (result.next())
//                flight = buildFlight(result);
//            return Optional.ofNullable(flight);
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    @Override
//    public List<Flight> findAll() {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_ALL)) {
//            List<Flight> flights = new ArrayList<>();
//            var result = statement.executeQuery();
//            while (result.next())
//                flights.add(buildFlight(result));
//
//            return flights;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }
//
//    private Flight buildFlight(ResultSet result) throws SQLException {
//        return new Flight(
//                result.getLong("id"),
//                result.getString("flight_no"),
//                result.getTimestamp("departure_date").toLocalDateTime(),
//                result.getString("departure_airport_code"),
//                result.getTimestamp("arrival_date").toLocalDateTime(),
//                result.getString("arrival_airport_code"),
//                result.getInt("aircraft_id"),
//                FlightStatus.valueOf(result.getString("status"))
//        );
//    }

    public static FlightDao getInstance(){
        return INSTANCE;
    }

    private FlightDao(){
    }
}
