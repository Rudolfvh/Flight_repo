package org.example.dao;

import org.example.entity.Seat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatDao implements Dao<Long, Seat>{

    private static final SeatDao INSTANCE = new SeatDao();
    private static String SAVE_SQL = """
            INSERT INTO seat (seat_no)
            values (?)
            """;

    private static String DELETE_SQL = """
            DELETE FROM seat
            WHERE aircraft_id = ? and seat_no = ?
            """;

    private static String FIND_ALL = """
            SELECT s.aircraft_id,
            s.seat_no 
            FROM seat s
            """;

    private static String FIND_BY_ID = FIND_ALL + """
            WHERE s.aircraft_id = ? and s.seat_no = ?
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
    public Optional<Seat> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID)) {
            Seat seat = null;
            statement.setLong(1, id);
            var result = statement.executeQuery();
            if (result.next())
                seat = buildSeat(result);
            return Optional.ofNullable(seat);
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    @Override
    public boolean update(Seat seat) {
       return false;
    }


    @Override
    public List<Seat> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL)) {
            List<Seat> seats = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                seats.add(buildSeat(result));

            return seats;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    private Seat buildSeat(ResultSet result) throws SQLException {
        return new Seat(result.getLong("aircraft_id"),
                result.getString("seat_no")
        );
    }

    @Override
    public Seat save(Seat seat) {
        try (var connection = ConnectionManager.get();
             var statement = connection
                     .prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,seat.getSeatNo());

            statement.executeUpdate();

            return seat;
        } catch (SQLException e) {
            throw new DaoExeption(e);
        }
    }

    public static SeatDao getInstance(){
        return INSTANCE;
    }

    private SeatDao(){
    }
}
