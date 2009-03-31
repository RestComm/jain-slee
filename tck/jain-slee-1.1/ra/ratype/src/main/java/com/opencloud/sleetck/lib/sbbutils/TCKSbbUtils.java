/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.sbbutils;

import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TraceFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Level;
import javax.slee.ComponentID;
import javax.slee.UnrecognizedComponentException;

/**
 * Defines static utility methods used by the TCK SBBs.
 */
public class TCKSbbUtils {

    /**
     * Returns the SBB interface to the TCK resource adaptor
     */
    public static TCKResourceAdaptorSbbInterface getResourceAdaptorInterface() throws NamingException {
        return (TCKResourceAdaptorSbbInterface)getSbbEnvironment().lookup(TCKSbbConstants.TCK_RESOURCE_ADAPTOR_LOCATION);
    }

    /**
     * Returns the SBB interface to the TCK resource
     */
    public static TCKResourceSbbInterface getResourceInterface() throws NamingException {
        return getResourceAdaptorInterface().getResource();
    }

    /**
     * Returns the SBB component environment naming context
     * (the context stored at "java:comp/env")
     */
    public static Context getSbbEnvironment() throws NamingException {
        Context initCtx = new InitialContext();
        return (Context)initCtx.lookup("java:comp/env");
    }

    /**
     * Forwards the given Exception to the test via the TCK resource,
     * or prints to the stderr stream if that fails.
     */
    public static void handleException(Exception toHandle) {
        try {
            getResourceInterface().sendException(toHandle);
        } catch (Exception newEx) {
            // print to the stderr stream as a last resort
            toHandle.printStackTrace();
            newEx.printStackTrace();
        }
    }

    /**
     * Creates an alarm using the AlarmFacility
     */
    public static void createAlarm(ComponentID alarmSource, Level alarmLevel, String message, Throwable cause) throws TCKTestErrorException {
        try {
            AlarmFacility alarmFacility = (AlarmFacility)getSbbEnvironment().lookup("slee/facilities/alarm");
            alarmFacility.createAlarm(alarmSource, alarmLevel, TCKSbbConstants.TCK_ALARM_TYPE, message,
                                            cause, System.currentTimeMillis());
        } catch (Exception ex) {
            throw new TCKTestErrorException("Caught Exception while trying to send an alarm:"+ex+". Alarm message: "+message);
        }
    }

    /**
     * Creates an trace message using the TraceFacility
     */
    public static void createTrace(ComponentID messageSource, Level traceLevel, String message, Throwable cause) throws TCKTestErrorException {
        try {
            TraceFacility traceFacility = (TraceFacility)getSbbEnvironment().lookup("slee/facilities/trace");
            traceFacility.createTrace(messageSource, traceLevel, TCKSbbConstants.TCK_MESSAGE_TYPE, message,
                                            cause, System.currentTimeMillis());
        } catch (Exception ex) {
            throw new TCKTestErrorException("Caught Exception while trying to send a trace message:"+ex+". Trace message: "+message);
        }
    }

}