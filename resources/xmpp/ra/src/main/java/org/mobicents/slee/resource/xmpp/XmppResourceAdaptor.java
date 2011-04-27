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

package org.mobicents.slee.resource.xmpp;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;

/** 
 * @author Eduardo Martins
 * @version 2.1
 * 
 */

public class XmppResourceAdaptor implements ResourceAdaptor {
           
    private ConcurrentHashMap<XmppActivityHandle,XmppConnection> activities;
    private SleeEndpoint sleeEndpoint;
    private EventLookupFacility eventLookup;
    private XmppResourceAdaptorSbbInterfaceImpl sbbInterface;
    private ResourceAdaptorContext resourceAdaptorContext;
    private Tracer log;
    private FireableEventTypeFilter eventTypeFilter = new FireableEventTypeFilter();

    private FireableEventTypeCache eventTypeCache;
    
    // lifecycle methods 
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
     */
    public void setResourceAdaptorContext(ResourceAdaptorContext arg0) {
    	this.resourceAdaptorContext = arg0;
    	this.eventLookup = arg0.getEventLookupFacility();
    	this.sleeEndpoint = arg0.getSleeEndpoint();
    	this.log = arg0.getTracer(getClass().getSimpleName());
    	eventTypeCache = new FireableEventTypeCache(log);
    	this.sbbInterface = new XmppResourceAdaptorSbbInterfaceImpl(this);
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raConfigure(javax.slee.resource.ConfigProperties)
     */
    public void raConfigure(ConfigProperties arg0) {}
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raActive()
     */
    public void raActive() {
    	this.sbbInterface.setActive(true);
    	this.activities = new ConcurrentHashMap<XmppActivityHandle, XmppConnection>();
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raStopping()
     */
    public void raStopping() {
    	// end all connections
    	for(XmppConnection activity : activities.values()) {
    		XmppActivityHandle handle = new XmppActivityHandle(activity.getConnectionId());
    		((XMPPConnection)activity.getConnection()).close();
    		endActivity(handle);
    	}
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raInactive()
     */
    public void raInactive() {
    	this.sbbInterface.setActive(false);
    	this.activities = null;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raUnconfigure()
     */
    public void raUnconfigure() {}
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#unsetResourceAdaptorContext()
     */
    public void unsetResourceAdaptorContext() {
    	this.resourceAdaptorContext = null;
    	this.eventLookup = null;
    	this.sleeEndpoint = null;
    	this.log = null;
    	this.eventTypeCache = null;
    	this.sbbInterface = null;
    }

    // config management methods
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raVerifyConfiguration(javax.slee.resource.ConfigProperties)
     */
    public void raVerifyConfiguration(ConfigProperties arg0)
    		throws InvalidConfigurationException {}
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#raConfigurationUpdate(javax.slee.resource.ConfigProperties)
     */
    public void raConfigurationUpdate(ConfigProperties arg0) {}
    
    // interface access methods
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
     */
    public Marshaler getMarshaler() {
    	return null;
    }
    
    /* (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#getResourceAdaptorInterface(java.lang.String)
     */
    public Object getResourceAdaptorInterface(String arg0) {
    	return sbbInterface;
    }
    
    // mandatory callbacks
    
    /*
     * (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#administrativeRemove(javax.slee.resource.ActivityHandle)
     */
    public void administrativeRemove(ActivityHandle arg0) {};
    
    /*
     * (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
     */
    public void queryLiveness(ActivityHandle arg0) {
		if (log.isFineEnabled()) {
			log.fine("queryLiveness");
		}
		// if connection does not exist fire an activity end event
		XmppConnection connection = (XmppConnection) activities.get(arg0);
		if (connection == null) {
			endActivity(arg0);
		}		
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle arg0) {
		return getActivity((XmppActivityHandle)arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object arg0) {
	    if (log.isFineEnabled()) {
	    	log.fine("getActivityHandle");
	    }
	    XmppActivityHandle activityHandle = null;
	    try {
	    	activityHandle = new XmppActivityHandle(((XmppConnection)arg0).getConnectionId());
	    }
	    catch (Exception e) {
	    	log.severe("Failed to get the ActivityHandle.",e);
	    }
	    return activityHandle;
	}
	
	// optional callbacks
	
	/*
     * (non-Javadoc)
     * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
     */
	public void activityEnded(ActivityHandle ah) {		
		if (log.isFineEnabled()) {
			log.info("Activity Ended of connection "+ah.toString());
		}
	    XmppConnection activity = (XmppConnection) activities.remove(ah);
		if (activity !=null) {
		    if (log.isFineEnabled()) {
		        log.fine("Removed terminated activity " +
		                "[activity =  " + activity + "]"); 
		        log.fine("active activity list = " + activities.size());
		    }
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {}
    
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {}
	
	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventUnreferenced(javax.slee.resource.ActivityHandle, javax.slee.resource.FireableEventType, java.lang.Object, javax.slee.Address, javax.slee.resource.ReceivableService, int)
	 */
	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {}
	
	// event filtering
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceActive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceActive(ReceivableService arg0) {
		eventTypeFilter.serviceActive(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceStopping(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceStopping(ReceivableService arg0) {
		eventTypeFilter.serviceStopping(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.ResourceAdaptor#serviceInactive(javax.slee.resource
	 * .ReceivableService)
	 */
	public void serviceInactive(ReceivableService arg0) {
		eventTypeFilter.serviceInactive(arg0);
	}
	
	// ra logic
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public XmppConnection getActivity(XmppActivityHandle handle) {
	    if (log.isFineEnabled()) {
	    	log.fine("get Activity with ActivityHandle "+handle.toString());
	    }
		return this.activities.get(handle);
	}
	
	/**
	 * Ends an activity
	 */
	public void endActivity(ActivityHandle ah) {
		if (log.isFineEnabled()) {
			log.fine("endActivity");
		}
		try {
			this.sleeEndpoint.endActivity(ah);
		} catch (Throwable e) {
			log.severe("Failed to end activity.",e);
		}
	}
	
	/**
	 * Receives an Event and sends it to the SLEE.
	 * @param event
	 * @param handle
	 */
    public void processEvent(Object event, XmppActivityHandle handle){
        
    	if (log.isFineEnabled()) {    		            		
    		log.fine("NEW XMPP-RA EVENT: " + event.getClass().getName());        
    	}
        
        final FireableEventType eventType = eventTypeCache.getEventType(eventLookup, event);

        if (eventTypeFilter.filterEvent(eventType)) {
        	if (log.isFineEnabled()) {
        		log.fine("No services activated have interest in "+eventType);
        	}        	 
        	return;
        }
  
        try {        
        	sleeEndpoint.fireEvent(handle, eventType, event, null, null);
        } catch (Throwable e) {
            log.severe("Failed to fire event.",e);
        }
        
    }
    
    // aux priv methods
    
    void addListener(String connectionID, XMPPConnection connection, XmppActivityHandle handle, Collection<Class<?>> packetFilters) {
    	// create listener
    	XmppConnectionListener listener = new XmppConnectionListener(this,connectionID,handle);    	
    	// add packet filters if packetFilters is not null
    	if (packetFilters == null) {
    		connection.addPacketListener(listener, null);
    	}
    	else {
    		for(Class<?> clazz : packetFilters) {        		
    			connection.addPacketListener(listener, new PacketTypeFilter(clazz));
    		}
    	}
    	// add connection listener
    	connection.addConnectionListener(listener);
    }
    
    XmppConnection createActivity(String connectionID, XMPPConnection connection, XmppActivityHandle handle) throws XMPPException {
    	XmppConnection activity = new XmppConnection(connectionID,connection);
    	if (activities.putIfAbsent(handle,activity) == null) {						
    		try {
    			sleeEndpoint.startActivity(handle,activity,ActivityFlags.REQUEST_ENDED_CALLBACK);
    		} catch (ActivityAlreadyExistsException e) {
    			log.warning(e.getMessage(),e);
    		} catch (Throwable e) {
    			activities.remove(handle);
    			throw new XMPPException(e.getMessage(),e);
    		}
    	}
    	return activity;
    }
    
    /**
	 * @return the resourceAdaptorContext
	 */
	public ResourceAdaptorContext getResourceAdaptorContext() {
		return resourceAdaptorContext;
	}
	
	/**
	 * @return the tracer
	 */
	public Tracer getTracer() {
		return log;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<XmppActivityHandle,XmppConnection> getActivities() {
		if (log.isFineEnabled()) {
			log.fine("getActivities");
		}        
		return this.activities;
	}  
	
}

