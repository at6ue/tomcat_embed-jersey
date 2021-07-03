package com.github.at6ue.jersey.basic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
public class BasicResource {
    @GET
    public String get() {
        return "Hello Jersey👕";
    }
}