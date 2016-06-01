package com.voicepin.flow.client.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mckulpa
 */
public class InputStreamWrapper extends InputStream {
    private InputStream underlyingInputStream;

    public InputStreamWrapper(InputStream inputStream) {
        this.underlyingInputStream = inputStream;
    }

    public InputStreamWrapper(byte[] data) {
        this.underlyingInputStream = new ByteArrayInputStream(data);
    }

    public InputStream getUnderlyingInputStream() {
        return underlyingInputStream;
    }

    @Override
    public int read() throws IOException {
        return underlyingInputStream.read();
    }
}
