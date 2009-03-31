/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.testutils;

import com.opencloud.sleetck.lib.TCKTestFailureException;

/**
 * A utility class which contains static methods to assert
 * conditions to be checked by the test.
 * A failed assertion indicates a test failure as opposed to a test error.
 */
public class Assert {

    public static void assertEquals(int assertionID, String message, Object expected, Object actual) throws TCKTestFailureException {
        boolean equals = (expected == null ? actual == null : expected.equals(actual));
        if(!equals) fail(assertionID, message+". expected="+expected+";actual="+actual);
    }

    public static void assertTrue(int assertionID, String message, boolean condition) throws TCKTestFailureException {
        if(!condition) fail(assertionID, message);
    }

    public static void fail(int assertionID, String message) throws TCKTestFailureException {
        throw new TCKTestFailureException(assertionID, message);
    }

}