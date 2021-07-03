package com.github.at6ue.jersey.validation;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path.Node;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExtendedExceptionMapper<ConstraintViolationException> {

    @Context
    HttpHeaders headers;

    @Override
    public boolean isMappable(ConstraintViolationException exception) {
        return headers.getAcceptableMediaTypes().stream()
                .anyMatch(t -> t.isCompatible(MediaType.APPLICATION_JSON_TYPE));
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var entity = exception.getConstraintViolations().stream().map(ViolationDescription::new)
                .collect(Collectors.toList());
        return Response.status(Status.BAD_REQUEST).entity(entity).build();
    }

    private static class ViolationDescription {
        @JsonProperty
        private final String fieldName;
        @JsonProperty
        private final String path;
        @JsonProperty
        private final String message;

        public <T> ViolationDescription(ConstraintViolation<T> violation) {
            var path = violation.getPropertyPath();

            // Get the name of the first property or the last node that is the argument of
            // the method.
            this.fieldName = StreamSupport.stream(path.spliterator(), false)
                    .reduce((a, b) -> a != null && a.getKind() == ElementKind.PROPERTY ? a : b).map(Node::getName)
                    .orElse(null);
            this.path = path.toString();
            this.message = violation.getMessage();
        }

    }
}
