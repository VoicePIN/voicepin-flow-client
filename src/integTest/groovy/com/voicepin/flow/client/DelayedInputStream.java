package com.voicepin.flow.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lukasz Warzecha
 */
public class DelayedInputStream extends InputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayedInputStream.class);

    private final InputStream inputStream;
    private int counter = 0;

    public DelayedInputStream(final InputStream stream) {
        this.inputStream = stream;
    }

    @Override
    public int read() throws IOException {
        final int data = inputStream.read();
        if (counter % 100000 == 0) {
            try {
                Thread.sleep(1500);
            } catch (final InterruptedException e) {
                LOGGER.error("", e);
            }
        }
        counter++;
        return data;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
