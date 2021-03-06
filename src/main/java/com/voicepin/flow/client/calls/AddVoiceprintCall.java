package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.result.AddVoiceprintResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author kodrzywolek
 */
public class AddVoiceprintCall implements Call<AddVoiceprintResult> {

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
    public AddVoiceprintResult parse(Response response) {
        return new AddVoiceprintResult(response.readEntity(String.class));
    }
}
