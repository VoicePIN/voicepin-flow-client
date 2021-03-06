package com.voicepin.flow.client;

import com.voicepin.flow.client.calls.*;
import com.voicepin.flow.client.exception.IncorrectAudioInputException;
import com.voicepin.flow.client.exception.FlowClientException;
import com.voicepin.flow.client.exception.FlowConnectionException;
import com.voicepin.flow.client.exception.VoiceprintNotEnrolledException;
import com.voicepin.flow.client.request.EnrollInitRequest;
import com.voicepin.flow.client.request.EnrollRequest;
import com.voicepin.flow.client.request.GetVoiceprintRequest;
import com.voicepin.flow.client.request.VerifyInitRequest;
import com.voicepin.flow.client.request.VerifyRequest;
import com.voicepin.flow.client.result.*;
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
public class FlowClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowClient.class);

    private final Caller caller;

    private FlowClient(FlowClientBuilder builder) {
        this.caller = new Caller(builder);
    }

    /**
     * Gets current configuration of VoicePIN Flow server.
     *
     * @return configuration of VoicePIN Flow server
     * @throws FlowClientException
     */
    public GetConfigurationResult getConfiguration() throws FlowClientException {
        Call<GetConfigurationResult> call = new GetConfigurationCall();
        return caller.call(call);
    }

    /**
     * Creates new Voiceprint. Returned ID should be passed to ay subsequent operations on
     * this Voiceprint (i.e. enrollment/verification).
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
     * 
     * 
     * Starts enrollment process on given Voiceprint with provided speech stream.
     * Enrollment starts immediately (i.e. it does not wait for the whole stream to be
     * sent). The process is finished when user stops streaming so in real-time enrollment
     * it is crucial to listen to current enrollment status.
     *
     * Succesfull enrollment will result in creating new voice model which will be
     * associated with given Voiceprint. The model will be used as a reference in any
     * subsequent verifications. If the Voiceprint has already been enrolled, its
     * preexisting model would be overwritten.
     * 
     * @param enrollRequest request with Voiceprint ID and recording stream
     *
     * @return
     *
     * @throws IncorrectAudioInputException if given audio is incorrect
     *
     * @throws FlowConnectionException if could not establish connection with Flow Server
     */
    public EnrollmentProcess enroll(EnrollRequest enrollRequest) throws FlowClientException {

        EnrollInitRequest initReq = new EnrollInitRequest(enrollRequest.getVoiceprintId());
        Call<EnrollInitResult> initCall = new EnrollInitCall(initReq);
        EnrollInitResult initResult = caller.call(initCall);

        return new EnrollmentProcess(caller, initResult, enrollRequest.getSpeechStream());
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
     * @throws IncorrectAudioInputException if given audio is incorrect
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
