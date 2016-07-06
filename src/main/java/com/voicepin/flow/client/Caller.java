package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.exception.FlowConnectionException;
import com.voicepin.flow.client.ssl.CertificateStrategy;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Calls service request and returns transformed result or exception if an error occurred.
 *
 * @author mckulpa
 */
class Caller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Caller.class);

    private final WebTarget webTarget;
    private final ExceptionMapper exceptionMapper;
    private final InvocationBuilderFactory invocationBuilderFactory;

    Caller(final String baseURL) {

        final Client client = ClientBuilder.newClient();
        client.register(MultiPartFeature.class);
        client.property(ClientProperties.READ_TIMEOUT, 100000);
        client.property(ClientProperties.CONNECT_TIMEOUT, 100000);

        webTarget = client.target(baseURL);
        exceptionMapper = new ExceptionMapper();
        invocationBuilderFactory = WebTarget::request;
    }

    Caller(final String baseURL, String username, String password, CertificateStrategy certificateStrategy) {
        final Client client = ClientBuilder.newBuilder()
                .sslContext(certificateStrategy.getSSLContext())
                .hostnameVerifier(certificateStrategy.getHostnameVerifer())
                .build();
        client.register(MultiPartFeature.class);
        client.property(ClientProperties.READ_TIMEOUT, 100000);
        client.property(ClientProperties.CONNECT_TIMEOUT, 100000);

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);

        webTarget = client.target(baseURL);
        exceptionMapper = new ExceptionMapper();
        invocationBuilderFactory = WebTarget::request;
    }

    <T> T call(final Call<T> call) throws FlowClientException {
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

    <T> CompletableFuture<T> asyncCall(final Call<T> call) {

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

        LOGGER.debug("Sending {} async request to {}", method, callTarget.getUri());

        Future<Response> futureResponse = request.async().method(method, entity);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Response response = futureResponse.get();
                exceptionMapper.validate(response);

                LOGGER.debug(response.toString());
                LOGGER.debug("Response body: " + response.toString());

                return call.parse(response);
            } catch (InterruptedException | ExecutionException | FlowClientException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @FunctionalInterface
    private interface InvocationBuilderFactory {

        Builder getInvocationBuilder(WebTarget callTarget);
    }

}