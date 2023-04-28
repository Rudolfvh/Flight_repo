package org.example.dao;

import org.example.entity.Aircraft;
import org.example.entity.Airport;
import org.example.entity.Seat;
import org.example.entity.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AirportDao implements Dao<String, Airport>{

    private static final AirportDao INSTANCE = new AirportDao();
    private static final Configuration configuration = new Configuration();
    @Override
    public boolean delete(String id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(Airport.class,id));
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public Optional<Airport> findById(String id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            Airport airport = null;
            session.beginTransaction();
            airport = session.get(Airport.class, id);
            session.beginTransaction().commit();
            return Optional.ofNullable(airport);
        }
    }

    @Override
    public boolean update(Airport airport) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(airport);
            session.beginTransaction().commit();
            return true;
        }
    }


    @Override
    public List<Airport> findAll() {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            List<Airport> airports = (List<Airport>) session.createQuery("From Airport ").list();
            return airports;
        }
    }
    @Override
    public Airport save(Airport airport) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(airport);
            session.beginTransaction().commit();
            return airport;
        }
    }

//    @Override
//    public List<Airport> findAll() {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_ALL)) {
//            List<Airport> airports = new ArrayList<>();
//            var result = statement.executeQuery();
//            while (result.next())
//                airports.add(buildAirport(result));
//
//            return airports;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    private Airport buildAirport(ResultSet result) throws SQLException {
//        return new Airport(result.getString("code"),
//                result.getString("country"),
//                result.getString("city")
//        );
//    }

    public static AirportDao getInstance(){
        return INSTANCE;
    }

    private AirportDao(){
    }
}
