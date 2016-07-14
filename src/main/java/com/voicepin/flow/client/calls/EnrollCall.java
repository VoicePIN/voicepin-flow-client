package com.voicepin.flow.client.calls;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.result.EnrollResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
public class EnrollCall implements Call<EnrollResult> {

    private final EnrollRequest req;

    public EnrollCall(final EnrollRequest enrollRequest) {
        this.req = enrollRequest;
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
        return true;
    }

    @Override
    public Entity<?> getEntity() {
        return Entity.entity(req.getSpeechStream(), MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public EnrollResult parse(final Response response) {
        return new EnrollResult();
    }
}
