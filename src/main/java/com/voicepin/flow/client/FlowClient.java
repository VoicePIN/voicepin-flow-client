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
import com.voicepin.flow.client.ssl.CustomKeystoreConnectionHelper;
import com.voicepin.flow.client.ssl.DefaultConnectionHelper;
import com.voicepin.flow.client.ssl.SecureConnectionHelper;
import com.voicepin.flow.client.ssl.UnsafeConnectionHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kodrzywolek, Lukasz Warzecha
 */
public class FlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowClient.class);

    private final Caller caller;

    private FlowClient(FlowClientBuilder builder) {
        if (builder.username != null) {
            this.caller = new Caller(builder.baseUrl, builder.username, builder.password, builder.connectionHelper);
        } else {
            this.caller = new Caller(builder.baseUrl);
        }
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

    /**
     * Creates builder for FlowClient.
     * 
     * @param baseUrl url to the service eg.
     *            http://flow.voicepin.com/voicepin-ti-server/v1/
     *
     * @return
     */
    public static FlowClientBuilder newBuilder(String baseUrl) {
        return new FlowClientBuilder(baseUrl);
    }

    public static final class FlowClientBuilder {

        private final String baseUrl;

        private String username;
        private String password;

        private SecureConnectionHelper connectionHelper = new DefaultConnectionHelper();

        private FlowClientBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        /**
         * Allows to connect to the Voicepin Flow server using secure http connection with
         * given credentials.
         * 
         * @param username
         * @param password
         *
         * @return
         */
        public FlowClientBuilder withHttps(String username, String password) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username is null or empty");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password is null or empty");
            }

            this.username = username;
            this.password = password;
            this.connectionHelper = new DefaultConnectionHelper();
            return this;
        }

        /**
         * Allows Flow Client to trust all https certificates. Use with caution and only
         * during development.
         * 
         * @return
         */
        public FlowClientBuilder acceptAllCertificates() {
            this.connectionHelper = new UnsafeConnectionHelper();
            return this;
        }

        /**
         * Allows to use custom self-signed certificate.
         * 
         * @param keystorePath path on the disc to the keystore file
         * @param keystorePassword password to the keystore
         *
         * @return
         */
        public FlowClientBuilder withKeystore(String keystorePath, String keystorePassword) {
            this.connectionHelper = new CustomKeystoreConnectionHelper(keystorePath, keystorePassword);
            return this;
        }

        public FlowClient build() {
            return new FlowClient(this);
        }
    }

}
