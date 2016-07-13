package com.voicepin.flow.client;

import com.voicepin.flow.client.exception.AudioTooShortException;
import com.voicepin.flow.client.exception.FlowServerException;
import com.voicepin.flow.client.exception.InvalidAudioException;
import com.voicepin.flow.client.exception.VoiceprintNotEnrolled;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

/**
 * @author mckulpa, kodrzywolek
 */
class ExceptionMapper {

    private final Collection<Definition> definitions = new ArrayList<>();

    ExceptionMapper() {

        definitions.add(code -> {
            if (code == 430) {
                throw new InvalidAudioException(code);
            }
        });

        definitions.add(code -> {
            if (code == 431) {
                throw new AudioTooShortException(code);
            }
        });

        definitions.add(code -> {
            if (code == 420) {
                throw new VoiceprintNotEnrolled(code);
            }
        });
    }

    void validate(final Response response) throws FlowServerException {
        final int statusCode = response.getStatus();

        if (statusCode != Response.Status.OK.getStatusCode()) {
            for (final Definition def : definitions) {
                def.apply(statusCode);
            }
            throw new FlowServerException(statusCode, response.readEntity(String.class));
        }
    }

    @FunctionalInterface
    private interface Definition {

        void apply(int code) throws FlowServerException;

    }

}
