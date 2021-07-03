package com.github.at6ue.jersey.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ParameterNameProvider;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;

public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

    @Context
    private ResourceContext resourceContext;

    @Override
    public ValidationConfig getContext(final Class<?> type) {
        final ValidationConfig config = new ValidationConfig();
        config.constraintValidatorFactory(resourceContext.getResource(InjectingConstraintValidatorFactory.class));
        config.parameterNameProvider(new AnnotatedParameterNameProvider());
        return config;
    }

    private class AnnotatedParameterNameProvider implements ParameterNameProvider {

        @Override
        public List<String> getParameterNames(final Constructor<?> constructor) {
            return convertToAnnotatedName(constructor.getParameters());
        }

        @Override
        public List<String> getParameterNames(final Method method) {
            return convertToAnnotatedName(method.getParameters());
        }

        private List<String> convertToAnnotatedName(Parameter[] parameters) {
            var ret = new ArrayList<String>();
            for (var param : parameters) {
                var queryParam = param.getAnnotation(QueryParam.class);
                if (queryParam != null) {
                    ret.add(queryParam.value());
                    continue;
                }

                var pathParam = param.getAnnotation(PathParam.class);
                if (pathParam != null) {
                    ret.add(pathParam.value());
                    continue;
                }

                ret.add(param.getName());
            }
            return ret;
        }
    }
}