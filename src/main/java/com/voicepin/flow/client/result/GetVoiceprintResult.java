package com.voicepin.flow.client.result;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author rludwa
 */
public class GetVoiceprintResult {

    public GetVoiceprintResult() {
    }

    private String id;
    private boolean enrolled;
    private Instant createdAt;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("enrolled", enrolled)
                .add("createdAt", createdAt)
                .toString();
    }
}
