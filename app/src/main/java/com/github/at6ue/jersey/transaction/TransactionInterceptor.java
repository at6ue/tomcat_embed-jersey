package com.github.at6ue.jersey.transaction;

import java.sql.Connection;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TransactionInterceptor implements MethodInterceptor {

    @Inject
    private Connection connection;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Begin");
        connection.setAutoCommit(false);
        try {
            var ret = invocation.proceed();
            System.out.println("Commit");
            if (!connection.isClosed()) {
                connection.commit();
                connection.close();
            }
            return ret;
        } catch (Throwable t) {
            System.out.println("Roll back");
            if (!connection.isClosed()) {
                connection.rollback();
                connection.close();
            }

            throw t;
        }
    }

}
