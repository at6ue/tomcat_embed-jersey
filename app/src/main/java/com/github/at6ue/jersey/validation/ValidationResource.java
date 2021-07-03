package com.github.at6ue.jersey.validation;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@Path("validation")
@Produces(MediaType.APPLICATION_JSON)
public class ValidationResource {

    @QueryParam("height")
    @Positive
    private int height;

    @GET
    @Path("{id}")
    public Object bean(@Valid @BeanParam ParamBean bean, @QueryParam("weight") @Positive int weight) {
        bean.setBmi(height, weight);
        return bean;
    }

    public static class IdNumber {
        @Pattern(regexp = "[0-9a-fA-F]{8}")
        private final String value;

        private IdNumber(String value) {
            this.value = value;
        }

        public static IdNumber valueOf(String value) {
            return new IdNumber(value);
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    private static class ParamBean {
        @JsonProperty
        @QueryParam("name")
        @NotEmpty
        private String name;

        @JsonProperty
        @Min(18)
        private int age;

        @JsonProperty
        @Valid
        @PathParam("id")
        private IdNumber id;

        @JsonProperty
        private double bmi;

        private ParamBean(@QueryParam("age") int age) {
            this.age = age;
        }

        public void setBmi(int height, int weight) {
            bmi = 10000 * weight / (height * height);
        }
    }
}
