package com.github.at6ue.jersey.injection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("greeting")
public class Greeting {

    private GreetingService greetingService;
    private IdentityService identityService;

    @Inject
    Greeting(GreetingService greetingService, IdentityService nameService) {
        this.greetingService = greetingService;
        this.identityService = nameService;
    }

    @GET
    public String sayHello() {
        return String.format("%s from %s", greetingService.getMessage(), identityService.getName());
    }
}