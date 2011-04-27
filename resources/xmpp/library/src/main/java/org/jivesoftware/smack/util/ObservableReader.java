/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jivesoftware.smack.util;

import java.io.*;
import java.util.*;

/**
 * An ObservableReader is a wrapper on a Reader that notifies to its listeners when
 * reading character streams.
 * 
 * @author Gaston Dombiak
 */
public class ObservableReader extends Reader {

    Reader wrappedReader = null;
    List listeners = new ArrayList();

    public ObservableReader(Reader wrappedReader) {
        this.wrappedReader = wrappedReader;
    }
        
    public int read(char[] cbuf, int off, int len) throws IOException {
        int count = wrappedReader.read(cbuf, off, len);
        if (count > 0) {
            String str = new String(cbuf, off, count);
            // Notify that a new string has been read
            ReaderListener[] readerListeners = null;
            synchronized (listeners) {
                readerListeners = new ReaderListener[listeners.size()];
                listeners.toArray(readerListeners);
            }
            for (int i = 0; i < readerListeners.length; i++) {
                readerListeners[i].read(str);
            }
        }
        return count;
    }

    public void close() throws IOException {
        wrappedReader.close();
    }

    public int read() throws IOException {
        return wrappedReader.read();
    }

    public int read(char cbuf[]) throws IOException {
        return wrappedReader.read(cbuf);
    }

    public long skip(long n) throws IOException {
        return wrappedReader.skip(n);
    }

    public boolean ready() throws IOException {
        return wrappedReader.ready();
    }

    public boolean markSupported() {
        return wrappedReader.markSupported();
    }

    public void mark(int readAheadLimit) throws IOException {
        wrappedReader.mark(readAheadLimit);
    }

    public void reset() throws IOException {
        wrappedReader.reset();
    }

    /**
     * Adds a reader listener to this reader that will be notified when
     * new strings are read.
     *
     * @param readerListener a reader listener.
     */
    public void addReaderListener(ReaderListener readerListener) {
        if (readerListener == null) {
            return;
        }
        synchronized (listeners) {
            if (!listeners.contains(readerListener)) {
                listeners.add(readerListener);
            }
        }
    }

    /**
     * Removes a reader listener from this reader.
     *
     * @param readerListener a reader listener.
     */
    public void removeReaderListener(ReaderListener readerListener) {
        synchronized (listeners) {
            listeners.remove(readerListener);
        }
    }

}
