package com.voicepin.flow.client

import java.util.function.Consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import com.voicepin.flow.client.data.SpeechStream
import com.voicepin.flow.client.exception.AudioTooShortException
import com.voicepin.flow.client.request.EnrollRequest
import com.voicepin.flow.client.request.GetVoiceprintRequest
import com.voicepin.flow.client.request.VerifyRequest
import com.voicepin.flow.client.result.AddVoiceprintResult
import com.voicepin.flow.client.result.EnrollStatus
import com.voicepin.flow.client.result.GetVoiceprintResult
import com.voicepin.flow.client.result.VerifyResult

class FlowClientIT extends Specification {

    Logger LOGGER = LoggerFactory.getLogger(FlowClientIT.class);

    FlowClient client

    def setup() {
        def url = "http://localhost:8081/voicepin-ti-server/v1/"
        client = FlowClient.newBuilder(url).build();
    }

    def "voiceprint lifecycle"() {
        given:
        def enrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_1.wav"))

        when: "adding voiceprint"
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()

        then: "new voiceprint ID is returned"
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        voiceprintId != null

        when: "enrolling voiceprint"
        EnrollRequest enrollRequest = new EnrollRequest(voiceprintId, enrollStream)
        client.enroll(enrollRequest).getFinalStatus();

        then: "OK is returned"
        notThrown(Exception)

        when: "getting voiceprint"
        GetVoiceprintRequest getVoiceprintRequest = new GetVoiceprintRequest(voiceprintId)
        GetVoiceprintResult getVoiceprintResult = client.getVoiceprint(getVoiceprintRequest)

        then: "voiceprint is enrolled"
        LOGGER.info("Getting voiceprint result [{}]", getVoiceprintResult)
        getVoiceprintResult.getId().equals(voiceprintId)
        getVoiceprintResult.isEnrolled()

        when: "verifying voiceprint and getting results during streaming"
        SpeechStream verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")));

        VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        VerificationProcess streamClient = client.verify(verifyRequest)

        while (!streamClient.future.isDone()) {
            VerifyResult verifyResult = streamClient.getCurrentResult();
            LOGGER.info("Current result {}", verifyResult)
            Thread.sleep(100);
        }

        VerifyResult finalResult = streamClient.getFinalResult();

        then: "final decision is returned"
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        LOGGER.info("Final result [{}]", finalResult)

        when: "verifying voiceprint and getting only final result"
        verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")));

        verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        streamClient = client.verify(verifyRequest)
        finalResult = streamClient.getFinalResult();

        then: "final decision is returned"
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        LOGGER.info("Final result [{}]", finalResult)

        when: "verifying voiceprint and getting final result asynchronously"
        verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")));

        verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        streamClient = client.verify(verifyRequest)
        finalResult = null

        streamClient.getFuture().thenAccept(new Consumer<VerifyResult>() {

            @Override
            void accept(VerifyResult verifyResult) {
                finalResult = verifyResult;
            }
        })

        while (!streamClient.future.isDone()) {
            Thread.sleep(1000)
        }

        then: "final decision is returned"
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        LOGGER.info("Final result [{}]", finalResult)
    }

    def "enrolling voiceprint"() {
        given:
        def enrollStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_1.wav")))
        def shortEnrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/short.wav"))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, enrollStream)
        when: "enrolling with correct stream"
        EnrollmentProcess enrollmentProcess = client.enroll(req)

        while (!enrollmentProcess.getFuture().isDone()) {
            EnrollStatus status = enrollmentProcess.getCurrentStatus();
            LOGGER.info("{}", status)
            Thread.sleep(100)
        }
        EnrollStatus result = enrollmentProcess.finalStatus
        then:
        LOGGER.info("{}", result)
        result != null
        result.getProgress() > 100
    }

    def "enrolling with too short audio should throw AudioTooShortException"() {
        given:
        def shortEnrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/short_audio.wav"))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, shortEnrollStream)
        when: "enrolling with correct stream"
        EnrollmentProcess enrollmentProcess = client.enroll(req)
        EnrollStatus result = enrollmentProcess.getFinalStatus()
        then:
        thrown(AudioTooShortException.class)
    }

}