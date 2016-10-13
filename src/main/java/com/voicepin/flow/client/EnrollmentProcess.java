package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.EnrollResultCall;
import com.voicepin.flow.client.calls.EnrollStreamCall;
import com.voicepin.flow.client.data.SpeechStream;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.EnrollResultRequest;
import com.voicepin.flow.client.request.EnrollStreamRequest;
import com.voicepin.flow.client.result.EnrollInitResult;
import com.voicepin.flow.client.result.EnrollStatus;
import com.voicepin.flow.client.result.EnrollStreamResult;

import java.util.concurrent.CompletableFuture;

/**
 * @author Lukasz Warzecha
 */
public class EnrollmentProcess implements StreamingProcess {

    private final Caller caller;
    private final EnrollInitResult initResult;
    private final CompletableFuture<EnrollStatus> futureResult;

    EnrollmentProcess(Caller caller, EnrollInitResult initResult, SpeechStream speechStream) {
        this.caller = caller;
        this.initResult = initResult;
        this.futureResult = new CompletableFuture<>();

        EnrollStreamRequest streamReq = new EnrollStreamRequest(initResult.getSpeechPath(), speechStream);
        Call<EnrollStreamResult> streamCall = new EnrollStreamCall(streamReq);

        caller.asyncCall(streamCall).whenComplete((streamResult, throwable) -> {

            if (throwable != null) {
                futureResult.completeExceptionally(getParentException(throwable));
            } else {
                EnrollStatus enrollStatus = new EnrollStatus(streamResult.getProgress());
                futureResult.complete(enrollStatus);
            }
        });
    }

    /**
     * Returns current status of enrollment during speech streaming.
     * 
     * If stream is already finished it returns final status immediately.
     *
     * @return current enrollment status
     */
    public EnrollStatus getCurrentStatus() throws FlowClientException {
        if (!futureResult.isDone()) {
            EnrollResultRequest enrollResultRequest = new EnrollResultRequest(initResult.getStatusPath());
            Call<EnrollStatus> streamCall = new EnrollResultCall(enrollResultRequest);
            return caller.call(streamCall);
        }

        return getFinalStatus();
    }

    /**
     * Blocks current thread in order to get final status of enrollment. If stream already
     * ended it returns final status immediately.
     *
     * @return final enrollment status
     */
    public EnrollStatus getFinalStatus() throws FlowClientException {
        try {
            return futureResult.get();
        } catch (Exception e) {
            throw getParentException(e);
        }
    }

    /**
     * Allows to use this process asynchronously.
     *
     * @return future of final enrollment status
     */
    public CompletableFuture<EnrollStatus> getFinalStatusAsync() {
        return futureResult;
    }

}
