/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU Lesser General Public License as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU Lesser General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.resource.xmpp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.CouldNotStartActivityException;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ComponentXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;

/** 
 * @author Eduardo Martins
 * @version 2.1
 * 
 */

public class XmppResourceAdaptor implements ResourceAdaptor,
		Serializable {
    
 	private static final long serialVersionUID = 1L;
	static private transient Logger log;
    static {
        log = Logger.getLogger(XmppResourceAdaptor.class);
    }
    
    private transient SleeContainer container;
    private ResourceAdaptorState state;    
    private transient Map activities = Collections.synchronizedMap(new HashMap(100));
    private int maxEventsPoolSize;
    private transient SleeEndpoint sleeEndpoint;
    private transient EventLookupFacility eventLookup;
    private transient BootstrapContext bootstrapContext;
    private transient XmppRASbbInterfaceImpl sbbInterface;
    private transient XmppActivityContextInterfaceFactory acif;
    private transient Map XmppToSleeEvent = Collections.synchronizedMap(new HashMap(100));
   
    //event filtering
    private Map eventIDsOfServicesInstalled = Collections.synchronizedMap(new HashMap(31));
    private Map myComponentKeys = Collections.synchronizedMap(new HashMap(31));
    
    public XmppResourceAdaptor() { }
    
    public void entityCreated(BootstrapContext ctx) throws javax.slee.resource.ResourceException {
    	//TODO
	    if (log.isDebugEnabled()) {
	    	log.debug("entityCreated");
	    }
	    this.init(ctx);
	}

	public void entityRemoved() {
		//TODO
	    if (log.isDebugEnabled()) {
	    	log.debug("entityRemoved");
	    }
	}

	public void entityActivated() throws ResourceException {
	    if (log.isDebugEnabled()) {
	    	log.debug("entityActivated");
	    }
		try {
            try {
                this.configure();
            } catch (InvalidStateException e1) {
                e1.printStackTrace();
            }
            this.start();
        } catch (ResourceException e) {
            e.printStackTrace();
            throw new javax.slee.resource.ResourceException("Failed to Activate Resource Adaptor!", e);
        }
	}

	public void entityDeactivating() {
		//TODO
		if (log.isDebugEnabled()) {
			log.debug("entityDeactivating");
		}
		this.stopping();

	}

	public void entityDeactivated() {
		//TODO
		if (log.isDebugEnabled()) {
			log.debug("entityDeactivated");
		}
		this.stop();
	}

	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {
		//TODO
		if (log.isDebugEnabled()) {
			log.debug("eventProcessingSuccessful");
		}
	}

	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
	    //TODO
		if (log.isDebugEnabled()) {
			log.debug("eventProcessingFailed");
		}
	}

	public void endActivity(ActivityHandle ah) {
		if (log.isDebugEnabled()) {
			log.debug("endActivity");
		}
		try {
			this.sleeEndpoint.activityEnding(ah);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void activityEnded(ActivityHandle ah) {		
		if (log.isDebugEnabled()) {
			log.info("Activity Ended of connection "+ah.toString());
		}
	    XmppConnection activity = (XmppConnection) activities.remove(ah);
		if (activity !=null) {
		    if (log.isDebugEnabled()) {
		        log.debug("Removed terminated activity " +
		                "[activity =  " + activity + "]"); 
		        log.debug("active activity list = " + activities.size());
		    }
		}
	}

	public void activityUnreferenced(ActivityHandle ah) {		
		if (log.isDebugEnabled()) {
			log.debug("activityUnreferenced");
		}
		XmppActivityHandle ac = (XmppActivityHandle) activities.remove(ah);
		if (ac !=null) {
		    if (log.isDebugEnabled()) {
		        log.debug("Removed terminated activity " +
		                "[activity =  " + ac + "]"); 
		        log.debug("active activity list = " + activities.size());
		    }
		}
	}

	public void queryLiveness(ActivityHandle arg0) {
		if (log.isDebugEnabled()) {
			log.debug("queryLiveness");
		}
		// if connection does not exist fire an activity end event
		XmppConnection connection = (XmppConnection) activities.get(arg0);
		if (connection == null) {
			endActivity(arg0);
		}		
	}

	public Object getActivity(ActivityHandle handle) {
	    if (log.isDebugEnabled()) {
	    	log.debug("get Activity with ActivityHandle "+handle.toString());
	    }
		return this.activities.get(handle);
	}

	public ActivityHandle getActivityHandle(Object arg0) {
	    if (log.isDebugEnabled()) {
	    	log.debug("getActivityHandle");
	    }
	    XmppActivityHandle activityHandle = null;
	    try {
	    	activityHandle = new XmppActivityHandle(((XmppConnection)arg0).getConnectionId());
	    }
	    catch (Exception e) {
	    	log.error("Failed to get the ActivityHandle.",e);
	    }
	    return activityHandle;
	}

	public Object getSBBResourceAdaptorInterface(String arg0) {
		if (log.isDebugEnabled()) {
			log.debug("getSBBResourceAdaptorInterface");
		}
		return this.sbbInterface;
	}

	public Marshaler getMarshaler() {
		//TODO
		if (log.isDebugEnabled()) {
			log.debug("getMarshaler");
		}
		return null;
	}

	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {				
		eventIDsOfServicesInstalled.put(arg0,arg1);		
	}

	public void serviceActivated(String arg0) {		
		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(arg0);
		if (eventIDs != null) {
			for(int i=0;i<eventIDs.length;i++) {
				EventTypeID eventTypeID = container.getEventTypeID(eventIDs[i]);
				ComponentKey eventKey = container.getEventKey(eventTypeID);
				Set servicesActivatedList = (Set) myComponentKeys.get(eventKey);
				if (servicesActivatedList != null) {
					servicesActivatedList.add(arg0);
					if (log.isDebugEnabled()) {
						log.debug("Service "+arg0+" is activated and registred to event with key "+eventKey);
					}
				}
			}
		}		

	}

	public void serviceDeactivated(String arg0) {
		int[] eventIDs = (int[]) eventIDsOfServicesInstalled.get(arg0);
		if (eventIDs != null) {
			for(int i=0;i<eventIDs.length;i++) {
				EventTypeID eventTypeID = container.getEventTypeID(eventIDs[i]);
				ComponentKey eventKey = container.getEventKey(eventTypeID);
				Set servicesActivatedList = (Set) myComponentKeys.get(eventKey);
				if (servicesActivatedList != null) {
					servicesActivatedList.remove(arg0);		
					if (log.isDebugEnabled()) {
						log.debug("Service "+arg0+" was deactivated and unregistred to event with key "+eventKey);
					}
				}
			}
		}
	}

	public void serviceUninstalled(String arg0) {	
		eventIDsOfServicesInstalled.remove(arg0);
	}

	public void init(BootstrapContext bootstrapContext) throws javax.slee.resource.ResourceException {
	    if (log.isDebugEnabled()) {
	    	log.debug("init");
	    }
		this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();        
        // TODO use JSLEE 1.1 facilities
        state = ResourceAdaptorState.UNCONFIGURED;
    }

	public void configure() throws InvalidStateException {
	    if (log.isDebugEnabled()) {
	    	log.debug("configure");
	    }
	    if (this.state != ResourceAdaptorState.UNCONFIGURED) {
			throw new InvalidStateException("Cannot configure RA wrong state: " + this.state);
        }				    
		try {						
			state = ResourceAdaptorState.CONFIGURED;
		} catch (Exception ex){
		    log.error("Couldn't create the Xmpp Connection Factory",ex);		  
	   	}		
	}
	
	public void start() throws ResourceException {
	    if (log.isDebugEnabled()) {
	    	log.debug("start");
	    }
		try {            
            
			container = SleeContainer.lookupFromJndi();
			ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
                .getResourceAdaptorEnitity(this.bootstrapContext.getEntityName()));
			
			ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
                .getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();
			
			//event filtering			
			EventTypeID[] eventTypeIDs = resourceAdaptorEntity
                .getInstalledResourceAdaptor().getRaType().getRaTypeDescr().getEventTypes();
			for(int i=0;i<eventTypeIDs.length;i++){				
				ComponentKey eventKey = container.getEventKey(eventTypeIDs[i]);				
				myComponentKeys.put(eventKey,Collections.synchronizedSet(new HashSet()));				
			}			
			if (log.isDebugEnabled()) {				
				log.debug("Keys for this RA: "+myComponentKeys.keySet().toString());
			}
			
			this.acif = new XmppActivityContextInterfaceFactoryImpl(
                resourceAdaptorEntity.getServiceContainer(),
                this.bootstrapContext.getEntityName());
			resourceAdaptorEntity.getServiceContainer().getActivityContextInterfaceFactories().put(raTypeId, this.acif);			
			if (this.acif != null) {
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
				.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				if (log.isDebugEnabled()) {
					log.debug("jndiName prefix =" + prefix + "; jndiName = " + name);
				}
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}			
		}
        catch (Exception ex) {
            ex.printStackTrace();
            throw new ResourceException(ex.getMessage());
        }

        sbbInterface = new XmppRASbbInterfaceImpl();
        state = ResourceAdaptorState.ACTIVE;
	}

	public void stop() {
	    if (log.isDebugEnabled()) {
	    	log.debug("stop");
	    }
	    synchronized(activities) {	    
	    	for(Iterator i=activities.values().iterator(); i.hasNext(); ) {
	    		XmppConnection activity = (XmppConnection)i.next();
	    		XmppActivityHandle handle = new XmppActivityHandle(activity.getConnectionId());
	    		((XMPPConnection)activity.getConnection()).close();
	    		endActivity(handle);
	    	}
	    }
	    if (log.isDebugEnabled()) {
	    	log.debug("All activities ended.");
	    }
	    
	    try {
	    	if (this.acif != null) {
	    		String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
	    		.getJndiName();
	    		//remove "java:" prefix
	    		int begind = jndiName.indexOf(':');
	    		String javaJNDIName = jndiName.substring(begind + 1);
	    		SleeContainer.unregisterWithJndi(javaJNDIName);
	    	}	    	
        } catch (Exception e) {
            log.error("Can't unbind naming context",e);
        }

        this.sbbInterface = null;
        if (log.isDebugEnabled()) {
        	log.debug("Xmpp Resource Adaptor stopped.");
        }
	}

	public void stopping() {
	    if (log.isDebugEnabled()) {
	    	log.debug("stopping");
	    }
		state = ResourceAdaptorState.STOPPING;
	}

	public Object getFactoryInterface() {
		//TODO
	    if (log.isDebugEnabled()) {
	    	log.debug("getFactoryInterface");
	    }
		//return this.factory;
	    return null;
	}

	public Object getActivityContextInterfaceFactory() {
	    if (log.isDebugEnabled()) {
	    	log.debug("getActivityContextInterfaceFactory");
	    }
		return acif;
	}

	public void setResourceAdaptorEntity( ResourceAdaptorEntity resourceAdaptorEntity) {
	    //TODO
		if (log.isDebugEnabled()) {
	    	log.debug("setResourceAdaptorEntity");
	    }
	}
	        
    /* Receives an Event and sends it to the SLEE */
    public void processEvent(Object event, XmppActivityHandle handle){
        
    	if (log.isDebugEnabled()) {    		            		
    		log.debug("NEW XMPP-RA EVENT: " + event.getClass().getName());        
    	}
        
        ComponentKey key = (ComponentKey) XmppToSleeEvent.get(event.getClass().getName());

        if (key == null) {
            if (log.isDebugEnabled()) {
            	log.debug("key = null - "+event.getClass().getName());            
            }
            key = new ComponentKey( event.getClass().getName(), "org.jivesoftware.smack", "1.0");
            XmppToSleeEvent.put(event.getClass().getName(), key);
        }                  
                     
        //event filtering        
        Set servicesActivatedList = (Set) myComponentKeys.get(key);
        if(servicesActivatedList == null || servicesActivatedList.isEmpty()) {
        	if (log.isDebugEnabled()) {
        		log.debug("No services activated have interest in event type with key "+key.toString());
        	}        	 
        	return;
        }                
        
        if (log.isDebugEnabled()) {
        	log.debug("event lookup for type " + key.toString());
        }

        int eventID;
        try {
            eventID = eventLookup.getEventID(key.getName(), key.getVendor(), key.getVersion());
        } catch (FacilityException e2) {
            e2.printStackTrace();
            throw new RuntimeException("Failed to lookup event!", e2);
        } catch (UnrecognizedEventException e2) {
            e2.printStackTrace();
            throw new RuntimeException("Failed to lookup event!", e2);

        }
                
        if (eventID == -1) {
            // Drop the message because this is not a registered event type.        	
            log.error("Event lookup -- could not find event mapping -- check xml/slee-events.xml");
            return;
        }        
        
        try {        
        	sleeEndpoint.fireEvent(handle, event, eventID, null);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ActivityIsEndingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecognizedActivityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public Map getActivities() {
        if (log.isDebugEnabled()) {
        	log.debug("getActivities");
        }        
        return this.activities;
    }  
    
    
    
    /**
     * @return Returns the sleeEndpoint.
     */
    public SleeEndpoint getSleeEndpoint() {
        if (log.isDebugEnabled()) {
        	log.debug("getSleeEndpoint");
        }
        return sleeEndpoint;
    }

    /**
     * @param sleeEndpoint The sleeEndpoint to set.
     */
    public void setSleeEndpoint(SleeEndpoint sleeEndpoint) {
        if (log.isDebugEnabled()) {
        	log.debug("setSleeEndpoint");
        }
        this.sleeEndpoint = sleeEndpoint;
    }
        
	public void setMaxEventsPoolSize(int max) {
		if (log.isDebugEnabled()) {
			log.debug("setMaxEventsPoolSize, max="+max);
		}
		maxEventsPoolSize = max;
	}
	
	public int getMaxPoolEventSize() {
		if (log.isDebugEnabled()) {
			log.debug("getMaxPoolEventSize");
		}
		return maxEventsPoolSize;
	}
	
    public class XmppRASbbInterfaceImpl implements XmppResourceAdaptorSbbInterface {
    	    	
        public void sendPacket(String connectionID, Packet packet) {
	    	try {
	    		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
        		XmppConnection connection = (XmppConnection) activities.get(handle);
        		if (connection != null) {
        			((XMPPConnection)connection.getConnection()).sendPacket(packet);	    	   
	    	    	if (log.isDebugEnabled()) {
	    	    		log.debug(connectionID+" sent packet: "+packet.toXML());
	    	    	}
	    	    }
	    	}
	    	catch (Exception e) {
	    		log.error(e);
	    		e.printStackTrace();
	    	}
	    }	         
        
        public void connectClient(String connectionID, String serverHost, int serverPort, String serviceName, String username, String password, String resource, Collection packetFilters) throws XMPPException {
        	XmppActivityHandle handle = new XmppActivityHandle(connectionID);
        	if (activities.get(handle) == null) {
        		
        		log.info("Connecting to service "+serviceName+" at "+serverHost+":"+serverPort);        		        		        		         		
        		        		
				XMPPConnection connection = null;
				try {
					//create connection
					connection = new XMPPConnection(serverHost, serverPort, serviceName);					
					addListener(connectionID,connection,handle,packetFilters);
					// login
					connection.login(username,password,resource);
				} catch (Exception e) {
					log.info("Can't connect to service.");					   		
					throw new XMPPException(e);
				}
				
				// create activity
				createActivity(connectionID,connection,handle);		
        	}
        	else {
        		String e = "Connection already exists!";
        		log.info(e);
        		throw new XMPPException(e);
        	}
        }               
        
        public void connectComponent(String connectionID, String serverHost, int serverPort, String serviceName, String componentName, String componentSecret, Collection packetFilters) throws XMPPException {
        	XmppActivityHandle handle = new XmppActivityHandle(connectionID);
        	if (activities.get(handle) == null) {        		        		
        		
        		log.info("Opening Component XMPP connection to "+serverHost+" on port "+serverPort);         		
        		
        		//create connection
				XMPPConnection connection = null;
				try {
					connection = new ComponentXMPPConnection(componentName,componentSecret,serverHost,serverPort,serviceName);					
					//Obtain the ServiceDiscoveryManager associated with my XMPPConnection
					ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
					discoManager.setIdentityName("mobicents component");
					discoManager.setIdentityType("mobicents");
					discoManager.setIdentityCategory("component");
					addListener(connectionID,connection,handle,packetFilters);					
					log.info("XMPP Component connected.");
				} catch (Exception e) {
					log.info("XMPP Component NOT connected.");					
					throw new XMPPException(e);					
				}
				
		        // create activity
				createActivity(connectionID,connection,handle);				
        	}
        	else {
        		String e = "Connection already exists!";
        		log.info(e);        		
        		throw new XMPPException(e);
        	}
        }
        
        public void disconnect(String connectionID) {
        	try {	    	    
        		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
        		XmppConnection connection = (XmppConnection) activities.get(handle);
        		if (connection != null) {
        			((XMPPConnection)connection.getConnection()).close();        				    	    
        		}
        	}
        	catch (Exception e) {
        		log.error(e);
        		e.printStackTrace();
        	}
        }  
             
    }
    
    // aux priv methods
    
    private void addListener(String connectionID, XMPPConnection connection, XmppActivityHandle handle, Collection packetFilters) {
    	// create listener
    	XmppConnectionListener listener = new XmppConnectionListener(connectionID,handle);    	
    	// add packet filters if packetFilters is not null
    	if (packetFilters == null) {
    		connection.addPacketListener(listener, null);
    	}
    	else {
    		for(Iterator i=packetFilters.iterator();i.hasNext(); ) {        		
    			connection.addPacketListener(listener, new PacketTypeFilter((Class)i.next()));
    		}
    	}
    	// add connection listener
    	connection.addConnectionListener(listener);
    }
    
    private void createActivity(String connectionID, XMPPConnection connection, XmppActivityHandle handle) {
    	if (connection != null) {
    		XmppConnection activity = new XmppConnection(connectionID,connection);
    		activities.put(handle,activity);						
    		try {
    			sleeEndpoint.activityStarted(handle);
    		} catch (NullPointerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalStateException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ActivityAlreadyExistsException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (CouldNotStartActivityException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
        
    public class XmppConnectionListener implements PacketListener, ConnectionListener {
    	    	
    	private String connectionID = null;
		private XmppActivityHandle handle = null;
				
		public XmppConnectionListener(String connectionID, XmppActivityHandle handle) {			
			this.connectionID = connectionID;
			this.handle = handle;
		}
		
		public void processPacket(Packet packet) {
			if (log.isDebugEnabled()) {
				log.debug(connectionID+" received packet: "+packet.toXML());			
			}			
		    processEvent(packet,handle);    	        	        
		}

		public void connectionClosed() {				    	    
			XmppActivityHandle handle = new XmppActivityHandle(connectionID);
			XmppConnection connection = (XmppConnection) activities.get(handle);
			if (connection != null) {        			
				log.info("Got notification that connection with id="+ connectionID+" closed. Requesting ActivityEndEvent.");        			
				endActivity(handle);		    	    	
			}			
		}

		public void connectionClosedOnError(Exception arg0) {
			XmppActivityHandle handle = new XmppActivityHandle(connectionID);
			XmppConnection connection = (XmppConnection) activities.get(handle);
			if (connection != null) {        			
				log.info("Got notification that connection with id="+ connectionID+" closed on error. Requesting ActivityEndEvent.",arg0);        			
				endActivity(handle);		    	    	
			}						
		}
	}
}

