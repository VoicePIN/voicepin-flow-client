package com.voicepin.flow.client.request;

/**
 * @author Lukasz Warzecha
 */
public class EnrollInitRequest {

    private final String voiceprintId;

    public EnrollInitRequest(String voiceprintId) {
        this.voiceprintId = voiceprintId;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }
}
