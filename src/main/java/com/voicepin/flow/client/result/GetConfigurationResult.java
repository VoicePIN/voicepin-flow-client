package com.voicepin.flow.client.result;

/**
 * @author mckulpa
 */
public class GetConfigurationResult {
    private int verificationThreshold;

    public GetConfigurationResult(int verificationThreshold) {
        this.verificationThreshold = verificationThreshold;
    }

    public int getVerificationThreshold() {
        return verificationThreshold;
    }
}
