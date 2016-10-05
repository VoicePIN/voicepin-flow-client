package com.voicepin.flow.client.result;

/**
 * @author Lukasz Warzecha
 */
public class EnrollInitResult {

    private String speechPath;
    private String statusPath;

    public EnrollInitResult() {
    }

    public EnrollInitResult(String speechPath, String statusPath) {
        this.speechPath = speechPath;
        this.statusPath = statusPath;
    }

    public String getSpeechPath() {
        return speechPath;
    }

    public void setSpeechPath(String speechPath) {
        this.speechPath = speechPath;
    }

    public String getStatusPath() {
        return statusPath;
    }

    public void setStatusPath(String statusPath) {
        this.statusPath = statusPath;
    }
}
