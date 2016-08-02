package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.AddVoiceprintCall;
import com.voicepin.flow.client.calls.Call;
import com.voicepin.flow.client.calls.EnrollCall;
import com.voicepin.flow.client.calls.GetVoiceprintCall;
import com.voicepin.flow.client.calls.VerifyInitCall;
import com.voicepin.flow.client.exception.AudioTooShortException;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.exception.FlowConnectionException;
import com.voicepin.flow.client.exception.InvalidAudioException;
import com.voicepin.flow.client.exception.VoiceprintNotEnrolledException;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.request.GetVoiceprintRequest;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.request.VerifyRequest;
import com.voicepin.flow.client.result.AddVoiceprintResult;
import com.voicepin.flow.client.result.EnrollResult;
import com.voicepin.flow.client.result.GetVoiceprintResult;
import com.voicepin.flow.client.result.VerifyInitResult;
import com.voicepin.flow.client.ssl.AnyCertificateStrategy;
import com.voicepin.flow.client.ssl.CertificateStrategy;
import com.voicepin.flow.client.ssl.PinnedCertificateStrategy;
import com.voicepin.flow.client.ssl.TrustedCertificateStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Client for VoicePIN Flow REST API (Text Independent Biometric Voice Verification
 * server)
 * 
 * @author kodrzywolek, Lukasz Warzecha
 */
public final class FlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowClient.class);

    private final Caller caller;

    private FlowClient(FlowClientBuilder builder) {
        this.caller = new Caller(builder);
    }

    /**
     * Creates new Voiceprint. Returned ID should be passed to any subsequent operations
     * on this Voiceprint (i.e. enrollment/verification).
     *
     * @return assigned Voiceprint ID
     *
     * @throws FlowConnectionException if could not establish connection with Flow Server
     */
    public AddVoiceprintResult addVoiceprint() throws FlowClientException {
        Call<AddVoiceprintResult> call = new AddVoiceprintCall();
        return caller.call(call);
    }

    /**
     * Gets information about Voiceprint state.
     * 
     * @param getVoiceprintRequest Voiceprint ID
     * @return voiceprint state
     *
     * @throws FlowConnectionException if could not establish connection with Flow Server
     */
    public GetVoiceprintResult getVoiceprint(GetVoiceprintRequest getVoiceprintRequest) throws FlowClientException {
        Call<GetVoiceprintResult> call = new GetVoiceprintCall(getVoiceprintRequest);
        return caller.call(call);
    }

    /**
     * Creates new voice model using streamed recording. Sets it for this Voiceprint. The
     * model will be used as a reference in any subsequent verifications. If the
     * Voiceprint has already been enrolled, its preexisting model would be overwritten.
     * 
     * @param enrollRequest request with Voiceprint ID and recording stream
     *
     * @return
     *
     * @throws InvalidAudioException if given audio is incorrect
     * @throws AudioTooShortException if given audio is too short
     *
     * @throws FlowConnectionException if could not establish connection with Flow Server
     */
    public EnrollResult enroll(EnrollRequest enrollRequest) throws FlowClientException {
        Call<EnrollResult> call = new EnrollCall(enrollRequest);
        return caller.call(call);
    }

    /**
     * Starts verification process on given Voiceprint with provided speech stream.
     * Verification starts immediately (i.e. it does not wait for the whole stream to be
     * sent). The process is finished when the stream ends so in real-time verification it
     * is crucial to listen to verification scores and end the stream when satisfying
     * results are achieved instead of waiting for the final result.
     * 
     * @param verifyRequest
     *
     * @return
     *
     * @throws VoiceprintNotEnrolledException if Voiceprint is not enrolled
     * @throws InvalidAudioException if given audio is incorrect
     * @throws AudioTooShortException if given audio is too short
     *
     * @throws FlowConnectionException if could not establish connection with Flow Server
     */
    public VerificationProcess verify(VerifyRequest verifyRequest) throws FlowClientException {

        VerifyInitRequest initReq = new VerifyInitRequest(verifyRequest.getVoiceprintId());
        Call<VerifyInitResult> initCall = new VerifyInitCall(initReq);
        VerifyInitResult initResult = caller.call(initCall);

        return new VerificationProcess(caller, initResult, verifyRequest.getSpeechStream());
    }

    /**
     * Creates builder for FlowClient.
     * 
     * @param baseUrl url to the service eg. http://localhost:8081/voicepin-ti-server/v1/
     *
     * @return
     */
    public static FlowClientBuilder newBuilder(String baseUrl) {
        return new FlowClientBuilder(baseUrl);
    }

    public static final class FlowClientBuilder {

        final String baseUrl;

        String username;
        String password;

        CertificateStrategy certificateStrategy;

        Executor executor = ForkJoinPool.commonPool();

        private FlowClientBuilder(String baseUrl) {
            this.baseUrl = baseUrl;
            if (baseUrl.contains("https://")) {
                this.certificateStrategy = new TrustedCertificateStrategy();
            }

        }

        /**
         * Allows to connect to the VoicePIN Flow server using HTTPS connection with given
         * credentials
         * 
         * @param username
         * @param password
         *
         * @return
         */
        public FlowClientBuilder withBasicAuth(String username, String password) {
            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username is null or empty");
            }

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password is null or empty");
            }

            this.username = username;
            this.password = password;
            return this;
        }

        /**
         * Allows Flow Client to trust all HTTPS certificates. Use with caution and only
         * during development.
         *
         * @return
         */
        public FlowClientBuilder acceptAllCertificates() {
            this.certificateStrategy = new AnyCertificateStrategy();
            return this;
        }

        /**
         * Allows certificate pinning - only certificates in passed key store will be
         * accepted (self-signed included).
         * 
         * @param keystorePath path on the disc to the keystore file
         * @param keystorePassword password to the keystore
         *
         * @return
         */
        public FlowClientBuilder withKeystore(String keystorePath, String keystorePassword) {
            this.certificateStrategy = new PinnedCertificateStrategy(keystorePath, keystorePassword);
            return this;
        }

        /**
         * Allows to pass custom executor that will run all asynchronous tasks in
         * FlowClient. By default it uses {@link ForkJoinPool#commonPool()}
         * 
         * @param executor
         *
         * @return
         */
        public FlowClientBuilder withExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public FlowClient build() {
            return new FlowClient(this);
        }
    }

}
