package com.github.at6ue.jersey.basic;

import java.util.Set;

import javax.ws.rs.core.Application;

public class BasicApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(BasicResource.class);
    }
}
