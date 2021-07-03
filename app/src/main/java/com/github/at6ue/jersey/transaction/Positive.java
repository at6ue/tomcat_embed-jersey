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

    private static final String SELECT_FROM_POSITIVE = "select * from positive";

    @Inject
    private Connection connection;

    /**
     * 
     * @param n Request body which must be an integer number
     * @return
     * @throws SQLException
     */
    @Transactional
    @PUT
    public String add(@Range(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) String n) throws SQLException {
        try (var select = connection.createStatement();
                var update = connection.prepareStatement("update positive set value = ?")) {
            var result = select.executeQuery(SELECT_FROM_POSITIVE);
            result.next();
            var current = result.getInt("value");
            var sum = current + Integer.parseInt(n);

            update.setInt(1, sum);
            update.executeUpdate();

            if (sum < 0) {
                // Transaction should roll back
                throw new NegativeException();
            }

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
        connection.setReadOnly(true);
        // assert connection.isReadOnly(); // not working :(
        try (var con = connection; var select = con.createStatement()) {
            // Connection should be closed in this scope
            var result = select.executeQuery(SELECT_FROM_POSITIVE);
            result.next();
            var current = result.getInt("value");
            return getMessage(current);
        }
    }

    private String getMessage(int value) {
        return String.format("Current value is %d. Never be negative!", value);
    }

}
