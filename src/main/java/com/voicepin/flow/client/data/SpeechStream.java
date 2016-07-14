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
    public boolean markSupported() {
        return underlyingInputStream.markSupported();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return underlyingInputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return underlyingInputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return underlyingInputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return underlyingInputStream.available();
    }

    @Override
    public void close() throws IOException {
        underlyingInputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        underlyingInputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        underlyingInputStream.reset();
    }

    @Override
    public int read() throws IOException {
        return underlyingInputStream.read();
    }

}
