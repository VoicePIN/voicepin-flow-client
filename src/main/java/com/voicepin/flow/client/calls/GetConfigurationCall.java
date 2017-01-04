package com.voicepin.flow.client.calls;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicepin.flow.client.Method;
import com.voicepin.flow.client.result.GetConfigurationResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mckulpa
 */
public class GetConfigurationCall implements Call<GetConfigurationResult> {
    private static final String JSON_PROPERTIES = "properties";
    private static final String JSON_CONF_VERIFICATION_THRESHOLD = "VERIFICATION_THRESHOLD";

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyResultCall.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public GetConfigurationCall() {
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

    @Override
    public String getPath() {
        return "configuration";
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
    public GetConfigurationResult parse(Response response) {
        try {
            String jsonString = response.readEntity(String.class);
            JsonNode jsonNode = mapper.readTree(jsonString).get(JSON_PROPERTIES);
            int verificationThreshold = jsonNode.get(JSON_CONF_VERIFICATION_THRESHOLD).asInt();
            return new GetConfigurationResult(verificationThreshold);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
