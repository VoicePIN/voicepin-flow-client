package com.voicepin.flow.client.result;

public class EnrollStatus {

    private double progress;

    public EnrollStatus() {
    }

    public EnrollStatus(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return "EnrollStatus{" + "progress=" + progress + '}';
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
