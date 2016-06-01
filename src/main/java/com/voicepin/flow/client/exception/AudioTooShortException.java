package com.voicepin.flow.client.exception;

/**
 * @author rludwa
 */
public class AudioTooShortException extends FlowServerException {
    private static final long serialVersionUID = 1L;

    public AudioTooShortException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
