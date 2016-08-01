# **voicepin-flow-client**

This is java client library for Voicepin Flow also known as Voicepin Text-Independend Server.

## **Code examples**

### Setup
Setting up a client which connects to locally installed Flow Server using http without any additional security.
``` java
FlowClient flowClient = FlowClient.newBuilder("http://localhost:8081/voicepin-ti-server/v1/").build();
```

Setting up more advanced client which uses Basic-Auth and https using custom Keystore from file.
``` java
FlowClient flowClient = FlowClient.newBuilder("https://localhost:8443/voicepin-ti-server/v1/")
                                  .withBasicAuth("voicepin-client", "password")
                                  .withKeystore("/path/to/the/keystore", "password")
                                  .build();

```

By default client for all asynchronous operations uses threads from ForkJoinPool.commonPool(). If you plan to use this library in production ready environment we advise you to pass external Executor.

``` java
FlowClient flowClient = FlowClient.newBuilder("https://localhost:8443/voicepin-ti-server/v1/")
                                  .withExecutor(Executors.newFixedThreadPool(5))
                                  .withKeystore("/path/to/the/keystore", "password")
                                  .build();

```


### Creating voiceprint
Creating voiceprint and getting VoiceprintId from the response.
``` java
AddVoiceprintRequest request = new AddVoiceprintRequest();
AddVoiceprintResult result = flowClient.addVoiceprint(request);
// getting voiceprintId from from pojo object
String voiceprintId = addVoiceprintResult.getVoiceprintId();
```

### Getting information about Voiceprint
Getting information about voiceprint and checking if it is already enrolled.
``` java
GetVoiceprintRequest request = new GetVoiceprintRequest(voiceprintId);
GetVoiceprintResult result = flowClient.getVoiceprint(getVoiceprintRequest);
// checking if voiceprint is already enrolled
if (result.isEnrolled) {
    // react
}
```

### Enrolling voiceprint using file
Voicepin Flow for the moment allows enrollment using wave file only.
``` java
SpeechStream enrollStream = new SpeechStream(new FileInputStream("/path/to/recording.wav"))
EnrollRequest enrollRequest = new EnrollRequest(voiceprintId, enrollStream)
flowClient.enroll(enrollRequest)
```

### Performing verification
The sample below uses FileInputStream but it might be any other InputStream. Data is dynamically transfered to the Flow-Server using chunked-encoding as soon as it is available in the InputStream.
``` java
SpeechStream enrollStream = new SpeechStream(new FileInputStream("/path/to/recording.wav"))
VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, verifyStream);
// Verification is performed in another thread
VerificationProcess verificationProcess = client.verify(verifyRequest);

// which allows to checking results during that process
verificationProcess.getCurrentResult();
```

In case we want to react on error during process or to know when it ended we need to add additional listener to the process.

``` java
verificationProcess.addListener(new VerificationProcessListener() {

            @Override
            void onError(Throwable throwable) {
                // react on error
            }

            @Override
            void onSuccess(VerifyResult verifyResult) {
                // react on final result
            }
        })
```

