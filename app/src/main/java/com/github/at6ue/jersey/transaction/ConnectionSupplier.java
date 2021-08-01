package com.github.at6ue.jersey.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.glassfish.jersey.internal.inject.DisposableSupplier;

public class ConnectionSupplier implements DisposableSupplier<Connection> {

    @Override
    public Connection get() {
        try {
            var ds = (DataSource) InitialContext.doLookup("java:comp/env/jdbc/h2db");
            var con = ds.getConnection();
            con.setAutoCommit(false);
            System.out.println("Supplied");
            return con;
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(Connection instance) {
        System.out.println("Closing...");
        try {
            if (!instance.isClosed()) {
                System.out.println("Roll back");
                instance.rollback();
                instance.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
