/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.xcapclient;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;

import org.mobicents.xcap.client.XcapClient;
import org.mobicents.xcap.client.impl.XcapClientImpl;

/** 
 * @author Eduardo Martins
 * @author aayush.bhatnagar
 * @version 2.0
 * 
 */
public class XCAPClientResourceAdaptor implements javax.slee.resource.ResourceAdaptor,
		Serializable {
    
 	private static final long serialVersionUID = 1L;
   
    private  ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl> activities;    
    
    private  XCAPClientResourceAdaptorSbbInterfaceImpl sbbInterface;
    private  XcapClient client;
    private  ExecutorService executorService = Executors.newCachedThreadPool();
    
    // XCAP RA context:
    private javax.slee.resource.ResourceAdaptorContext raContext;
    // JAIN SLEE endpoint:
    private SleeEndpoint sleeEndpoint;
    // Event Lookup Facility:
    private EventLookupFacility eventLookupFacility;
    // JAIN SLEE tracer facility:
    private Tracer tracer;
    // Event fired:
    private FireableEventType getResponseEvent;
    private FireableEventType putResponseEvent;
    private FireableEventType deleteResponseEvent;
   
    public FireableEventType getGetResponseEventType() {
		return getResponseEvent;
	}
    
    public FireableEventType getDeleteResponseEventType() {
		return deleteResponseEvent;
	}
    
    public FireableEventType getPutResponseEventType() {
		return putResponseEvent;
	}
    
	/**
	 * 
	 * @param ah
	 */
	public void endActivity(ActivityHandle ah) {
		if (tracer.isFineEnabled()) {
			tracer.fine("endActivity");
		}
		
		if (activities.containsKey(ah)) {
			// tell slee to end the activity
			try {
				this.sleeEndpoint.endActivity(ah);
			} catch (Exception e) {
				tracer.severe("unable to end activity: ",e);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
	 */
	public void activityEnded(ActivityHandle ah) {		
		if (tracer.isFineEnabled()) {
			tracer.fine("activityEnded( handle = "+ah+")");
		}
		// just remove the handle
	    activities.remove(ah);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle ah) {		
		// ignore
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle ah) {
		if (tracer.isFineEnabled()) {
			tracer.fine("queryLiveness( handle = "+ah+")");
		}
		// guard against activity leaks:
		if (!activities.contains(ah)) {
			raContext.getSleeEndpoint().endActivity(ah);
		}			
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle ah) {
	    if (tracer.isFineEnabled()) {
	    	tracer.fine("get Activity with ActivityHandle "+ah.toString());
	    }
	    // if handle exists then recreate activity
	    return activities.get(ah);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object arg0) {
		if (tracer.isFineEnabled()) {
			tracer.fine("getActivityHandle");
	    }
	    XCAPResourceAdaptorActivityHandle activityHandle = ((AsyncActivityImpl)arg0).getHandle();
	    if (activities.containsKey(activityHandle)) {
	    	return activityHandle;
	    }
	    else {
	    	return null;
	    }
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		return null;
	}
			
	/**
	 * Receives an Event and sends it to the SLEE
	 * @param event
	 * @param handle
	 */
	public void processResponseEvent(FireableEventType eventType, ResponseEvent event, XCAPResourceAdaptorActivityHandle handle){
        
    	if (tracer.isFineEnabled()) {    		            		
    		tracer.fine("NEW RESPONSE EVENT");        
    	}
                        
        try {                	
            sleeEndpoint.fireEvent(handle,eventType, event, null, null);
        } catch (Exception e) {           
            tracer.severe("unable to fire event",e);
        }        
    }
    
    protected ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl> getActivities() {
		return activities;
	} 
        
    /**
     * @return Returns the sleeEndpoint.
     */
    public SleeEndpoint getSleeEndpoint() {
       return sleeEndpoint;
    }
 
    /**
     * 
     * @return the XCAP client API handle
     */
	public XcapClient getClient() {
		return client;
	}
    
	/**
	 * 
	 * @return the executor service for this RA
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	/**
	 * To add the newly created activity to the activity map
	 * @param activityHandle
	 * @param activity
	 */
	public void addActivity(XCAPResourceAdaptorActivityHandle activityHandle,
			AsyncActivityImpl activity) {
		activities.put(activityHandle, activity);
	}
	
	/**
     * 
     * @param tracerName
     * @return
     */
	public Tracer getTracer(String tracerName) {
            return this.tracer;
    }
    
    /**
     * 
     * @return
     */
    public ResourceAdaptorContext getXCAPResourceAdaptorContext() {
    	return this.raContext;
    }

    // JAIN SLEE 1.1 XCAP RA Callbacks:   

    /*
     * (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource.ActivityHandle)
     */
	public void administrativeRemove(ActivityHandle arg0) {
		activities.remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle ah,
			FireableEventType arg1, Object arg2, Address ad,
			ReceivableService arg4, int arg5, FailureReason fr) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle ah,
			FireableEventType arg1, Object arg2, Address ad,
			ReceivableService arg4, int arg5) {
				
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.lang.String)
	 */
	public Object getResourceAdaptorInterface(String arg0) {
		return this.sbbInterface;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raActive()
	 */
	public void raActive() {
		// init client
		client = new XcapClientImpl();		
		activities = new ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl>();
		executorService = Executors.newCachedThreadPool();
		sbbInterface.setActive(true);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigurationUpdate(ConfigProperties arg0) {
		//no configuration
		
	}
 
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.ConfigProperties)
	 */
	public void raConfigure(ConfigProperties arg0) {
		//no config
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raInactive()
	 */
	public void raInactive() {
		sbbInterface.setActive(false);
		activities = null;
		client.shutdown();
		client=null;
		executorService.shutdown();
		executorService = null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raStopping()
	 */
	public void raStopping() {
		// end all activities
	    synchronized(activities) {	    
	    	for(XCAPResourceAdaptorActivityHandle handle : activities.keySet()) {   		
	    		endActivity(handle);
	    	}
	    }		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
	 */
	public void raUnconfigure() {
		// not impl for this RA
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.resource.ConfigProperties)
	 */
	public void raVerifyConfiguration(ConfigProperties arg0)
			throws InvalidConfigurationException {
		// no verification for this RA.
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource.ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		// no event filtering
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource.ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		//no event filtering
		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource.ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		//no event filtering
		
	}
    
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext raContext) {
		this.raContext = raContext; 
		this.tracer = raContext.getTracer("XCAPClientResourceAdaptor");
		sbbInterface = new XCAPClientResourceAdaptorSbbInterfaceImpl(this);
        this.sleeEndpoint = raContext.getSleeEndpoint();
        this.eventLookupFacility = raContext.getEventLookupFacility();

        try {
        	this.getResponseEvent = eventLookupFacility.getFireableEventType(new EventTypeID("GetResponseEvent", "org.mobicents", "2.0"));
        	this.putResponseEvent = eventLookupFacility.getFireableEventType(new EventTypeID("PutResponseEvent", "org.mobicents", "2.0"));
        	this.deleteResponseEvent = eventLookupFacility.getFireableEventType(new EventTypeID("DeleteResponseEvent", "org.mobicents", "2.0"));
        }
        catch(Exception e) {
        	if(tracer.isWarningEnabled())
        		tracer.warning("Could not look up Response Event",e);
        }	
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
	 */
	public void unsetResourceAdaptorContext() {
		this.raContext = null;
		this.tracer = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
		this.sbbInterface = null;		
	}
	
}

