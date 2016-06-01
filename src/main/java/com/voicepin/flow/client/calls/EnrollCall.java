package com.voicepin.flow.client.calls;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.result.EnrollResult;

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
    public EnrollResult parse(final Response response) throws FlowParseException {
        return new EnrollResult();
    }
}
