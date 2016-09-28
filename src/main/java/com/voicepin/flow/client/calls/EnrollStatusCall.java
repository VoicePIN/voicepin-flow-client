package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.VerifyResultRequest;
import com.voicepin.flow.client.result.EnrollStatus;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class EnrollStatusCall implements Call<EnrollStatus> {

    private final VerifyResultRequest req;

    public EnrollStatusCall(final VerifyResultRequest verifyResultRequest) {
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
    public EnrollStatus parse(Response response) {
        return response.readEntity(EnrollStatus.class);
    }
}
