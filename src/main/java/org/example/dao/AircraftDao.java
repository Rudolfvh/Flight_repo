package org.example.dao;

import org.example.entity.Aircraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AircraftDao implements Dao<Long, Aircraft>{

    private static final AircraftDao INSTANCE = new AircraftDao();
    private static String SAVE_SQL = """
            INSERT INTO aircraft (model)
            values (?)
            """;

    private static String DELETE_SQL = """
            DELETE FROM aircraft a
            WHERE a.id = ? 
            """;

    private static final String FIND_ALL = """
            SELECT a.id,
            a.model
            FROM aircraft a
            """;

    private static String UPDATE_SQL = """
            UPDATE aircraft SET
            model = ?
            WHERE id = ?;
            """;

    private static String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?;
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
    public Optional<Aircraft> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID)) {
            Aircraft aircraft = null;
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next())
                aircraft = buildAircraft(result);
            return Optional.ofNullable(aircraft);
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public boolean update(Aircraft aircraft) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1,aircraft.getModel());
            statement.setLong(2,aircraft.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public List<Aircraft> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<Aircraft> aircrafts = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                aircrafts.add(buildAircraft(result));

            return aircrafts;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    private Aircraft buildAircraft(ResultSet result) throws SQLException {
        return new Aircraft(result.getLong("id"),
                result.getString("model")
        );
    }

    @Override
    public Aircraft save(Aircraft aircraft) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,aircraft.getModel());

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                aircraft.setId(generatedKeys.getLong("id"));

            return aircraft;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public static AircraftDao getInstance(){
        return INSTANCE;
    }

    private AircraftDao(){
    }
}
