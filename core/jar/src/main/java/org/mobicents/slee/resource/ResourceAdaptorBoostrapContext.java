/*
 * Created on Mar 7, 2005
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

import java.util.Timer;

import javax.slee.SLEEException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileFacility;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.transaction.SleeTransactionManager;

import org.mobicents.slee.runtime.SleeInternalEndpoint;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class ResourceAdaptorBoostrapContext implements BootstrapContext {
    String raEntityName;
    SleeEndpoint endpoint;
    EventLookupFacility eventLookup;
    
    public ResourceAdaptorBoostrapContext(SleeEndpoint endpoint, EventLookupFacilityImpl eventLookup, String raEntityName) {
        this.endpoint = endpoint;
        this.eventLookup = eventLookup;
        this.raEntityName = raEntityName;
    }
    
    public String getEntityName() {
        return raEntityName;
    }

    public SleeEndpoint getSleeEndpoint() {
        // TODO Auto-generated method stub
        return this.endpoint;
    }
    /*
     * NOT YET IMPLEMENTED
     *  
     */
    public Tracer getTracer(String arg0) throws NullPointerException,
            IllegalArgumentException, SLEEException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * NOT YET IMPLEMENTED
     *  
     */
    public AlarmFacility getAlarmFacility() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public EventLookupFacility getEventLookupFacility() {
        // TODO Auto-generated method stub
        return this.eventLookup;
    }

    public Timer getTimer() {
        // TODO Auto-generated method stub
        return null;
    }

    public SleeTransactionManager getSleeTransactionManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public ProfileFacility getProfileFacility() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
