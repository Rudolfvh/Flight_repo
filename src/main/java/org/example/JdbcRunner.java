package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class JdbcRunner {

    private static String countNameSql = """
            SELECT t.passenger_name,
            count(split_part(t.passenger_name,' ',1))
            from ticket t
            group by t.passenger_name order by count(*) desc;
            """;

    private static String countTicketSql = """
            SELECT t.passenger_name,
              count(t.passenger_name) from ticket t 
              group by t.passenger_name
              """;

    private static String updateTicketSql = """
            UPDATE ticket SET passport_no = ?,passenger_name = ?,flight_id = ?,seat_no = ?, cost = ?
            WHERE id = ?""";
    private static String updateTicketAndFlightSql = """
            begin; 
            UPDATE ticket SET cost = ? 
            WHERE flight_id = ?;
            UPDATE flight SET flight_no = ?, departure_date = ?,arrival_date = ?
            WHERE id = ?;
            commit;
            """;

    public static void main(String[] args) throws SQLException {
        //countByName();
        countBuyTicket();
        //updateTicketById(1L);
        //updateTicketAndFlight(1L);
    }

    private static void updateTicketAndFlight(Long flightId) throws SQLException {
        try (Connection connection = ConnectionManager.open();
             var statement = connection.prepareStatement(updateTicketAndFlightSql)) {
            statement.setBigDecimal(1, new BigDecimal(256));
            statement.setLong(2, flightId);
            statement.setString(3,"GP3005");
            statement.setTimestamp(4, Timestamp.valueOf("2020-09-12 09:15:00"));
            statement.setTimestamp(5, Timestamp.valueOf("2020-09-12 15:00:00"));
            statement.setLong(6,flightId);
            statement.executeUpdate();
        }
    }

    private static void updateTicketById(Long id) throws SQLException {
        try (Connection connection = ConnectionManager.open();
             var statement = connection.prepareStatement(updateTicketSql)) {
            statement.setString(1, "21435");
            statement.setString(2, "Матвей Клещёв");
            statement.setLong(3, 3);
            statement.setString(4, "G3");
            statement.setBigDecimal(5, new BigDecimal(196));
            statement.setLong(6, id);
            statement.executeUpdate();
        }
    }

    private static void countBuyTicket() throws SQLException {

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            ResultSet result = statement.executeQuery(countNameSql);

            while (result.next()) {
                System.out.println(result.getString("passenger_name"));
                System.out.println(result.getBigDecimal("count"));
                System.out.println("------------------");
            }
        }
    }

    private static void countByName() throws SQLException {

        try (Connection connection = ConnectionManager.open();
             var statement = connection.createStatement()) {

            ResultSet result = statement.executeQuery(countTicketSql);

            while (result.next()) {
                System.out.println(result.getString("passenger_name"));
                System.out.println(result.getBigDecimal("count"));
                System.out.println("------------------");
            }
        }
    }
}
