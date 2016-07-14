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
 * Provides current verification results (while still streaming) and accepts a final
 * success/error callback.
 *
 * @author Lukasz Warzecha
 */
public class VerificationProcess {

    private final Caller caller;
    private final VerifyInitResult initResult;
    private final CompletableFuture<VerifyStreamResult> futureResult;

    VerificationProcess(Caller caller, VerifyInitResult initResult, SpeechStream speechStream) {
        this.caller = caller;
        this.initResult = initResult;

        VerifyStreamRequest streamReq = new VerifyStreamRequest(initResult.getSpeechPath(), speechStream);
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(streamReq);
        this.futureResult = caller.asyncCall(streamCall);
    }

    /**
     * Returns current result while speech streaming may still be in progress.
     *
     * @return current verification result
     */
    public VerifyResult getCurrentResult() throws FlowClientException {
        VerifyResultRequest verifyResultRequest = new VerifyResultRequest(initResult.getResultPath());
        Call<VerifyResult> streamCall = new VerifyResultCall(verifyResultRequest);
        return caller.call(streamCall);
    }

    /**
     * Adds listener for verification process which will be called on final success or
     * error.
     * 
     * @param verificationProcessListener
     */
    // TODO - add additional method to the Listener which allows to listening for
    // currentResults in given interval
    public void addListener(VerificationProcessListener verificationProcessListener) {

        futureResult.whenComplete((streamResult, throwable) -> {

            if (streamResult == null) {
                verificationProcessListener.onError(throwable.getCause());
            } else {
                verificationProcessListener.onSuccess(new VerifyResult(streamResult.getScore(),
                        streamResult.getDecision()));
            }
        });

    }

}
