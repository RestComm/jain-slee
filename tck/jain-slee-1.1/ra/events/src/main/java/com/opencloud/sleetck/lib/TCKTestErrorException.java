/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib;

/**
 * Exception class for TCK test errors.
 * Use this exception to indicate that a test could 
 * not complete due to an unexpected error.
 */
public class TCKTestErrorException extends TCKException {

    public TCKTestErrorException(String message) {
        super(message);
    }
    
    public TCKTestErrorException(String message, Exception nestedException) {
        super(message,nestedException);
    }
    
}