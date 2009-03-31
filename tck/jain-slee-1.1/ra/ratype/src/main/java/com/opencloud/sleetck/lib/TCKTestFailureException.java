/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib;

/**
 * Exception class for TCK test failures.
 * Use this exception to indicate that the test subject failed
 * to satisfy and assertion of behaviour.
 */
public class TCKTestFailureException extends TCKException {

    public TCKTestFailureException(int assertionID, String message) {
        super(message);
        this.assertionID = assertionID;
    }

    public TCKTestFailureException(int assertionID, String message, Exception nestedException) {
        super(message,nestedException);
        this.assertionID = assertionID;
    }

    public int getAssertionID() {
        return assertionID;
    }

    private int assertionID;

}