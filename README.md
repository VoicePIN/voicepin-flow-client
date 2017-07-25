# **voicepin-flow-client**

This is java client library for VoicePIN Flow also known as VoicePIN Text-Independent Server.

## Gradle dependency

Add VoicePIN.com Maven repository to `build.gradle`:

    repositories {
        maven { url 'https://nexus.voicepin.com/repository/maven-releases'}
    }

Add voicepin-flow-client dependency:

    dependencies {
        compile 'com.voicepin.flow:voicepin-flow-client:1.0.0'
    }

## Maven dependency

Add VoicePIN.com Maven repository to `pom.xml`:

    <project>
        ...
        <repositories>
            <repository>
                <id>VoicePIN.com</id>
                <url>https://nexus.voicepin.com/repository/maven-releases</url>
            </repository>
        </repositories>
    </project>

Add voicepin-flow-client dependency:

    <project>
        ...
        <dependencies>
            ...
            <dependency>
                <groupId>com.voicepin.flow</groupId>
                <artifactId>voicepin-flow-client</artifactId>
                <version>1.0.0</version>
            </dependency>
        </dependencies>
    </project>


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



### Performing Enrollment or Verification
Both processes are similar in usage. The example below uses FileInputStream but it might be any other InputStream. Data is dynamically transferred to the Flow Server using chunked Transfer-Encoding as soon as it is available in the InputStream.
``` java
SpeechStream speechStream = new SpeechStream(new FileInputStream("/path/to/recording.wav"));
VerifyRequest verifyRequest = new VerifyRequest(voiceprintId, speechStream);
// Verification is performed in another thread
VerificationProcess verificationProcess = flowClient.verify(verifyRequest);

// to check current result
verificationProcess.getCurrentResult();

// to wait for the final result and block current Thread
verificationProcess.getFinalResult();

// to react asynchronously to the final result
verificationProcess.getFinalResultAsync().thenAccept(e -> {
    // do something
})
```



