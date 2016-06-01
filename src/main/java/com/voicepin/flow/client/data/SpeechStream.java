package com.voicepin.flow.client.data;

import java.io.InputStream;

/**
 * @author mckulpa
 */
public class SpeechStream extends InputStreamWrapper {
    public SpeechStream(InputStream inputStream) {
        super(inputStream);
    }

    public SpeechStream(byte[] data) {
        super(data);
    }
}
