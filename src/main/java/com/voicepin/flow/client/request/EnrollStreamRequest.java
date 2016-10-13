package com.voicepin.flow.client.request;

import com.voicepin.flow.client.data.SpeechStream;

/**
 * @author Lukasz Warzecha
 */
public class EnrollStreamRequest {

    private final String speechPath;
    private final SpeechStream speechStream;

    public EnrollStreamRequest(String speechPath, SpeechStream speechStream) {
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
