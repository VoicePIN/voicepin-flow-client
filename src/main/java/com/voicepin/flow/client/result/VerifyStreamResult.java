package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyStreamResult {

    private Integer score;
    private String decision;
    private Integer blacklistScore;

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

    public Integer getBlacklistScore() {
        return blacklistScore;
    }

    public void setBlacklistScore(Integer blacklistScore) {
        this.blacklistScore = blacklistScore;
    }
}
