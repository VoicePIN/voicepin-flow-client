package com.voicepin.flow.client

import spock.lang.Specification

import com.voicepin.flow.client.data.SpeechStream
import com.voicepin.flow.client.request.AddVoiceprintRequest
import com.voicepin.flow.client.request.EnrollRequest
import com.voicepin.flow.client.request.GetVoiceprintRequest
import com.voicepin.flow.client.request.VerifyRequest
import com.voicepin.flow.client.result.AddVoiceprintResult
import com.voicepin.flow.client.result.GetVoiceprintResult
import com.voicepin.flow.client.result.VerifyResult

class FlowClientIT extends Specification {

    FlowClient client

    def setup() {
        def url = "http://localhost:8081/voicepin-ti-server/v1/"
        client = FlowClient.newBuilder(url).build();

//        def url = "https://localhost:8443/voicepin-ti-server/v1/"
//        client = FlowClient.newBuilder(url)
//                .withHttps("test_deployment", "qwer")
//                .withKeystore("/home/sch/voicepin.jks", "changeit")
//                .build();
    }

    def "voiceprint lifecycle"() {
        given:
        AddVoiceprintRequest request = new AddVoiceprintRequest()
        def enrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_1.wav"))

        when: "adding voiceprint"
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint(request)

        then: "new voiceprint ID is returned"
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        voiceprintId != null

        when: "enrolling voiceprint"
        EnrollRequest enrollRequest = new EnrollRequest(voiceprintId, enrollStream)
        client.enroll(enrollRequest)

        then: "OK is returned"
        notThrown(Exception)

        when: "getting voiceprint"
        GetVoiceprintRequest getVoiceprintRequest = new GetVoiceprintRequest(voiceprintId)
        GetVoiceprintResult getVoiceprintResult = client.getVoiceprint(getVoiceprintRequest)

        then: "voiceprint is enrolled"
        println getVoiceprintResult
        getVoiceprintResult.getId().equals(voiceprintId)
        getVoiceprintResult.isEnrolled()

        when: "verifying voiceprint"
        def verifyStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_2.wav"))
        VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        VerifyStreamClient streamClient = client.verify(verifyRequest)

        boolean isRunning = true;
        def finalResult;
        streamClient.addListener(new VerifyListener() {

            @Override
            void onError(Throwable throwable) {
                isRunning = false;
            }

            @Override
            void onSuccess(VerifyResult verifyResult) {
                isRunning = false;
                finalResult = verifyResult;
            }
        })

        while (isRunning) {
            VerifyResult verifyResult = streamClient.getCurrentResult();
            println verifyResult
            Thread.sleep(1000);
        }

        then: "final decision is returned"
        finalResult != null
        finalResult.getDecision() != null
        finalResult.getScore() != null
        println "decision: " + finalResult.getDecision()
        println "score: " + finalResult.getScore()
    }

}