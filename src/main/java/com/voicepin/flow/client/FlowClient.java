package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.AddVoiceprintCall;
import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.EnrollCall;
import com.voicepin.flow.client.calls.VerifyInitCall;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.AddVoiceprintRequest;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.request.VerifyRequest;
import com.voicepin.flow.client.result.AddVoiceprintResult;
import com.voicepin.flow.client.result.EnrollResult;
import com.voicepin.flow.client.result.VerifyInitResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author kodrzywolek, Lukasz Warzecha
 */
public class FlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowClient.class);
    private final Caller caller;

    public FlowClient(String baseURL) {
        caller = new Caller(baseURL);
    }

    public FlowClient(String baseURL, String username, String password)
            throws KeyManagementException, NoSuchAlgorithmException {
        caller = new Caller(baseURL, username, password);
    }

    public AddVoiceprintResult addVoiceprint(AddVoiceprintRequest addVoiceprintRequest) throws FlowClientException {
        Call<AddVoiceprintResult> call = new AddVoiceprintCall(addVoiceprintRequest);
        return caller.call(call);
    }

    public EnrollResult enroll(EnrollRequest enrollRequest) throws FlowClientException {
        Call<EnrollResult> call = new EnrollCall(enrollRequest);
        return caller.call(call);
    }

    public VerifyStreamClient verify(VerifyRequest verifyRequest) throws FlowClientException {

        VerifyInitRequest initReq = new VerifyInitRequest(verifyRequest.getVoiceprintId());
        Call<VerifyInitResult> initCall = new VerifyInitCall(initReq);
        VerifyInitResult initResult = caller.call(initCall);

        return new VerifyStreamClient(caller, initResult, verifyRequest.getSpeechStream());
    }

}
