package com.voicepin.flow.client.request;

/**
 * @author Lukasz Warzecha
 */
public class EnrollResultRequest {

    private final String statusPath;

    public EnrollResultRequest(String statusPath) {
        this.statusPath = statusPath;
    }

    public String getStatusPath() {
        return statusPath;
    }
}
