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

/**
 * 
 * TODO Class Description
 * Depracated, in SLEE 1.1 Specs by ConfigurationProperties set
 * @author F.Moggia
 * @deprecated
 */
public class ResourceAdaptorBoostrapContext implements BootstrapContext {
    
	protected String raEntityName;
    protected SleeEndpoint endpoint;
    protected EventLookupFacility eventLookup;
    protected AlarmFacility alarmFacility;
    
    protected SleeTransactionManager sleeTransactionManager;
    protected ProfileFacility profileFacility;
    
   
    
    public ResourceAdaptorBoostrapContext(String raEntityName,
			SleeEndpoint endpoint, EventLookupFacility eventLookup,
			AlarmFacility alarmFacility,
			SleeTransactionManager sleeTransactionManager,
			ProfileFacility profileFacility) {
		super();
		this.raEntityName = raEntityName;
		this.endpoint = endpoint;
		this.eventLookup = eventLookup;
		this.alarmFacility = alarmFacility;
		this.sleeTransactionManager = sleeTransactionManager;
		this.profileFacility = profileFacility;
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

	public String getRaEntityName() {
		return raEntityName;
	}

	public SleeEndpoint getEndpoint() {
		return endpoint;
	}

	public AlarmFacility getAlarmFacility() {
		return alarmFacility;
	}

	public SleeTransactionManager getSleeTransactionManager() {
		return sleeTransactionManager;
	}

	public ProfileFacility getProfileFacility() {
		return profileFacility;
	}

	public EventLookupFacility getEventLookupFacility() {
		
		return eventLookup;
	}

	public Timer getTimer() {
		// TODO Auto-generated method stub
		return null;
	}
    
 
 
    
}
