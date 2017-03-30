package com.voicepin.flow.client.result;

/**
 * @author Lukasz Warzecha
 */
public class FinalVerifyResult {

    private Integer score;
    private String decision;
    private Integer blacklistScore;

    public FinalVerifyResult() {
    }

    public FinalVerifyResult(Integer score, String decision, Integer blacklistScore) {
        this.score = score;
        this.decision = decision;
        this.blacklistScore = blacklistScore;
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

    public Integer getBlacklistScore() {
        return blacklistScore;
    }

    public void setBlacklistScore(Integer blacklistScore) {
        this.blacklistScore = blacklistScore;
    }

    @Override
    public String toString() {
        return "FinalVerifyResult{" +
                "score=" + score +
                ", decision='" + decision + '\'' +
                ", blacklistScore='" + blacklistScore + '\'' +
                '}';
    }

}
