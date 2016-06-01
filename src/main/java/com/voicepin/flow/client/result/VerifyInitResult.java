package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyInitResult {

    private String speechPath;
    private String resultPath;

    public VerifyInitResult() {
    }

    public VerifyInitResult(String speechPath, String resultPath) {
        this.speechPath = speechPath;
        this.resultPath = resultPath;
    }

    public String getSpeechPath() {
        return speechPath;
    }

    public void setSpeechPath(String speechPath) {
        this.speechPath = speechPath;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }
}
