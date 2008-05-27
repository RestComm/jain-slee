/*
 * Created on Nov 19, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.tck.TCKActivityHandle;
import org.mobicents.slee.runtime.SleeInternalEndpoint;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import javax.slee.Address;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.CouldNotStartActivityException;
import javax.slee.resource.SleeEndpoint;

import org.jboss.logging.Logger;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceAdaptorInterface;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceEventHandler;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class TCKResourceEventHandlerImpl implements TCKResourceEventHandler {
    private SleeEndpoint sleeEndpoint;

    private EventLookupFacility eventLookup;

    private static Logger logger;

    private TCKResourceAdaptorInterface raInterface;

    static {
        logger = Logger.getLogger(SleeContainer.class);
    }

    public TCKResourceEventHandlerImpl(BootstrapContext ctx,
            TCKResourceAdaptorInterface raInterface) {
        this.raInterface = raInterface;
        this.sleeEndpoint = ctx.getSleeEndpoint();
        eventLookup = ctx.getEventLookupFacility();
       
    }

    public void handleEvent(TCKResourceEvent eventObj, String eventType,
            TCKActivityID activityObj, Address address)
            throws TCKTestErrorException, RemoteException {
        if ( logger.isDebugEnabled()) {
            logger.debug("Event Type :" + eventType);
        }
        /*
         * StringTokenizer token = new StringTokenizer(eventType, ";"); if
         * (token.countTokens() != 3){ throw new RemoteException("Bad event type
         * string format: " + eventType); }
         */

        String name = eventType;
        String vendor = "jain.slee.tck";
        String version = "1.0";

        int eventTypeId;
        try {
            eventTypeId = eventLookup.getEventID(name, vendor, version);
        } catch (FacilityException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Failed to lookup Event!", e1);
        } catch (UnrecognizedEventException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Failed to lookup Event!", e1);
        }
        try {
            sleeEndpoint.fireEvent(new TCKActivityHandle(activityObj),
                    eventObj, eventTypeId, address);
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            throw new RuntimeException ("unexpected exception ", e2);
        } 
        logger.debug("fireEvent competed");
    }

    public void handleActivityEnd(TCKActivityID tckActivityID, boolean arg1)
            throws TCKTestErrorException, RemoteException {
        if ( logger.isDebugEnabled() ) 
            logger.debug("TCKActivity ID " + tckActivityID);

        try {
            //sleeEndpoint.sendActivityEndEvent(raInterface.getActivity(tckActivityID));
            sleeEndpoint.activityEnding(new TCKActivityHandle(tckActivityID));
        } catch (UnrecognizedActivityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void handleActivityCreatedBySbb(TCKActivityID activityID)
            throws TCKTestErrorException, RemoteException {
        try {
            sleeEndpoint.activityStarted(new TCKActivityHandle(activityID));
        } catch (ActivityAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CouldNotStartActivityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}