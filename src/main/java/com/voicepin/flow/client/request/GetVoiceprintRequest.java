package com.voicepin.flow.client.request;

/**
 * @author rludwa
 */
public class GetVoiceprintRequest {

    private String voiceprintId;

    public GetVoiceprintRequest(String voiceprintId) {
        this.voiceprintId = voiceprintId;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }

    public void setVoiceprintId(String voiceprintId) {
        this.voiceprintId = voiceprintId;
    }
}
