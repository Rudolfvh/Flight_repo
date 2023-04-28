package org.example.dao;

import org.example.entity.Aircraft;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class AircraftDao implements Dao<Long, Aircraft>{

    private static final AircraftDao INSTANCE = new AircraftDao();
    private static final Configuration configuration = new Configuration();
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
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(session.get(Aircraft.class,id));
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean update(Aircraft aircraft) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(aircraft);
            session.beginTransaction().commit();
            return true;
        }
    }

    @Override
    public Aircraft save(Aircraft aircraft) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(aircraft);
            session.beginTransaction().commit();
            return aircraft;
        }
    }
    @Override
    public Optional<Aircraft> findById(Long id) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            Aircraft aircraft = null;
            session.beginTransaction();
            aircraft = session.get(Aircraft.class, id);
            session.beginTransaction().commit();
            return Optional.ofNullable(aircraft);
        }
    }
//    @Override
//    public List<Aircraft> findAll() {
//        try (var connection = ConnectionManager.get();
//             var statement = connection.prepareStatement(FIND_ALL)) {
//            List<Aircraft> aircrafts = new ArrayList<>();
//            var result = statement.executeQuery();
//            while (result.next())
//                aircrafts.add(buildAircraft(result));
//
//            return aircrafts;
//        } catch (SQLException e) {
//            throw new DaoExeption(e);
//        }
//    }

//    private Aircraft buildAircraft(ResultSet result) throws SQLException {
//        return new Aircraft(result.getLong("id"),
//                result.getString("model")
//        );
//    }

    public static AircraftDao getInstance(){
        return INSTANCE;
    }

    private AircraftDao(){
    }
}
