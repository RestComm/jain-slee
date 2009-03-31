/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.sbbutils;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import javax.slee.Sbb;
import javax.slee.SbbID;
import javax.slee.SbbContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.facilities.Level;

/**
 * TCK SBBs may optionally extend this abstract class.
 */
public abstract class BaseTCKSbb implements Sbb {

    // -- SBB methods -- //

    /**
     * This implementation sends the exception to the test via the TCK resource
     */
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface aci) {
        TCKSbbUtils.handleException(new TCKTestErrorException("sbbExceptionThrown() called. event="+event+";aci="+aci,exception));
    }

    /**
     * This implementation sends an exception to the test via the TCK resource
     */
    public void sbbRolledBack(RolledBackContext context) {
        TCKSbbUtils.handleException(new TCKTestErrorException("Unexpected roll back. event="+context.getEvent()+";aci="+context.getActivityContextInterface()));
    }

    /**
     * This implementation sets the sbb context instance variable,
     * which can be accessed via getSbbContext().
     */
    public void setSbbContext(SbbContext context) {
        this.context=context;
    }

    /**
     * This implementation unsets the sbb context instance variable.
     */
    public void unsetSbbContext()  {
        context = null;
    }

    // Empty method implementations

    /** Empty implementation */
    public void sbbCreate() throws CreateException  {}
    /** Empty implementation */
    public void sbbPostCreate() throws CreateException  {}
    /** Empty implementation */
    public void sbbActivate()  {}
    /** Empty implementation */
    public void sbbPassivate() {}
    /** Empty implementation */
    public void sbbLoad() {}
    /** Empty implementation */
    public void sbbStore() {}
    /** Empty implementation */
    public void sbbRemove() {}

    // -- Introduced protected methods -- //

    protected SbbID getSbbID() {
        return context.getSbb();
    }

    protected SbbContext getSbbContext() {
        return context;
    }

    /** Creates the specified trace message, and handles any resulting Exceptions */
    protected void createTraceSafe(Level traceLevel, String message) {
        createTraceSafe(traceLevel,message,null);
    }

    /** Creates the specified trace message, and handles any resulting Exceptions */
    protected void createTraceSafe(Level traceLevel, String message, Throwable cause) {
        try {
            TCKSbbUtils.createTrace(getSbbID(),traceLevel,message,cause);
        } catch (TCKTestErrorException e) {
            TCKSbbUtils.handleException(e);
        }
    }

    // -- Private state -- //

    private SbbContext context;

}
