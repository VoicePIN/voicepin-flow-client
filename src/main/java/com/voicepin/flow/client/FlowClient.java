package com.voicepin.flow.client;


import com.voicepin.flow.client.calls.*;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.*;
import com.voicepin.flow.client.result.*;

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
        VerifyInitRequest initReq = new VerifyInitRequest(verifyRequest.getVoiceprintId());
        Call<VerifyInitResult> initCall = new VerifyInitCall(initReq);
        VerifyInitResult initResult = caller.call(initCall);

        VerifyStreamRequest streamReq = new VerifyStreamRequest(initResult.getSpeechPath(),
                verifyRequest.getSpeechStream());
        Call<VerifyStreamResult> streamCall = new VerifyStreamCall(streamReq);
        VerifyStreamResult streamResult = caller.call(streamCall);

        return new VerifyResult(streamResult.getScore(), streamResult.getDecision());
    }

}
