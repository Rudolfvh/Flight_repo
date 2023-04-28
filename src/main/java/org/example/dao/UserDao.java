package org.example.dao;

import org.example.entity.Gender;
import org.example.entity.Role;
import org.example.entity.User;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final Configuration configuration = new Configuration();

//    @SneakyThrows
//    public Optional<User> findByEmailAndPassword(String email, String password) {
//        try (var connection = ConnectionManager.get();
//             var preparedStatement = connection.prepareStatement(GET_BY_EMAIL_AND_PASSWORD_SQL)) {
//            preparedStatement.setString(1, email);
//            preparedStatement.setString(2, password);
//
//            var resultSet = preparedStatement.executeQuery();
//            User user = null;
//            if (resultSet.next()) {
//                user = buildEntity(resultSet);
//            }
//            return Optional.ofNullable(user);
//        }
//    }

    @Override
    @SneakyThrows
    public User save(User entity) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();

            return entity;
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}