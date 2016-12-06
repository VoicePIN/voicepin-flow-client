package com.voicepin.flow.client.result;

/**
 * @author Lukasz Warzecha
 */
public class FinalVerifyResult {

    private Integer score;
    private String decision;

    public FinalVerifyResult() {
    }

    public FinalVerifyResult(Integer score, String decision) {
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

    @Override
    public String toString() {
        return "FinalVerifyResult{" +
                "score=" + score +
                ", decision='" + decision + '\'' +
                '}';
    }

}
