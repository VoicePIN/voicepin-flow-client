package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.GetVoiceprintRequest;
import com.voicepin.flow.client.result.GetVoiceprintResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author rludwa
 */
public class GetVoiceprintCall implements Call<GetVoiceprintResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyResultCall.class);

    private final GetVoiceprintRequest req;

    public GetVoiceprintCall(GetVoiceprintRequest getVoiceprintRequest) {
        this.req = getVoiceprintRequest;
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

    @Override
    public String getPath() {
        return "voiceprint/" + req.getVoiceprintId();
    }

    @Override
    public Entity<?> getEntity() {
        return null;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public GetVoiceprintResult parse(Response response) {
        return response.readEntity(GetVoiceprintResult.class);
    }
}
