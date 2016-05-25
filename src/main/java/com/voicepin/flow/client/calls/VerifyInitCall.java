package com.voicepin.flow.client.calls;


import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.ParsedResponse;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.result.VerifyInitResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
public class VerifyInitCall implements Call<VerifyInitResult> {
    private final VerifyInitRequest req;

    public VerifyInitCall(VerifyInitRequest verifyInitRequest) {
        this.req = verifyInitRequest;
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    @Override
    public String getPath() {
        return "voiceprint/" + req.getVoiceprintId() + "/verification";
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
    public ParsedResponse<VerifyInitResult> parse(Response response) throws FlowParseException {
        return () -> response.readEntity(VerifyInitResult.class);
    }

}
