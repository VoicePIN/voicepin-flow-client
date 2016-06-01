package com.voicepin.flow.client.exception;

public class FlowServerException extends FlowClientException {
    private static final long serialVersionUID = 1L;

    public FlowServerException(int errorCode, String errorMessage) {
        super(String.format("Flow Server error: (%d) %s", errorCode, errorMessage));
    }

    public FlowServerException(String message) {
        super(message);
    }

}
