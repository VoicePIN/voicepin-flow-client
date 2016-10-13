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
public class VerificationProcess implements StreamingProcess {

    private final Caller caller;
    private final VerifyInitResult initResult;
    private final CompletableFuture<VerifyResult> futureResult;

    VerificationProcess(Caller caller, VerifyInitResult initResult, SpeechStream speechStream) {
        this.caller = caller;
        this.initResult = initResult;
        this.futureResult = new CompletableFuture<>();

        VerifyStreamRequest streamReq = new VerifyStreamRequest(initResult.getSpeechPath(), speechStream);
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(streamReq);

        caller.asyncCall(streamCall).whenComplete((streamResult, throwable) -> {

            if (throwable != null) {
                futureResult.completeExceptionally(getParentException(throwable));
            } else {
                VerifyResult finalResult = new VerifyResult(streamResult.getScore(), streamResult.getDecision());
                futureResult.complete(finalResult);
            }
        });

    }

    /**
     * Returns current result while speech streaming may still be in progress.
     * <p>
     * If stream is already finished it returns final result.
     *
     * @return current verification result
     */
    public VerifyResult getCurrentResult() throws FlowClientException {
        if (!futureResult.isDone()) {
            VerifyResultRequest verifyResultRequest = new VerifyResultRequest(initResult.getResultPath());
            Call<VerifyResult> streamCall = new VerifyResultCall(verifyResultRequest);
            return caller.call(streamCall);
        }

        return getFinalResult();
    }

    /**
     * Blocks current thread in order to get final result of verification. If stream
     * already ended it returns final result immediately.
     *
     * @return final verification result
     */
    public VerifyResult getFinalResult() throws FlowClientException {
        try {
            return futureResult.get();
        } catch (Exception e) {
            throw getParentException(e);
        }
    }

    /**
     * Allows to use this process asynchronously.
     * 
     * @return future of final verification result
     */
    public CompletableFuture<VerifyResult> getFinalResultAsync() {
        return futureResult;
    }

}
