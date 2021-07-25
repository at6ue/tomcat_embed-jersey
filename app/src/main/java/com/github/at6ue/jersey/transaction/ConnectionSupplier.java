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
            System.out.println("connection provided");
            return con;
        } catch (NamingException | SQLException e) {
            System.out.println("connection failed");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(Connection instance) {
        System.out.println("connection closing...");
        try {
            if (!instance.isClosed()) {
                instance.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
