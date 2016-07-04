package com.voicepin.flow.client.calls;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.exception.FlowParseException;
import com.voicepin.flow.client.request.GetVoiceprintRequest;
import com.voicepin.flow.client.result.GetVoiceprintResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author rludwa
 */
public class GetVoiceprintCall implements Call<GetVoiceprintResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyResultCall.class);

    protected final ObjectMapper mapper = new ObjectMapper();

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
    public GetVoiceprintResult parse(Response response) throws FlowParseException {
        try {
            String string = response.readEntity(String.class);
            GetVoiceprintResult result = mapper.readValue(string, GetVoiceprintResult.class);
            return result;
        } catch (IOException e) {
            throw new FlowParseException(e);
        }
    }
}
