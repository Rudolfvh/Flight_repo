package org.example.dao;

import java.sql.SQLException;

public class DaoExeption extends RuntimeException {
    public DaoExeption(SQLException e) {
        super(e);
    }
}
