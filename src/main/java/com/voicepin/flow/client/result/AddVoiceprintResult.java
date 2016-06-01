package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class AddVoiceprintResult {

    private String voiceprintId;

    public AddVoiceprintResult(String voiceprintId) {
        this.voiceprintId = voiceprintId;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }
}
