/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib;

import com.opencloud.sleetck.lib.testutils.ExceptionsUtil;
import java.io.Serializable;

/**
 * The result type for all SLEE TCK tests.
 * It replaces JavaTest's Status class, and adds
 * the requirement for assertion ID's for test failures
 */
public class TCKTestResult implements Serializable {

    // Constants

    public static final int PASSED = 0;
    public static final int FAILED = 1;
    public static final int ERROR  = 2;

    public static final int NO_ASSERTION = -1;

    // Constructors

    private TCKTestResult(int type, String reason) {
        this(type, reason, NO_ASSERTION);
    }

    private TCKTestResult(int type, String reason, int assertionID) {
        this.type = type;
        this.reason = reason;
        this.assertionID = assertionID;
    }

    // Static creation methods

    public static TCKTestResult passed() {
        return new TCKTestResult(PASSED, null);
    }

    public static TCKTestResult failed(int assertionID, String reason) {
        return new TCKTestResult(FAILED, reason, assertionID);
    }

    public static TCKTestResult failed(TCKTestFailureException reason) {
        return failed(reason.getAssertionID(), ExceptionsUtil.formatThrowable(reason));
    }

    public static TCKTestResult error(String reason) {
        return error(reason,null);
    }

    public static TCKTestResult error(Throwable cause) {
        return error(null,cause);
    }

    public static TCKTestResult error(String reason, Throwable cause) {
        String formattedCause = (cause == null ? null : ExceptionsUtil.formatThrowable(cause));
        String formattedReason = null;
        if(reason == null) formattedReason = formattedCause;
        else if(cause == null) formattedReason = reason;
        else formattedReason = reason + "\nCause:" + formattedCause;
        return new TCKTestResult(ERROR,formattedReason);
    }

    // Instance accessor methods

    public boolean isPassed() {
        return type == PASSED;
    }

    public boolean isFailed() {
        return type == FAILED;
    }

    public boolean isError() {
        return type == ERROR;
    }

    public int getType() {
        return type;
    }

    public int getAssertionID() {
        return assertionID;
    }

    public String getReason() {
        return reason;
    }

    // Miscellaneous methods

    public String toString() {
        StringBuffer rBuf = new StringBuffer(typeToString(type));
        if(!isPassed()) rBuf.append("\nReason: ").append(reason);
        if(isFailed())  rBuf.append("\nAssertion ID: ").append(assertionID);
        return rBuf.toString();
    }

    public static String typeToString(int type) {
        String rString = null;
        switch (type) {
            case PASSED: rString = "PASSED"; break;
            case FAILED: rString = "FAILED"; break;
            case ERROR:  rString = "ERROR";  break;
            default: throw new IllegalArgumentException("Unrecognized result type: "+type);
        }
        return rString;
    }

    // Export / Import

    /**
     * Exports the Object into an Object or array of primitive types,
     * J2SE types or SLEE types.
     * SBBs wishing to send a result to a test should pass the result
     * in the exported form to avoid classloading issues.
     */
    public synchronized Object toExported() {
        Object[] dataArray = new Object[3];
        dataArray[0] = new Integer(type);
        dataArray[1] = reason;
        dataArray[2] = new Integer(assertionID);
        return dataArray;
    }

    /**
     * Returns a TCKTestResult Object from the
     * given object in exported form.
     * Tests expecting to receive a result from an SBB in exported form
     * use this method to import to result.
     */
    public static TCKTestResult fromExported(Object exported) {
        Object[] dataArray = (Object[])exported;
        int type = ((Integer)dataArray[0]).intValue();
        String reason = (String)dataArray[1];
        int assertionID = ((Integer)dataArray[2]).intValue();
        return new TCKTestResult(type,reason,assertionID);
    }

    // Private instance state

    private int type;
    private String reason;
    private int assertionID;

}