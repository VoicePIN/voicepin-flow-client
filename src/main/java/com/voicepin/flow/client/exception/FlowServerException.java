package com.voicepin.flow.client.exception;

public class FlowServerException extends FlowClientException {

    private static final long serialVersionUID = 1L;
    final int errorCode;
    final String errorMessage;

    public FlowServerException(int errorCode, String errorMessage) {
        super(String.format("Flow Server error: (%d) %s", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getFlowErrorCode() {
        return errorCode;
    }

    public String getFlowErrorMessage() {
        return errorMessage;
    }

}
