package com.voicepin.flow.client.calls;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.VerifyStreamRequest;
import com.voicepin.flow.client.result.VerifyStreamResult;

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
    public VerifyStreamResult parse(final Response response) throws FlowParseException {
        return response.readEntity(VerifyStreamResult.class);
    }
}
