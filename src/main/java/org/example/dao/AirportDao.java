package org.example.dao;

import org.example.entity.Airport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportDao implements Dao<String, Airport>{

    private static final AirportDao INSTANCE = new AirportDao();
    private static String SAVE_SQL = """
            INSERT INTO airport (code,country,city)
            values (?,?,?)
            """;

    private static String DELETE_SQL = """
            DELETE FROM airport
            WHERE code = ? 
            """;

    private static String FIND_ALL = """
            SELECT a.code,
            a.country,
            a.city 
            FROM airport a
            """;

    private static String UPDATE_SQL = """
            UPDATE airport SET
            country = ?,
            city = ?
            WHERE code = ?;
            """;

    private static String FIND_BY_ID = FIND_ALL + """
            WHERE code = ?;
            """;
    @Override
    public boolean delete(String id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public Optional<Airport> findById(String id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID)) {
            Airport airport = null;
            statement.setString(1, id);
            var result = statement.executeQuery();
            if (result.next())
                airport = buildAirport(result);
            return Optional.ofNullable(airport);
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public boolean update(Airport airport) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1,airport.getCountry());
            statement.setString(2,airport.getCity());
            statement.setString(3,airport.getCode());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public List<Airport> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<Airport> airports = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                airports.add(buildAirport(result));

            return airports;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    private Airport buildAirport(ResultSet result) throws SQLException {
        return new Airport(result.getString("code"),
                result.getString("country"),
                result.getString("city")
        );
    }

    @Override
    public Airport save(Airport airport) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,airport.getCode());
            statement.setString(2,airport.getCountry());
            statement.setString(3,airport.getCity());

            statement.executeUpdate();

            return airport;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public static AirportDao getInstance(){
        return INSTANCE;
    }

    private AirportDao(){
    }
}
