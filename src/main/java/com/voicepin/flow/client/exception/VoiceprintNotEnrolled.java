package com.voicepin.flow.client.exception;

/**
 * @author rludwa
 */
public class VoiceprintNotEnrolled extends FlowServerException {

    private static final long serialVersionUID = 1L;

    public VoiceprintNotEnrolled(int errorCode) {
        super(errorCode, "Voiceprint is not enrolled");
    }
}
