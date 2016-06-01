package com.voicepin.flow.client.request;


import com.voicepin.flow.client.data.SpeechStream;

/**
 * @author kodrzywolek
 */
public class EnrollRequest {

    private SpeechStream speechStream;
    private String voiceprintId;


    public EnrollRequest(String voiceprintId, SpeechStream speechStream) {
        this.speechStream = speechStream;
        this.voiceprintId = voiceprintId;
    }

    public SpeechStream getSpeechStream() {
        return speechStream;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }
}
