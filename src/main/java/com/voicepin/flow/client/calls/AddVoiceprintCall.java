package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.AddVoiceprintRequest;
import com.voicepin.flow.client.result.AddVoiceprintResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author kodrzywolek
 */
public class AddVoiceprintCall implements Call<AddVoiceprintResult> {

    private final AddVoiceprintRequest req;

    public AddVoiceprintCall(AddVoiceprintRequest addVoiceprintRequest) {
        this.req = addVoiceprintRequest;
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    @Override
    public String getPath() {
        return "voiceprint";
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
    public AddVoiceprintResult parse(Response response) throws FlowParseException {

        return new AddVoiceprintResult(response.readEntity(String.class));
    }
}
