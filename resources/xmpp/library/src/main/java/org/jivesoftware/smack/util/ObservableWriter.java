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
 * An ObservableWriter is a wrapper on a Writer that notifies to its listeners when
 * writing to character streams.
 * 
 * @author Gaston Dombiak
 */
public class ObservableWriter extends Writer {

    Writer wrappedWriter = null;
    List listeners = new ArrayList();

    public ObservableWriter(Writer wrappedWriter) {
        this.wrappedWriter = wrappedWriter;
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        wrappedWriter.write(cbuf, off, len);
        String str = new String(cbuf, off, len);
        notifyListeners(str);
    }

    public void flush() throws IOException {
        wrappedWriter.flush();
    }

    public void close() throws IOException {
        wrappedWriter.close();
    }

    public void write(int c) throws IOException {
        wrappedWriter.write(c);
    }

    public void write(char cbuf[]) throws IOException {
        wrappedWriter.write(cbuf);
        String str = new String(cbuf);
        notifyListeners(str);
    }

    public void write(String str) throws IOException {
        wrappedWriter.write(str);
        notifyListeners(str);
    }

    public void write(String str, int off, int len) throws IOException {
        wrappedWriter.write(str, off, len);
        str = str.substring(off, off + len);
        notifyListeners(str);
    }

    /**
     * Notify that a new string has been written.
     * 
     * @param str the written String to notify 
     */
    private void notifyListeners(String str) {
        WriterListener[] writerListeners = null;
        synchronized (listeners) {
            writerListeners = new WriterListener[listeners.size()];
            listeners.toArray(writerListeners);
        }
        for (int i = 0; i < writerListeners.length; i++) {
            writerListeners[i].write(str);
        }
    }

    /**
     * Adds a writer listener to this writer that will be notified when
     * new strings are sent.
     *
     * @param writerListener a writer listener.
     */
    public void addWriterListener(WriterListener writerListener) {
        if (writerListener == null) {
            return;
        }
        synchronized (listeners) {
            if (!listeners.contains(writerListener)) {
                listeners.add(writerListener);
            }
        }
    }

    /**
     * Removes a writer listener from this writer.
     *
     * @param writerListener a writer listener.
     */
    public void removeWriterListener(WriterListener writerListener) {
        synchronized (listeners) {
            listeners.remove(writerListener);
        }
    }

}
