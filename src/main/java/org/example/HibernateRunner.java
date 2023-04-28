package org.example;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        UserDao userDao = UserDao.getInstance();
        User user = User.builder().name("user").build();
       userDao.save(user);
    }
}
