package com.voicepin.flow.client

import spock.lang.Specification

import com.voicepin.flow.client.data.SpeechStream
import com.voicepin.flow.client.request.AddVoiceprintRequest
import com.voicepin.flow.client.request.EnrollRequest
import com.voicepin.flow.client.request.VerifyRequest
import com.voicepin.flow.client.result.AddVoiceprintResult
import com.voicepin.flow.client.result.VerifyResult

class FlowClientIT extends Specification {

    FlowClient client

    def setup() {
        def url = "http://localhost:8081/voicepin-ti-server/v1/"
        client = new FlowClient(url)
    }

    def "voiceprint lifecycle"() {
        given:
        AddVoiceprintRequest request = new AddVoiceprintRequest()
        def enrollStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_1.wav"))

        when: " adding voiceprint"
        AddVoiceprintResult addVoiceprintResult = client.addVoiceprint(request)

        then: "new voiceprint id is returned"
        def voiceprintId = addVoiceprintResult.getVoiceprintId()
        voiceprintId != null

        when: "enrolling voiceprint"
        EnrollRequest enrollRequest = new EnrollRequest(voiceprintId, enrollStream)

        client.enroll(enrollRequest)
        then:
        notThrown(Exception)

        when: "verifying voiceprint"
        def verifyStream = new SpeechStream(getClass().getResourceAsStream("/recordings/record_2.wav"))

        VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream)
        VerifyResult verifyResult = client.verify(verifyRequest)

        then: "decision is returned"
        verifyResult.getDecision() != null
        verifyResult.getScore() != null
        println "decision: "+ verifyResult.getDecision()
        println "score: " + verifyResult.getScore()
    }

}