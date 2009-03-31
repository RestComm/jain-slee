/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.util.exception;

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



