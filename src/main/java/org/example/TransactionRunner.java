package org.example;

import by.itacademy.jdbc.starter.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        Long flightId = 7l;
        String deleteFlightSQL = "delete from flight where id = " + flightId;
        String deleteTicketSQL = "delete from ticket where flight_id = " + flightId;

        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionManager.get();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.addBatch(deleteTicketSQL);
            statement.addBatch(deleteFlightSQL);


            statement.executeBatch();

            connection.commit();

        } catch (Exception e) {
            if (connection != null)
                connection.rollback();
            throw e;
        } finally {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
        }
    }
}
