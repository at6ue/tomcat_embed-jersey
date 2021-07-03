package com.github.at6ue.jersey.transaction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;

public class TransactionInterceptionService implements InterceptionService {
    private final List<MethodInterceptor> interceptors;

    @Inject
    TransactionInterceptionService(ServiceLocator locator, MethodInterceptor interceptor) {
        locator.inject(interceptor);
        this.interceptors = List.of(interceptor);
    }

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        var isInTransactionScope = method.isAnnotationPresent(Transactional.class)
                || method.getDeclaringClass().isAnnotationPresent(Transactional.class);
        return isInTransactionScope ? interceptors : Collections.emptyList();
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return Collections.emptyList();
    }

}