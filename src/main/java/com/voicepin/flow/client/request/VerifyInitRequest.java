package com.voicepin.flow.client.request;

/**
 * @author kodrzywolek
 */
public class VerifyInitRequest {

    private final String voiceprintId;

    public VerifyInitRequest(String voiceprintId) {
        this.voiceprintId = voiceprintId;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }
}
