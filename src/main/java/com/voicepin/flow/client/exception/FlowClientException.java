package com.voicepin.flow.client.exception;

/**
 * @author kodrzywolek
 */
public class FlowClientException extends Exception {
    private static final long serialVersionUID = 1L;

    public FlowClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowClientException(String message) {
        super(message);
    }

    public FlowClientException(Throwable cause) {
        super(cause);
    }

}
