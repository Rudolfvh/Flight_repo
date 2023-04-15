package org.example;

import org.example.dao.AircraftDao;
import org.example.dao.FlightDao;
import org.example.entity.Aircraft;

import java.math.BigDecimal;
import java.sql.*;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        var aircraftDao = AircraftDao.getInstance();
    }
}
