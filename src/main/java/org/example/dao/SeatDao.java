package org.example.dao;

import org.example.entity.Seat;
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

public class SeatDao implements Dao<Long, Seat>{

    private static final SeatDao INSTANCE = new SeatDao();
    private static final Configuration configuration = new Configuration();
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
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(User.class,id));
            return true;
        }
    }

    @Override
    public Seat save(Seat seat) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(seat);
            session.beginTransaction().commit();
            return seat;
        }
    }

    @Override
    public boolean update(Seat seat) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(seat);
            session.beginTransaction().commit();
            return true;
        }
    }
//    @Override
//    public Optional<Seat> findById(Long id) {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_BY_ID)) {
//            Seat seat = null;
//            statement.setLong(1, id);
//            var result = statement.executeQuery();
//            if (result.next())
//                seat = buildSeat(result);
//            return Optional.ofNullable(seat);
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    @Override
//    public List<Seat> findAll() {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_ALL)) {
//            List<Seat> seats = new ArrayList<>();
//            var result = statement.executeQuery();
//            while (result.next())
//                seats.add(buildSeat(result));
//
//            return seats;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    private Seat buildSeat(ResultSet result) throws SQLException {
//        return new Seat(result.getLong("aircraft_id"),
//                result.getString("seat_no")
//        );
//    }

    public static SeatDao getInstance(){
        return INSTANCE;
    }

    private SeatDao(){
    }
}
