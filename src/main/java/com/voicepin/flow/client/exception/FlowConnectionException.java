package com.voicepin.flow.client.exception;

public class FlowConnectionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FlowConnectionException(Throwable cause) {
        super("Could not contact VoicePIN Flow server", cause);
    }

}
