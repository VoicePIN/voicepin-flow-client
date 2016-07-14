package com.voicepin.flow.client.exception;

/**
 * @author rludwa
 */
public class VoiceprintNotEnrolledException extends FlowServerException {

    private static final long serialVersionUID = 1L;

    public VoiceprintNotEnrolledException(int errorCode) {
        super(errorCode, "Voiceprint is not enrolled");
    }
}
