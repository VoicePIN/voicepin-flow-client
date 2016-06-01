package com.voicepin.flow.client.request;

import com.voicepin.flow.client.data.SpeechStream;

/**
 * @author kodrzywolek
 */
public class VerifyStreamRequest {

    private final String speechPath;
    private final SpeechStream speechStream;

    public VerifyStreamRequest(String speechPath, SpeechStream speechStream) {
        this.speechPath = speechPath;
        this.speechStream = speechStream;
    }

    public String getSpeechPath() {
        return speechPath;
    }

    public SpeechStream getSpeechStream() {
        return speechStream;
    }
}
