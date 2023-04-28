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

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();

    private static final Configuration configuration = new Configuration();

    private static final String SAVE_SQL =
            "INSERT INTO users (name, birthday, email, password, role, gender) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_BY_EMAIL_AND_PASSWORD_SQL =
            "SELECT * FROM users WHERE email = ? AND password = ?";


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


//    private User buildEntity(ResultSet resultSet) throws java.sql.SQLException {
//        return User.builder()
//                .id(resultSet.getObject("id", Integer.class))
//                .name(resultSet.getObject("name", String.class))
//                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
//                .email(resultSet.getObject("email", String.class))
//                .password(resultSet.getObject("password", String.class))
//                .role(Role.find(resultSet.getObject("role", String.class)).orElse(null))
//                .gender(Gender.valueOf(resultSet.getObject("gender", String.class)))
//                .build();
//    }

    @Override
    @SneakyThrows
    public User save(User entity) {
        configuration.configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.beginTransaction().commit();

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
