package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.EnrollInitRequest;
import com.voicepin.flow.client.result.EnrollInitResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class EnrollInitCall implements Call<EnrollInitResult> {

    private final EnrollInitRequest req;

    public EnrollInitCall(EnrollInitRequest enrollInitRequest) {
        this.req = enrollInitRequest;
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    @Override
    public String getPath() {
        return "voiceprint/" + req.getVoiceprintId() + "/enrollment";
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
    public EnrollInitResult parse(Response response) {
        return response.readEntity(EnrollInitResult.class);
    }

}
