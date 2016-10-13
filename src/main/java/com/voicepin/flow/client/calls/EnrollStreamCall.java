package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.EnrollStreamRequest;
import com.voicepin.flow.client.result.EnrollStreamResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class EnrollStreamCall implements Call<EnrollStreamResult> {

    private final EnrollStreamRequest req;

    public EnrollStreamCall(final EnrollStreamRequest enrollStreamRequest) {
        this.req = enrollStreamRequest;
    }

    @Override
    public Method getMethod() {
        return Method.PUT;
    }

    @Override
    public String getPath() {
        return req.getSpeechPath();
    }

    @Override
    public boolean isChunked() {
        return true;
    }

    @Override
    public Entity<?> getEntity() {
        return Entity.entity(req.getSpeechStream(), MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public EnrollStreamResult parse(final Response response) {
        return response.readEntity(EnrollStreamResult.class);
    }
}
