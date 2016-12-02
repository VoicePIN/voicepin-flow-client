package com.voicepin.flow.client.result;

/**
 * @author Lukasz Warzecha
 */
public class FinalEnrollStatus {

    private double progress;

    public FinalEnrollStatus() {
    }

    public FinalEnrollStatus(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FinalEnrollStatus{");
        sb.append("progress=").append(progress);
        sb.append('}');
        return sb.toString();
    }

}
