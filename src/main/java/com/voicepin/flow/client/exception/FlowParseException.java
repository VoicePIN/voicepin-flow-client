package com.voicepin.flow.client.exception;

public class FlowParseException extends FlowClientException {
    private static final long serialVersionUID = 1L;

    public FlowParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowParseException(String message) {
        super(message);
    }

    public FlowParseException(Throwable cause) {
        super(cause);
    }

}
