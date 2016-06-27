package com.voicepin.flow.client.calls;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.VerifyResultRequest;
import com.voicepin.flow.client.result.VerifyResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Warzecha
 */
public class VerifyResultCall implements Call<VerifyResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyResultCall.class);

    protected final ObjectMapper mapper = new ObjectMapper();

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
    public VerifyResult parse(Response response) throws FlowParseException {
        try {
            String string = response.readEntity(String.class);
            VerifyResult result = mapper.readValue(string, VerifyResult.class);
            return result;
        } catch (IOException e) {
            throw new FlowParseException(e);
        }

    }
}
