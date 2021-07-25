package com.github.at6ue.jersey;

import java.sql.Connection;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import com.github.at6ue.jersey.injection.GreetingService;
import com.github.at6ue.jersey.injection.HelloService;
import com.github.at6ue.jersey.injection.IdentityService;
import com.github.at6ue.jersey.transaction.ConnectionSupplier;
import com.github.at6ue.jersey.transaction.DatabaseInitializer;
import com.github.at6ue.jersey.transaction.TransactionInterceptionService;
import com.github.at6ue.jersey.transaction.TransactionInterceptor;
import com.github.at6ue.jersey.validation.ConstraintViolationExceptionMapper;
import com.github.at6ue.jersey.validation.ValidationConfigurationContextResolver;

import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

@ApplicationPath("app")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages(getClass().getPackage().getName());
        register(AddCharsetUtf8ToResponseFilter.class);

        // For demonstration of transaction
        register(DatabaseInitializer.class);

        // For demonstration of Bean Validation
        register(JacksonFeature.class);
        register(ValidationFeature.class);
        register(ValidationConfigurationContextResolver.class);
        register(ConstraintViolationExceptionMapper.class);
        
        register(new AbstractBinder() {
            @Override
            protected void configure() {

                // For demonstration of dependency injection
                bind(HelloService.class).to(GreetingService.class);
                bindAsContract(IdentityService.class);

                // For demonstration of transaction
                bindAsContract(TransactionInterceptor.class).in(Singleton.class);
                bind(TransactionInterceptionService.class).to(InterceptionService.class).in(Singleton.class);
                bindFactory(ConnectionSupplier.class).to(Connection.class).proxy(true).proxyForSameScope(false)
                        .in(RequestScoped.class);
            }
        });

    }
}
