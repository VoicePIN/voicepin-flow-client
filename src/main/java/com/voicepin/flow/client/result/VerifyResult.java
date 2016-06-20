package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyResult {

    private Integer score;
    private String status;
    private String decision;

    public VerifyResult() {

    }

    public VerifyResult(Integer score, String decision) {
        this.score = score;
        this.decision = decision;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Integer getScore() {
        return score;
    }

    public String getDecision() {
        return decision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
