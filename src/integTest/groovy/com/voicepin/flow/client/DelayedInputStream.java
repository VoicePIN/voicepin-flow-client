package com.voicepin.flow.client;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Lukasz Warzecha
 */
public class DelayedInputStream extends InputStream {

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
                e.printStackTrace();
            }
        }
        counter++;
        return data;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n);
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }
}
