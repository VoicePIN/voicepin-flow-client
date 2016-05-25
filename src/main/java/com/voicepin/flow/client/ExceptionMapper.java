package com.voicepin.flow.client;


import static javax.ws.rs.core.Response.Status;

import com.voicepin.flow.client.exception.FlowServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
public class ExceptionMapper {
    private Map<Integer, Definition> map = new HashMap<>();

    public ExceptionMapper() {
        for (Definition definition : getDefinitions()) {
            for (Integer code : definition.getCodes()) {
                Definition oldValue = map.put(code, definition);
                if (oldValue != null) {
                    throw new RuntimeException("Tried to map two exceptions to the same error code (" + code + ")");
                }
            }
        }
    }

    // Add new definitions here if you wish to add more exceptions to mapper
    private List<Definition> getDefinitions() {
        List<Definition> definitions = new ArrayList<>();
        // TODO add statuses
        return definitions;
    }

    public void validate(Response response) throws FlowServerException {
        int statusCode = response.getStatus();

        if (statusCode != Status.OK.getStatusCode()) {
            Definition definition = map.get(statusCode);
            FlowServerException e;
            if (definition != null) {
                e = definition.getException(statusCode);
            } else {
                e = new FlowServerException(statusCode, response.readEntity(String.class));
            }

            throw e;
        }
    }

    public interface Definition {
        List<Integer> getCodes();

        FlowServerException getException(int code) throws FlowServerException;
    }
}
