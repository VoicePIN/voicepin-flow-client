package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.VerifyResultRequest;
import com.voicepin.flow.client.result.VerifyResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class VerifyResultCall implements Call<VerifyResult> {

    private final VerifyResultRequest req;

    public VerifyResultCall(final VerifyResultRequest verifyResultRequest) {
        this.req = verifyResultRequest;
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

    @Override
    public String getPath() {
        return req.getResultPath();
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public Entity<?> getEntity() {
        return null;
    }

    @Override
    public VerifyResult parse(Response response) {
        return response.readEntity(VerifyResult.class);
    }
}
