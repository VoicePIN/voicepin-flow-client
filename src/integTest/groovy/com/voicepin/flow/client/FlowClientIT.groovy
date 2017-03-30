package com.voicepin.flow.client

import com.voicepin.flow.client.exception.FlowClientException
import com.voicepin.flow.client.result.GetConfigurationResult

import java.util.function.Consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import com.voicepin.flow.client.data.SpeechStream
import com.voicepin.flow.client.exception.FlowServerException
import com.voicepin.flow.client.exception.IncorrectAudioInputException
import com.voicepin.flow.client.request.EnrollRequest
import com.voicepin.flow.client.request.GetVoiceprintRequest
import com.voicepin.flow.client.request.VerifyRequest
import com.voicepin.flow.client.result.AddVoiceprintResult
import com.voicepin.flow.client.result.AudioIssue
import com.voicepin.flow.client.result.EnrollStatus
import com.voicepin.flow.client.result.FinalEnrollStatus
import com.voicepin.flow.client.result.FinalVerifyResult
import com.voicepin.flow.client.result.GetVoiceprintResult
import com.voicepin.flow.client.result.VerifyResult

class FlowClientIT extends Specification {

    Logger LOGGER = LoggerFactory.getLogger(FlowClientIT.class)

    FlowClient client

    def setup() {
        def url = "http://192.168.66.182:8081/voicepin-ti-server/v1/"
        client = FlowClient.newBuilder(url).build()
    }

    def "retrieves configuration"() {
        when: "getting configuration"
        GetConfigurationResult result = client.getConfiguration()

        then: "it contains all the expected fields"
        print result.getVerificationThreshold()
        result.getVerificationThreshold() > -100 && result.getVerificationThreshold() < 100
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
        client.enroll(enrollRequest).getFinalStatus()

        then: "exception is not thrown"
        notThrown(Exception)

        when: "getting voiceprint"
        GetVoiceprintRequest getVoiceprintRequest = new GetVoiceprintRequest(voiceprintId)
        GetVoiceprintResult getVoiceprintResult = client.getVoiceprint(getVoiceprintRequest)

        then: "voiceprint is enrolled"
        LOGGER.info("Getting voiceprint result [{}]", getVoiceprintResult)
        getVoiceprintResult.getId() == voiceprintId
        getVoiceprintResult.isEnrolled()

        when: "verifying voiceprint and getting results during streaming"
        SpeechStream verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")))

        VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        VerificationProcess streamClient = client.verify(verifyRequest)

        while (!streamClient.finalResultAsync.isDone()) {
            VerifyResult verifyResult = streamClient.getCurrentResult()
            LOGGER.info("Current result {}", verifyResult)
            Thread.sleep(500)
        }

        FinalVerifyResult finalResult = streamClient.getFinalResult()

        then: "final decision is returned"
        notThrown(Exception)
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        LOGGER.info("Final result [{}]", finalResult)

        when: "verifying voiceprint and getting only final result"
        verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")))

        verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        streamClient = client.verify(verifyRequest)
        finalResult = streamClient.getFinalResult()

        then: "final decision is returned"
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        LOGGER.info("Final result [{}]", finalResult)

        when: "verifying voiceprint and getting final result asynchronously"
        verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")))

        verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        streamClient = client.verify(verifyRequest)
        finalResult = null

        streamClient.getFinalResultAsync().thenAccept(new Consumer<FinalVerifyResult>() {

            @Override
            void accept(FinalVerifyResult verifyResult) {
                finalResult = verifyResult
            }
        })

        while (!streamClient.finalResultAsync.isDone()) {
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
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, enrollStream)

        when: "enrolling with correct stream"

        EnrollmentProcess enrollmentProcess = client.enroll(req)

        while (!enrollmentProcess.getFinalStatusAsync().isDone()) {
            try {
                EnrollStatus status = enrollmentProcess.getCurrentStatus()
                Thread.sleep(500)
                LOGGER.info("{}", status)
            } catch (FlowClientException e) {
                LOGGER.debug("", e)
            }
        }
        FinalEnrollStatus result = enrollmentProcess.finalStatus

        then: "progress is bigger than 100"

        LOGGER.info("{}", result)
        result != null
        result.getProgress() > 100
    }

    def "enrolling with too short audio throws IncorrectAudioInputException"() {
        given:
        def shortEnrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/short_audio.wav"))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, shortEnrollStream)

        when: "enrolling with speech stream containing too short audio for enrollment"
        EnrollmentProcess enrollmentProcess = client.enroll(req)
        enrollmentProcess.getFinalStatus()

        then: "operation should throw IncorrectAudioInputException"
        FlowServerException e = thrown(IncorrectAudioInputException.class)
        e.getFlowErrorMessage() == "Provided audio was too short for enrollment process"
    }

    def "enrolling with distorted audio throws IncorrectAudioInputException"() {
        given:
        def shortEnrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_1_distorted.wav"))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, shortEnrollStream)

        when: "enrolling with speech stream containing too short audio for enrollment"
        EnrollmentProcess enrollmentProcess = client.enroll(req)
        enrollmentProcess.getFinalStatus()

        then: "operation should throw IncorrectAudioInputException"
        FlowServerException e = thrown(IncorrectAudioInputException.class)
        e.getFlowErrorMessage() == "Audio is distorted"
    }

    def "enrolling voiceprint with distortedAudio returns DISTORTED flag"() {
        given:
        def enrollStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_1_distorted.wav")))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, enrollStream)

        when: "enrolling with correct stream"
        EnrollmentProcess enrollmentProcess = client.enroll(req)

        while (!enrollmentProcess.getFinalStatusAsync().isDone()) {
            EnrollStatus status = enrollmentProcess.getCurrentStatus()

            LOGGER.info("{}", status)
            Thread.sleep(500)

            if(status.getProgress()>0){
                assert status.audioIssues.contains(AudioIssue.DISTORTED)
            }
        }
        enrollmentProcess.finalStatus
        then: "IncorrectAudioInputException is thrown"
        FlowServerException e = thrown(IncorrectAudioInputException.class)
        e.getFlowErrorMessage() == "Audio is distorted"
    }

    def "updating enroll with impostor speech returns IMPOSTOR audioIssue"() {
        given: "enrolled voiceprint"
        def enrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_1.wav"))
        def impostorEnrollStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/impostor.wav")))
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint()
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        EnrollRequest req = new EnrollRequest(voiceprintId, enrollStream)
        EnrollmentProcess enrollmentProcess = client.enroll(req)
        enrollmentProcess.getFinalStatus()

        when: "performing enrollment update with speech of an impostor"
        req = new EnrollRequest(voiceprintId, impostorEnrollStream)
        enrollmentProcess = client.enroll(req)
        then: "audioIssues contain IMPOSTOR flag"
        boolean flaggedAsImpostor = false

        while (!enrollmentProcess.getFinalStatusAsync().isDone()) {
            EnrollStatus status = enrollmentProcess.getCurrentStatus()
            if (flaggedAsImpostor || status.audioIssues.contains(AudioIssue.IMPOSTOR)) {
                flaggedAsImpostor = true
                assert status.audioIssues.contains(AudioIssue.IMPOSTOR)
            }
            LOGGER.info("{}", status)
            Thread.sleep(500)
        }

        when: "trying to return finalStatus"
        enrollmentProcess.getFinalStatus()
        then: "IncorrectAudioInputException is thrown with Impostor"
        FlowServerException e = thrown(IncorrectAudioInputException.class)
        e.getFlowErrorMessage() == "Impostor detected"
    }


}