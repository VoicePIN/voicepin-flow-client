package com.voicepin.flow.client.request;


import com.voicepin.flow.client.data.SpeechStream;

/**
 * @author kodrzywołek
 */
public class VerifyRequest {

    private final String voiceprintId;
    private final SpeechStream speechStream;

    public VerifyRequest(String voiceprintId, SpeechStream speechStream) {
        super();
        this.voiceprintId = voiceprintId;
        this.speechStream = speechStream;
    }

    public String getVoiceprintId() {
        return voiceprintId;
    }

    public SpeechStream getSpeechStream() {
        return speechStream;
    }
}
