package com.voicepin.flow.client.exception;

/**
 * @author rludwa
 */
public class IncorrectAudioInputException extends FlowServerException {

    private static final long serialVersionUID = 1L;

    public IncorrectAudioInputException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
