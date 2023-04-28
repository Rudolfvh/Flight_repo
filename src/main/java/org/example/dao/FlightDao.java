package org.example.dao;

import org.example.entity.*;
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

    @Override
    public Optional<Flight> findById(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            Flight flight = null;
            session.beginTransaction();
            flight = session.get(Flight.class, id);
            session.beginTransaction().commit();
            return Optional.ofNullable(flight);
        }
    }

    @Override
    public List<Flight> findAll() {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            List<Flight> flights = (List<Flight>) session.createQuery("From Flight ").list();
            return flights;
        }
    }

    public static FlightDao getInstance(){
        return INSTANCE;
    }

    private FlightDao(){
    }
}
