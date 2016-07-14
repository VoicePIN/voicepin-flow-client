package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.VerifyStreamRequest;
import com.voicepin.flow.client.result.VerifyStreamResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author kodrzywolek
 */
public class VerifyStreamCall implements Call<VerifyStreamResult> {

    private final VerifyStreamRequest req;

    public VerifyStreamCall(final VerifyStreamRequest verifyStreamRequest) {
        this.req = verifyStreamRequest;
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
    public VerifyStreamResult parse(final Response response) {
        return response.readEntity(VerifyStreamResult.class);
    }
}
