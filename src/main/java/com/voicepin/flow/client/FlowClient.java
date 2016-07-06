package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.AddVoiceprintCall;
import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.EnrollCall;
import com.voicepin.flow.client.calls.GetVoiceprintCall;
import com.voicepin.flow.client.calls.VerifyInitCall;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.request.AddVoiceprintRequest;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.request.GetVoiceprintRequest;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.request.VerifyRequest;
import com.voicepin.flow.client.result.AddVoiceprintResult;
import com.voicepin.flow.client.result.EnrollResult;
import com.voicepin.flow.client.result.GetVoiceprintResult;
import com.voicepin.flow.client.result.VerifyInitResult;
import com.voicepin.flow.client.ssl.PinnedCertificateStrategy;
import com.voicepin.flow.client.ssl.TrustedCertificateStrategy;
import com.voicepin.flow.client.ssl.CertificateStrategy;
import com.voicepin.flow.client.ssl.AnyCertificateStrategy;

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

    /**
     * Creates new Voiceprint, which is represented by UUID.
     * 
     * @param addVoiceprintRequest
     * @return assigned voiceprint id
     * @throws FlowClientException
     */
    public AddVoiceprintResult addVoiceprint(AddVoiceprintRequest addVoiceprintRequest) throws FlowClientException {
        Call<AddVoiceprintResult> call = new AddVoiceprintCall(addVoiceprintRequest);
        return caller.call(call);
    }

    /**
     * Gets information about Voiceprint state.
     * 
     * @param getVoiceprintRequest voiceprint id
     * @return voiceprint state
     * @throws FlowClientException
     */
    public GetVoiceprintResult getVoiceprint(GetVoiceprintRequest getVoiceprintRequest) throws FlowClientException {
        Call<GetVoiceprintResult> call = new GetVoiceprintCall(getVoiceprintRequest);
        return caller.call(call);
    }

    /**
     * Creates new voice model using streamed recording. Sets it for this Voiceprint. The model will be used as a
     * reference in any subsequent verifications. If the Voiceprint has already been enrolled, its preexisting model
     * would be overwritten.
     * 
     * @param enrollRequest request with voiceprint id and recording stream
     * @return
     * @throws FlowClientException
     */
    public EnrollResult enroll(EnrollRequest enrollRequest) throws FlowClientException {
        Call<EnrollResult> call = new EnrollCall(enrollRequest);
        return caller.call(call);
    }

    /**
     * Starts verification process on given Voiceprint with incoming stream. Verification process starts immediately
     * (i.e. it does not wait for the whole stream to be uploaded).
     *
     * @param verifyRequest
     * @return result client for getting results
     * @throws FlowClientException
     */
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

        private CertificateStrategy connectionHelper = new TrustedCertificateStrategy();

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
            this.connectionHelper = new TrustedCertificateStrategy();
            return this;
        }

        /**
         * Allows Flow Client to trust all https certificates. Use with caution and only
         * during development.
         * 
         * @return
         */
        public FlowClientBuilder acceptAllCertificates() {
            this.connectionHelper = new AnyCertificateStrategy();
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
            this.connectionHelper = new PinnedCertificateStrategy(keystorePath, keystorePassword);
            return this;
        }

        public FlowClient build() {
            return new FlowClient(this);
        }
    }

}
