package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyStreamResult {

    private Integer score;
    private String decision;

    public VerifyStreamResult() {
    }

    public VerifyStreamResult(Integer score, String decision) {
        this.score = score;
        this.decision = decision;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
