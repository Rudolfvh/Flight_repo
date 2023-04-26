package org.example;


import org.hibernate.cfg.Configuration;
import java.sql.*;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.configure();
    }
}
