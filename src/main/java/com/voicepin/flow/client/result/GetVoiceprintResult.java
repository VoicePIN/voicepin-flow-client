package com.voicepin.flow.client.result;

/**
 * @author rludwa
 */
public class GetVoiceprintResult {

    public GetVoiceprintResult() {
    }

    private String id;
    private boolean enrolled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
    }

    @Override
    public String toString() {
        return "GetVoiceprintResult{" +
                "id='" + id + '\'' +
                ", enrolled=" + enrolled +
                '}';
    }
}
