package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyResult {

    private Integer score;
    private String status;
    private String decision;
    private Integer blacklistScore;

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

    public Integer getBlacklistScore() {
        return blacklistScore;
    }

    public void setBlacklistScore(Integer blacklistScore) {
        this.blacklistScore = blacklistScore;
    }

    @Override
    public String toString() {
        return "VerifyResult{" +
                "score=" + score +
                ", status='" + status + '\'' +
                ", decision='" + decision + '\'' +
                ", blacklistScore='" + blacklistScore + '\'' +
                '}';
    }
}
