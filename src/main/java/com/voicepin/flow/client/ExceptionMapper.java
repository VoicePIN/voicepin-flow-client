package com.voicepin.flow.client;


import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.core.Response;

import com.voicepin.flow.client.exception.AudioTooShortException;
import com.voicepin.flow.client.exception.FlowServerException;
import com.voicepin.flow.client.exception.InvalidAudioException;
import com.voicepin.flow.client.exception.VoiceprintNotEnrolled;

/**
 * @author mckulpa, kodrzywolek
 */
public class ExceptionMapper {

    private final Collection<Definition> definitions = new ArrayList<>();

    public ExceptionMapper() {

        definitions.add(code -> {
            if (code == 430) {
                throw new InvalidAudioException(code, "Provided audio is incorrect");
            }
        });

        definitions.add(code -> {
            if (code == 431) {
                throw new AudioTooShortException(code, "Provided audio is too short");
            }
        });

        definitions.add(code -> {
            if (code == 420) {
                throw new VoiceprintNotEnrolled(code, "Voiceprint is not enrolled");
            }
        });
    }

    public void validate(final Response response) throws FlowServerException {
        final int statusCode = response.getStatus();
        if (statusCode != Response.Status.OK.getStatusCode()) {
            for (final Definition def : definitions) {
                def.apply(statusCode);
            }

            throw new FlowServerException(statusCode, response.readEntity(String.class));
        }
    }

    @FunctionalInterface
    interface Definition {
        void apply(int code) throws FlowServerException;
    }

}
