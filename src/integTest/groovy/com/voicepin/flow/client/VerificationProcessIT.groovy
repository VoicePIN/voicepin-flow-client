package com.voicepin.flow.client

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.voicepin.flow.client.data.SpeechStream
import com.voicepin.flow.client.request.VerifyRequest
import com.voicepin.flow.client.result.FinalVerifyResult
import com.voicepin.flow.client.result.VerifyResult
import org.junit.Rule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

class VerificationProcessIT extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(18081)

    Logger LOGGER = LoggerFactory.getLogger(FlowClientIT.class)

    FlowClient client

    def setup() {
        def url = "http://localhost:18081/voicepin-ti-server/v1/"
        client = FlowClient.newBuilder(url).build()
    }

    def "Verification can return different partial statuses"() {
        given:
        String voiceprintId = "123"
        String sessionId = "e14cc0c7-f180-451e-b852-71e0e74690a3"
        String scenarioName = "Verify"
        wireMockRule.stubFor(post(urlEqualTo("/voicepin-ti-server/v1/voiceprint/${voiceprintId}/verification"))
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "speechPath": "verification/${sessionId}/speech",
                        "resultPath": "verification/${sessionId}/result"
                    }""")))

        wireMockRule.stubFor(put(urlEqualTo("/voicepin-ti-server/v1/verification/${sessionId}/speech"))
                .willReturn(aResponse()
                .withFixedDelay(2000)
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "decision": "MATCH",
                        "score": 100,
                        "blacklistScore": 73
                    }""")))

        wireMockRule.stubFor(get(urlEqualTo("/voicepin-ti-server/v1/verification/${sessionId}/result"))
                .inScenario(scenarioName)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "status": "PENDING"
                    }"""))
                .willSetStateTo("MISMATCH"))

        wireMockRule.stubFor(get(urlEqualTo("/voicepin-ti-server/v1/verification/${sessionId}/result"))
                .inScenario(scenarioName)
                .whenScenarioStateIs("MISMATCH")
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "status": "MISMATCH",
                        "score": -10,
                        "blacklistScore": 5
                    }"""))
                .willSetStateTo("BLACKLIST_FRAUD"))

        wireMockRule.stubFor(get(urlEqualTo("/voicepin-ti-server/v1/verification/${sessionId}/result"))
                .inScenario(scenarioName)
                .whenScenarioStateIs("BLACKLIST_FRAUD")
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "status": "BLACKLIST_FRAUD",
                        "score": 40,
                        "blacklistScore": 70
                    }"""))
                .willSetStateTo("MATCH"))

        wireMockRule.stubFor(get(urlEqualTo("/voicepin-ti-server/v1/verification/${sessionId}/result"))
                .inScenario(scenarioName)
                .whenScenarioStateIs("MATCH")
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""{
                        "status": "MATCH",
                        "score": 99,
                        "blacklistScore": 0
                    }""")))

        SpeechStream verifyStream = new SpeechStream(
                new DelayedInputStream(getClass().getResourceAsStream("/recordings/record_2.wav")))
        VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream)

        when: "State is PENDING"
        VerificationProcess streamClient = client.verify(verifyRequest)
        VerifyResult verifyResult = streamClient.getCurrentResult()

        then: "partial status is PENDING"
        notThrown(Exception)
        verifyResult.getStatus() == "PENDING"
        verifyResult.getScore() == null
        verifyResult.getBlacklistScore() == null

        when: "State is MISMATCH"
        verifyResult = streamClient.getCurrentResult()

        then: "partial status is MISMATCH"
        notThrown(Exception)
        verifyResult.getStatus() == "MISMATCH"
        verifyResult.getScore() == -10
        verifyResult.getBlacklistScore() == 5

        when: "State is BLACKLIST_FRAUD"
        verifyResult = streamClient.getCurrentResult()

        then: "partial status is BLACKLIST_FRAUD"
        notThrown(Exception)
        verifyResult.getStatus() == "BLACKLIST_FRAUD"
        verifyResult.getScore() == 40
        verifyResult.getBlacklistScore() == 70

        when: "State is MATCH"
        FinalVerifyResult finalResult = streamClient.getFinalResult()
        verifyResult = streamClient.getCurrentResult()

        then: "partial status is MATCH"
        notThrown(Exception)
        verifyResult.getStatus() == "MATCH"
        verifyResult.getScore() == 99
        verifyResult.getBlacklistScore() == 0
        finalResult.getDecision() == "MATCH"
        finalResult.getScore() == 100
        finalResult.getBlacklistScore() == 73
        LOGGER.info("Final result [{}]", finalResult)
    }
}
