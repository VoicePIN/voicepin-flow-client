package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.VerifyResultCall;
import com.voicepin.flow.client.calls.VerifyStreamCall;
import com.voicepin.flow.client.data.SpeechStream;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.VerifyResultRequest;
import com.voicepin.flow.client.request.VerifyStreamRequest;
import com.voicepin.flow.client.result.VerifyInitResult;
import com.voicepin.flow.client.result.VerifyResult;
import com.voicepin.flow.client.result.VerifyStreamResult;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lukasz Warzecha
 */
public class VerifyStreamClient {

    private final Caller caller;
    private final VerifyInitResult initResult;
    private final CompletableFuture<VerifyStreamResult> futureResult;

    VerifyStreamClient(Caller caller, VerifyInitResult initResult, SpeechStream speechStream) {
        this.caller = caller;
        this.initResult = initResult;

        VerifyStreamRequest streamReq = new VerifyStreamRequest(initResult.getSpeechPath(), speechStream);
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(streamReq);
        this.futureResult = caller.asyncCall(streamCall);
    }

    /**
     * Perfoms blocking operation which asks Voicepin Flow server for the current
     * verification result
     * 
     * @return current verification result
     */
    public VerifyResult getCurrentResult() throws FlowClientException {
        VerifyResultRequest verifyResultRequest = new VerifyResultRequest(initResult.getResultPath());
        Call<VerifyResult> streamCall = new VerifyResultCall(verifyResultRequest);
        return caller.call(streamCall);
    }

    /**
     * Adds listener for verification process. The listener will be called at the end of
     * the verification process
     * 
     * @param verifyListener
     */
    public void addListener(VerifyListener verifyListener) {

        futureResult.whenComplete((streamResult, throwable) -> {

            if (streamResult == null) {
                verifyListener.onError(throwable.getCause());
            } else {
                verifyListener.onSuccess(new VerifyResult(streamResult.getScore(), streamResult.getDecision()));
            }
        });

    }

}
