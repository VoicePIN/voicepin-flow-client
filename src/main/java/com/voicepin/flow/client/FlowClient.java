package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.AddVoiceprintCall;
import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.EnrollCall;
import com.voicepin.flow.client.calls.VerifyInitCall;
import com.voicepin.flow.client.calls.VerifyResultCall;
import com.voicepin.flow.client.calls.VerifyStreamCall;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.AddVoiceprintRequest;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.request.VerifyRequest;
import com.voicepin.flow.client.request.VerifyResultRequest;
import com.voicepin.flow.client.request.VerifyStreamRequest;
import com.voicepin.flow.client.result.AddVoiceprintResult;
import com.voicepin.flow.client.result.EnrollResult;
import com.voicepin.flow.client.result.VerifyInitResult;
import com.voicepin.flow.client.result.VerifyResult;
import com.voicepin.flow.client.result.VerifyStreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kodrzywolek
 */
public class FlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowClient.class);
    private final Caller caller;

    public FlowClient(String baseURL) {
        caller = new Caller(baseURL);
    }

    public AddVoiceprintResult addVoiceprint(AddVoiceprintRequest addVoiceprintRequest) throws FlowClientException {
        Call<AddVoiceprintResult> call = new AddVoiceprintCall(addVoiceprintRequest);
        return caller.call(call);
    }

    public EnrollResult enroll(EnrollRequest enrollRequest) throws FlowClientException {
        Call<EnrollResult> call = new EnrollCall(enrollRequest);
        return caller.call(call);
    }

    public VerifyResult verify(VerifyRequest verifyRequest) throws FlowClientException {

        VerifyInitResult initResult = initVerifySession(new VerifyInitRequest(verifyRequest.getVoiceprintId()));

        VerifyStreamRequest streamReq = new VerifyStreamRequest(initResult.getSpeechPath(),
                verifyRequest.getSpeechStream());
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(streamReq);
        VerifyStreamResult streamResult = caller.call(streamCall);

        return new VerifyResult(streamResult.getScore(), streamResult.getDecision());
    }

    public VerifyInitResult initVerifySession(VerifyInitRequest verifyInitRequest) throws FlowClientException {
        VerifyInitRequest initReq = new VerifyInitRequest(verifyInitRequest.getVoiceprintId());
        Call<VerifyInitResult> initCall = new VerifyInitCall(initReq);
        return caller.call(initCall);
    }

    public VerifyStreamResult verify(VerifyStreamRequest verifyStreamRequest) throws FlowClientException {
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(verifyStreamRequest);
        return caller.call(streamCall);
    }

    public VerifyResult getVerifyCurrentResult(VerifyResultRequest verifyStreamRequest)
            throws FlowClientException {
        Call<VerifyResult> streamCall = new VerifyResultCall(verifyStreamRequest);
        return caller.call(streamCall);
    }

}
