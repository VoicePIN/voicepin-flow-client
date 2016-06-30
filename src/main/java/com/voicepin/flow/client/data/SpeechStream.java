package com.voicepin.flow.client.data;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mckulpa
 */
public class SpeechStream extends InputStream {

    private final InputStream underlyingInputStream;

    public SpeechStream(InputStream inputStream) {
        this.underlyingInputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return underlyingInputStream.read();
    }

}
