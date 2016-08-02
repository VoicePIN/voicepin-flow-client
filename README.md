# **voicepin-flow-client**

This is java client library for VoicePIN Flow also known as VoicePIN Text-Independent Server.

## **Code examples**

### Setup
Setting up a client which connects to locally installed Flow Server using HTTP without any additional security.
``` java
FlowClient flowClient = FlowClient.newBuilder("http://localhost:8081/voicepin-ti-server/v1/").build();
```

Setting up more advanced client which uses Basic-Auth and HTTPS using custom Keystore from file.
``` java
FlowClient flowClient = FlowClient.newBuilder("https://localhost:8443/voicepin-ti-server/v1/")
                                  .withBasicAuth("voicepin-client", "password")
                                  .withKeystore("/path/to/the/keystore", "keystorePassword")
                                  .build();

```

By default the client uses threads from ForkJoinPool.commonPool() for all asynchronous operations. If you plan to use this library in production environment we advise you to pass an external Executor.

``` java
FlowClient flowClient = FlowClient.newBuilder("https://localhost:8443/voicepin-ti-server/v1/")
                                  .withExecutor(Executors.newFixedThreadPool(5))
                                  .withKeystore("/path/to/the/keystore", "keystorePassword")
                                  .build();

```


### Creating Voiceprint
Creating Voiceprint and getting VoiceprintId from the response.
``` java
AddVoiceprintResult result = flowClient.addVoiceprint();
String voiceprintId = result.getVoiceprintId();
```

### Getting information about Voiceprint
Getting information about Voiceprint and checking if it is already enrolled.
``` java
GetVoiceprintRequest request = new GetVoiceprintRequest(voiceprintId);
GetVoiceprintResult result = flowClient.getVoiceprint(request);
// checking if Voiceprint is already enrolled
if (result.isEnrolled()) {
    // react
}
```

### Enrolling Voiceprint using file
VoicePIN Flow for the moment allows enrollment using stream in wave format (wave headers must be present).
``` java
// using a wave file on local filesystem (please note that this might also be a real-time stream as long as wave headers are provided)
SpeechStream enrollStream = new SpeechStream(new FileInputStream("/path/to/recording.wav"));
EnrollRequest enrollRequest = new EnrollRequest(voiceprintId, enrollStream);
flowClient.enroll(enrollRequest);
```

### Performing verification
The example below uses FileInputStream but it might be any other InputStream. Data is dynamically transferred to the Flow Server using chunked Transfer-Encoding as soon as it is available in the InputStream.
``` java
SpeechStream verifyStream = new SpeechStream(new FileInputStream("/path/to/recording.wav"));
VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream);
// Verification is performed in another thread
VerificationProcess verificationProcess = flowClient.verify(verifyRequest);

// which allows to check results during that process
verificationProcess.getCurrentResult();
```

In case we want to react on an error or to know when verification finished we need to add additional listener to the process.

``` java
verificationProcess.addListener(new VerificationProcessListener() {
    @Override
    public void onError(Throwable throwable) {
        // react on error
    }

    @Override
    public void onSuccess(VerifyResult verifyResult) {
        // react on final result
    }
});
```
