package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.EnrollResultRequest;
import com.voicepin.flow.client.result.EnrollStatus;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class EnrollResultCall implements Call<EnrollStatus> {

    private final EnrollResultRequest req;

    public EnrollResultCall(final EnrollResultRequest enrollResultRequest) {
        this.req = enrollResultRequest;
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

    @Override
    public String getPath() {
        return req.getStatusPath();
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
