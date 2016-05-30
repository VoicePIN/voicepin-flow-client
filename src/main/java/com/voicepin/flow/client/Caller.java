package com.voicepin.flow.client;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.exception.FlowConnectionException;

/**
 * Calls service request and returns transformed result or exception if an error occurred.
 *
 * @author mckulpa
 */
public class Caller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Caller.class);
    private final WebTarget webTarget;
    private ExceptionMapper exceptionMapper;
    private InvocationBuilderFactory invocationBuilderFactory;

    protected Caller(final String baseURL) {

        final Client client = ClientBuilder.newClient();
        client.register(MultiPartFeature.class);
        client.property(ClientProperties.READ_TIMEOUT, 100000);
        client.property(ClientProperties.CONNECT_TIMEOUT, 100000);


        webTarget = client.target(baseURL);
        exceptionMapper = new ExceptionMapper();
        invocationBuilderFactory = WebTarget::request;
    }

    public <T> T call(final Call<T> call) throws FlowClientException {
        try {
            final String path = call.getPath();
            final String method = call.getMethod().toString();
            final Entity<?> entity = call.getEntity();

            final WebTarget callTarget = webTarget.path(path);
            Builder request = invocationBuilderFactory.getInvocationBuilder(callTarget);
            if (call.isChunked()) {
                request = request.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
            } else {
                request = request
                        .property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);
            }

            LOGGER.debug("Sending {} request to {}", method, callTarget.getUri());

            final Response response = request.method(method, entity);
            exceptionMapper.validate(response);

            LOGGER.debug(response.toString());
            LOGGER.debug("Response body: " + response.toString());


            return call.parse(response);
        } catch (final ProcessingException e) {
            throw new FlowConnectionException(e);
        }
    }

    public void setInvocationBuilderFactory(final InvocationBuilderFactory invocationBuilderFactory) {
        this.invocationBuilderFactory = invocationBuilderFactory;
    }

    public void setExceptionMapper(final ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
    }

    public interface InvocationBuilderFactory {
        Builder getInvocationBuilder(WebTarget callTarget);
    }

}
