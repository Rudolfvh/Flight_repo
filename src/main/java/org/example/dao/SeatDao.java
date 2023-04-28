package org.example.dao;

import org.example.entity.Aircraft;
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

    @Override
    public boolean delete(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(User.class,id));
            session.beginTransaction().commit();
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
    @Override
    public Optional<Seat> findById(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            Seat seat = null;
            seat = session.get(Seat.class, id);
            return Optional.ofNullable(seat);
        }
    }

    @Override
    public List<Seat> findAll() {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            List<Seat> seats = (List<Seat>) session.createQuery("From Seat ").list();
            return seats;
        }
    }

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
