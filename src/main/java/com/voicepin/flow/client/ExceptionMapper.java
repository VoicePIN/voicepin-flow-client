package com.voicepin.flow.client;

import com.voicepin.flow.client.exception.IncorrectAudioInputException;
import com.voicepin.flow.client.exception.FlowServerException;
import com.voicepin.flow.client.exception.InvalidAudioException;
import com.voicepin.flow.client.exception.VoiceprintNotEnrolledException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
class ExceptionMapper {

    private final Map<Integer, Definition> definitions = new HashMap<>();

    ExceptionMapper() {
        definitions.put(415, IncorrectAudioInputException::new);
        definitions.put(420, VoiceprintNotEnrolledException::new);
    }

    void validate(final Response response) throws FlowServerException {
        final int statusCode = response.getStatus();

        if (statusCode != Response.Status.OK.getStatusCode()) {
            Definition definition = definitions.get(statusCode);
            if (definition != null) {
                throw definition.apply(statusCode, response.readEntity(String.class));
            } else {
                throw new FlowServerException(statusCode, response.readEntity(String.class));
            }
        }
    }

    @FunctionalInterface
    private interface Definition {
        FlowServerException apply(int code, String message);
    }

}
