/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource;

/**
 * A TCKTestCallException is used to wrap an Exception thrown by
 * the onSbbCall() method of the TCKResourceListener.
 */
public class TCKTestCallException extends Exception {

    public TCKTestCallException(Exception exception) {
        this.exception=exception;
    }

    /**
     * Returns the Exception thrown by the onSbbCall() method
     */
    public Exception getException() {
        return exception;
    }

    private Exception exception;

}