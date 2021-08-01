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
        try {
            var ret = invocation.proceed();
            if (!connection.isClosed()) {
                System.out.println("[AOP] Commit");
                connection.commit();
                connection.close();
            }
            return ret;
        } catch (Throwable t) {
            if (!connection.isClosed()) {
                System.out.println("[AOP] Roll back");
                connection.rollback();
                connection.close();
            }

            throw t;
        }
    }

}
