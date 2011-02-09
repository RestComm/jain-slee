package org.mobicents.util;

public class XMLException extends java.io.IOException {
    /**
     * Construct an exception with the specified detail message.
     * @param message a meaningful message which explains the exceptional situation
     */
    public XMLException(String message) {
        super(message);
    }
    
    public XMLException(String message, Throwable cause) {
	super(message);
        initCause(cause);
    }
}



