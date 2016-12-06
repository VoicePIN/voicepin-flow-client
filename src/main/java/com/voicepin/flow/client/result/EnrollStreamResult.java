package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class EnrollStreamResult {

    private double progress;

    public EnrollStreamResult() {
    }

    public EnrollStreamResult(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

}
