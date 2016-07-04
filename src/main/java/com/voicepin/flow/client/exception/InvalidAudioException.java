package com.voicepin.flow.client.exception;

/**
 * @author rludwa
 */
public class InvalidAudioException extends FlowServerException {

    private static final long serialVersionUID = 1L;

    public InvalidAudioException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
