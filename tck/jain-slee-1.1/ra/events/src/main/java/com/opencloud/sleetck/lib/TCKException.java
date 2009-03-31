/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib;

import com.opencloud.sleetck.lib.testutils.ExceptionsUtil;

/**
 * Super class of all SLEE TCK exceptions.
 */
public class TCKException extends Exception {

    public TCKException(String message, Exception nestedException) {
        super(message+". Nested exception: "+ExceptionsUtil.formatThrowable(nestedException));
        this.nestedException = nestedException;
    }

    public TCKException(String message) {
        super(message);
    }

    public Exception getEnclosedException() {
        return nestedException;
    }

    private Exception nestedException;

}