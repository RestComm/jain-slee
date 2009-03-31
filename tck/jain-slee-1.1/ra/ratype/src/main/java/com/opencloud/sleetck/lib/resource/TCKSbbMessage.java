/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource;

/**
 * TCKSbbMessage are messages sent from SBBs
 * to a test case via the TCKActivity interface,
 * or the TCKResourceSbbInterface.
 */
public interface TCKSbbMessage {

    /**
     * Returns the payload of the message (may be null).
     */
    public Object getMessage();

}
