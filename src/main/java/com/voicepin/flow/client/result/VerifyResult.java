package com.voicepin.flow.client.result;

/**
 * @author kodrzywolek
 */
public class VerifyResult {

    private Integer score;
    private String decision;

    public VerifyResult(Integer score, String decision) {
        this.score = score;
        this.decision = decision;
    }

    public Integer getScore() {
        return score;
    }

    public String getDecision() {
        return decision;
    }
}
