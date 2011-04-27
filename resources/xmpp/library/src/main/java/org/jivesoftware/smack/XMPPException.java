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

package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.StreamError;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A generic exception that is thrown when an error occurs performing an
 * XMPP operation. XMPP servers can respond to error conditions with an error code
 * and textual description of the problem, which are encapsulated in the XMPPError
 * class. When appropriate, an XMPPError instance is attached instances of this exception.<p>
 *
 * When a stream error occured, the server will send a stream error to the client before
 * closing the connection. Stream errors are unrecoverable errors. When a stream error
 * is sent to the client an XMPPException will be thrown containing the StreamError sent
 * by the server.
 *
 * @see XMPPError
 * @author Matt Tucker
 */
public class XMPPException extends Exception {

    private StreamError streamError = null;
    private XMPPError error = null;
    private Throwable wrappedThrowable = null;

    /**
     * Creates a new XMPPException.
     */
    public XMPPException() {
        super();
    }

    /**
     * Creates a new XMPPException with a description of the exception.
     *
     * @param message description of the exception.
     */
    public XMPPException(String message) {
        super(message);
    }

    /**
     * Creates a new XMPPException with the Throwable that was the root cause of the
     * exception.
     *
     * @param wrappedThrowable the root cause of the exception.
     */
    public XMPPException(Throwable wrappedThrowable) {
        super();
        this.wrappedThrowable = wrappedThrowable;
    }

    /**
     * Cretaes a new XMPPException with the stream error that was the root case of the
     * exception. When a stream error is received from the server then the underlying
     * TCP connection will be closed by the server.
     *
     * @param streamError the root cause of the exception.
     */
    public XMPPException(StreamError streamError) {
        super();
        this.streamError = streamError;
    }

    /**
     * Cretaes a new XMPPException with the XMPPError that was the root case of the
     * exception.
     *
     * @param error the root cause of the exception.
     */
    public XMPPException(XMPPError error) {
        super();
        this.error = error;
    }

    /**
     * Creates a new XMPPException with a description of the exception and the
     * Throwable that was the root cause of the exception.
     *
     * @param message a description of the exception.
     * @param wrappedThrowable the root cause of the exception.
     */
    public XMPPException(String message, Throwable wrappedThrowable) {
        super(message);
        this.wrappedThrowable = wrappedThrowable;
    }

    /**
     * Creates a new XMPPException with a description of the exception, an XMPPError,
     * and the Throwable that was the root cause of the exception.
     *
     * @param message a description of the exception.
     * @param error the root cause of the exception.
     * @param wrappedThrowable the root cause of the exception.
     */
    public XMPPException(String message, XMPPError error, Throwable wrappedThrowable) {
        super(message);
        this.error = error;
        this.wrappedThrowable = wrappedThrowable;
    }

    /**
     * Creates a new XMPPException with a description of the exception and the
     * XMPPException that was the root cause of the exception.
     *
     * @param message a description of the exception.
     * @param error the root cause of the exception.
     */
    public XMPPException(String message, XMPPError error) {
        super(message);
        this.error = error;
    }

    /**
     * Returns the XMPPError asscociated with this exception, or <tt>null</tt> if there
     * isn't one.
     *
     * @return the XMPPError asscociated with this exception.
     */
    public XMPPError getXMPPError() {
        return error;
    }

    /**
     * Returns the StreamError asscociated with this exception, or <tt>null</tt> if there
     * isn't one. The underlying TCP connection is closed by the server after sending the
     * stream error to the client.
     *
     * @return the StreamError asscociated with this exception.
     */
    public StreamError getStreamError() {
        return streamError;
    }

    /**
     * Returns the Throwable asscociated with this exception, or <tt>null</tt> if there
     * isn't one.
     *
     * @return the Throwable asscociated with this exception.
     */
    public Throwable getWrappedThrowable() {
        return wrappedThrowable;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        if (wrappedThrowable != null) {
            out.println("Nested Exception: ");
            wrappedThrowable.printStackTrace(out);
        }
    }

    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        if (wrappedThrowable != null) {
            out.println("Nested Exception: ");
            wrappedThrowable.printStackTrace(out);
        }
    }

    public String getMessage() {
        String msg = super.getMessage();
        // If the message was not set, but there is an XMPPError, return the
        // XMPPError as the message.
        if (msg == null && error != null) {
            return error.toString();
        }
        else if (msg == null && streamError != null) {
            return streamError.toString();
        }
        return msg;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String message = super.getMessage();
        if (message != null) {
            buf.append(message).append(": ");
        }
        if (error != null) {
            buf.append(error);
        }
        if (streamError != null) {
            buf.append(streamError);
        }
        if (wrappedThrowable != null) {
            buf.append("\n  -- caused by: ").append(wrappedThrowable);
        }

        return buf.toString();
    }
}