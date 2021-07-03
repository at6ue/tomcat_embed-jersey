package com.github.at6ue.jersey;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;

public class AddCharsetUtf8ToResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        var mediaType = responseContext.getMediaType();
        if (mediaType == null || mediaType.toString().contains(MediaType.CHARSET_PARAMETER)) {
            return;
        }
        responseContext.getHeaders().putSingle("Content-Type", mediaType.toString() + ";charset=utf-8");
    }

}
