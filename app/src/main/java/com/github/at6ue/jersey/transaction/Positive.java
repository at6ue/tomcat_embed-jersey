package com.github.at6ue.jersey.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.hibernate.validator.constraints.Range;

@Path("positive")
public class Positive {

    private static final String SELECT_VALUE_FROM_POSITIVE = "select value from positive";

    @Inject
    private Connection connection;

    /**
     * 
     * @param n Request body which must be an integer number
     * @return Message with current value
     * @throws SQLException
     */
    @Transactional
    @PUT
    public String add(@Range(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) String n) throws SQLException {
        try (var select = connection.prepareStatement(SELECT_VALUE_FROM_POSITIVE);
                var update = connection.prepareStatement("update positive set value = ?")) {
            var result = select.executeQuery();
            result.next();
            var current = result.getInt("value");
            var sum = current + Integer.parseInt(n);

            update.setInt(1, sum);
            update.executeUpdate();

            if (sum < 0) {
                // Transaction should roll back
                throw new NegativeException();
            }

            // System.out.println("Commit");
            // connection.commit();
            // connection.close();

            return getMessage(sum);
        }
    }

    /**
     * Database read-only access without {@link Transactional} annotation.
     * 
     * @return Message with current value
     * @throws SQLException
     */
    @GET
    public String query() throws SQLException {
        try (var con = connection; var select = con.prepareStatement(SELECT_VALUE_FROM_POSITIVE)) {
            var result = select.executeQuery();
            result.next();
            var current = result.getInt("value");
            return getMessage(current);
        }
    }

    private String getMessage(int value) {
        return String.format("Current value is %d. Never be negative!", value);
    }

}
