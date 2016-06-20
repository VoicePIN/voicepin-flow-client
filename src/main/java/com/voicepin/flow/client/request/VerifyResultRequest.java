package com.voicepin.flow.client.request;

/**
 * @author Lukasz Warzecha
 */
public class VerifyResultRequest {

    private final String resultPath;

    public VerifyResultRequest(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getResultPath() {
        return resultPath;
    }
}
