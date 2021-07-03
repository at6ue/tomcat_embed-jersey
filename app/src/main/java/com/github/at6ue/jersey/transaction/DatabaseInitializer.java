package com.github.at6ue.jersey.transaction;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

public class DatabaseInitializer implements ApplicationEventListener {

    @Override
    public void onEvent(ApplicationEvent event) {
        if (event.getType().equals(ApplicationEvent.Type.INITIALIZATION_START)) {
            createTable();
            System.out.println("DB initialized");
        }
    }

    private void createTable() {
        try {
            var ds = (DataSource) InitialContext.doLookup("java:comp/env/jdbc/h2db");
            try (var con = ds.getConnection(); var stmnt = con.createStatement()) {
                stmnt.execute("create table positive(value integer)");
                stmnt.executeUpdate("insert into positive values(0)");
            }
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }

}
