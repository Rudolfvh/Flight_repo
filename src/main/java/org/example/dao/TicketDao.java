package org.example.dao;

import org.example.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Long,Ticket>{
    private static final TicketDao INSTANCE = new TicketDao();
    private static final Configuration configuration = new Configuration();

    @Override
    public boolean update(Ticket ticket) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(ticket);
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean delete(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(Ticket.class,id));
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public Ticket save(Ticket ticket) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(ticket);
            session.beginTransaction().commit();
            return ticket;
        }
    }

    public Optional<Ticket> findById(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                Ticket ticket = null;
                session.beginTransaction();
                session.get(Ticket.class, id);
                session.beginTransaction().commit();
                return Optional.ofNullable(ticket);
            }
        }
    }
    public List<Ticket> findAll() {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            List<Ticket> tickets = (List<Ticket>) session.createQuery("From Ticket ").list();
            return tickets;
        }
    }

//    public List<Ticket> findAll(TicketFilter filter) {
//        List<Object> parameters = new ArrayList<>();
//        List<String> whereSql = new ArrayList<>();
//        if (filter.seatNo() != null) {
//            whereSql.add("seat_no like ?");
//            parameters.add("%" +filter.seatNo()+"%");
//        }
//        if (filter.passengerName() != null) {
//            whereSql.add("passenger_name = ?");
//            parameters.add(filter.passengerName());
//        }
//        parameters.add(filter.limit());
//        parameters.add(filter.offset());
//
//        var where = whereSql.stream().collect(Collectors.joining(
//                " AND ",
//                parameters.size() > 2 ? " WHERE " : " ",
//                " LIMIT ? OFFSET ?"
//        ));
//        String sql = FIND_ALL + where;
//
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(sql)) {
//            List<Ticket> tickets = new ArrayList<>();
//            for (int i = 0; i < parameters.size(); i++) {
//                statement.setObject(i + 1, parameters.get(i));
//            }
//            System.out.println(statement);
//            var result = statement.executeQuery();
//            while (result.next())
//                tickets.add(buildTicket(result));
//
//            return tickets;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }
}