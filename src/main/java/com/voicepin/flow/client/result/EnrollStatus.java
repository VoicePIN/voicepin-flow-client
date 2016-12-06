package com.voicepin.flow.client.result;

import java.util.List;

public class EnrollStatus {

    private double progress;
    private List<AudioIssue> audioIssues;

    public EnrollStatus() {
    }

    public EnrollStatus(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public List<AudioIssue> getAudioIssues() {
        return audioIssues;
    }

    public void setAudioIssues(List<AudioIssue> audioIssues) {
        this.audioIssues = audioIssues;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EnrollStatus{");
        sb.append("progress=").append(progress);
        sb.append(", audioIssues=").append(audioIssues);
        sb.append('}');
        return sb.toString();
    }
}
