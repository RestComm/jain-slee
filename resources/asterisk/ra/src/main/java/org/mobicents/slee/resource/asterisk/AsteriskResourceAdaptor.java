/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under GPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.asterisk;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

//import net.sf.asterisk.manager.ManagerConnection;
//import net.sf.asterisk.manager.ManagerConnectionFactory;
//import net.sf.asterisk.manager.ManagerEventHandler;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;
//import net.sf.asterisk.manager.TimeoutException;
//import net.sf.asterisk.manager.action.ManagerAction;
//import net.sf.asterisk.manager.event.ManagerEvent;
//import net.sf.asterisk.manager.response.ManagerResponse;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;

/**
 * The main Asterisk Resource Adaptor class.Provides to SLEE the AsteriskManager interface.
 * 
 * @author Sancho
 * @version 1.0
 * 
 */
public class AsteriskResourceAdaptor implements /*ManagerEventHandler*/ManagerEventListener, ResourceAdaptor,
		Serializable {
    
	private static final long serialVersionUID = -8460649793374940252L;
	
	static private transient Logger log;

    static {
        log = Logger.getLogger(AsteriskResourceAdaptor.class);
    }
    
    private ResourceAdaptorState state;
    private Properties properties;
    private ManagerConnection managerConnection;
    private transient HashMap activities;
    
    private String MAGIIP = "0.0.0.0";
    private String MAGILogin = "admin";
    private String MAGIPassword = "amp111";
    private int responseTimeOut = 300000;
    
    private transient SleeEndpoint sleeEndpoint;
    private transient Object eventObj;
    private transient Object activityObj;
    private transient Address address;
    private transient EventLookupFacility eventLookup;
    private transient BootstrapContext bootstrapContext;
    private transient ManagerConnectionFactory factory;
    private transient AsteriskRASbbInterfaceImpl sbbInterface;
    private transient AsteriskActivityContextInterfaceFactory acif;
    private transient HashMap AsteriskToSleeEvent;
    
    public AsteriskResourceAdaptor() {
    	AsteriskToSleeEvent = new HashMap();   
    }
    
    /* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#entityCreated(javax.slee.resource.BootstrapContext)
	 */
	public void entityCreated(BootstrapContext ctx) throws javax.slee.resource.ResourceException {
		// TODO Auto-generated method stub
		this.init(ctx);
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#entityRemoved()
	 */
	public void entityRemoved() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#entityActivated()
	 */
	public void entityActivated() throws ResourceException {
		// TODO Auto-generated method stub
        try {
            try {
                this.configure(null);
            } catch (InvalidStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            this.start();
        } catch (ResourceException e) {
            e.printStackTrace();
            throw new javax.slee.resource.ResourceException("Failed to Activate Resource Adaptor!", e);
        }
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#entityDeactivating()
	 */
	public void entityDeactivating() {
		this.stopping();

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#entityDeactivated()
	 */
	public void entityDeactivated() {
		this.stop();
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle, java.lang.Object, int, javax.slee.Address, int)
	 */
	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle, java.lang.Object, int, javax.slee.Address, int, javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
	 */
	public void activityEnded(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
	 */
	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
	 */
	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
	 */
	public Object getActivity(ActivityHandle handle) {
		// TODO Auto-generated method stub
		return this.activities.get(handle);
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
	 */
	public ActivityHandle getActivityHandle(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getSBBResourceAdaptorInterface(java.lang.String)
	 */
	public Object getSBBResourceAdaptorInterface(String arg0) {
		// TODO Auto-generated method stub
		return this.sbbInterface;
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
	 */
	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceInstalled(java.lang.String, int[], java.lang.String[])
	 */
	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceUninstalled(java.lang.String)
	 */
	public void serviceUninstalled(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceActivated(java.lang.String)
	 */
	public void serviceActivated(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.resource.ResourceAdaptor#serviceDeactivated(java.lang.String)
	 */
	public void serviceDeactivated(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#init(org.mobicents.slee.resource.ResourceAdaptorContext)
	 */
	public void init(BootstrapContext bootstrapContext)
			throws javax.slee.resource.ResourceException
			 {
		// TODO Auto-generated method stub
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();

        factory = new ManagerConnectionFactory(MAGIIP,MAGILogin,MAGIPassword);
        state = ResourceAdaptorState.UNCONFIGURED;
        
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#configure(java.util.Properties)
	 */
	public void configure(Properties properties) throws InvalidStateException {
		if (this.state != ResourceAdaptorState.UNCONFIGURED) {
			throw new InvalidStateException("Cannot configure RA wrong state: " + 
			   this.state);
        }
		
		log.debug("Configure routine - Going to load properties");
		// Tries to get the properties from a file
		this.properties = loadProperties(bootstrapContext);
		this.MAGIIP = this.properties.getProperty("MAGIIP","0.0.0.0");
		this.MAGILogin = this.properties.getProperty("MAGILogin","manager");
		this.MAGIPassword = this.properties.getProperty("MAGIPassword", "secret");
		this.responseTimeOut = Integer.parseInt(this.properties.getProperty(
                "responseTimeout","300000"));
		
		log.debug("Configure routine - Loaded properties => " + this.properties);
		// Creates and configures a new connection
		this.factory = new ManagerConnectionFactory(MAGIIP,MAGILogin,MAGIPassword);
	        
		try {
//			this.managerConnection = this.factory.getManagerConnection( this.MAGIIP,
//	        		this.MAGILogin, this.MAGIPassword);
//	   
//			managerConnection.addEventHandler(this);
			this.managerConnection = this.factory.createManagerConnection();
	   
			managerConnection.addEventListener(this);
			
			state = ResourceAdaptorState.CONFIGURED;
	   
	   } catch (Exception ex){
	   	log.error("Couldn't create the Asterisk Connection Factory");
	   	System.exit(0);
	   	}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#start()
	 */
	public void start() throws ResourceException {
		// TODO Auto-generated method stub
        try {
        	log.info("Login to " + this.MAGIIP + " with " + this.MAGILogin + " " + this.MAGIPassword);
            
            managerConnection.login();
            
            log.debug("Asterisk Resource Adaptor started.");
      
            initializeNamingContext();
        }
        catch (Exception ex) {
            ex.printStackTrace();

            throw new ResourceException(ex.getMessage());
        }

        sbbInterface = new AsteriskRASbbInterfaceImpl();
        activities = new HashMap();
        state = ResourceAdaptorState.ACTIVE;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#stop()
	 */
	public void stop() {
		// TODO Auto-generated method stub
		try{
		managerConnection.logoff();
		} 
//    	catch (TimeoutException ex){
//    		handleTimeout(ex);
//    	}
		catch (Exception tiex){ // later change this to deal with timeouts
		log.error("Exception: " + tiex);
			
		}
		
        try {
            cleanNamingContext();
        } catch (NamingException e) {
            log.error("Can't unbind naming context");
        }

        this.sbbInterface = null;
        this.factory = null;
		
        log.debug("Asterisk Resource Adaptor stopped.");
		
}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#stopping()
	 */
	public void stopping() {
		// TODO Auto-generated method stub
		state = ResourceAdaptorState.STOPPING;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#getFactoryInterface()
	 */
	public Object getFactoryInterface() {
		return this.factory;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#getActivityContextInterfaceFactory()
	 */
	public Object getActivityContextInterfaceFactory() {
		// TODO Auto-generated method stub
		return acif;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptor#setResourceAdaptorEntity(org.mobicents.slee.resource.ResourceAdaptorEntity)
	 */
	public void setResourceAdaptorEntity( ResourceAdaptorEntity resourceAdaptorEntity) {
		// TODO Auto-generated method stub
	}
	
    private Properties loadProperties(BootstrapContext bootstrapContext) {
        Properties properties = new Properties();

        // Load default values
        try {
            properties.load(getClass().getResourceAsStream("asteriskra.properties"));
            log.error("loadProperties asteriskra.properties");
            
        } catch (java.io.IOException ex) {
            // Silently set default values
            log.error("Couldn't load properties");
        }
        
        return properties;
    }
	
	/* Receives an Event from Asterisk and sends it to the SLEE */
    public void onManagerEvent(ManagerEvent event)
    {
        // just print received events
    	log.info("Event Handler: " + event);
            
        if (event.getClass().getName().equals("org.asteriskjava.manager.event.ConnectEvent")){
        	log.info("ConnectEvent Temporary anti-Exception measure");
        	return;
        }
        
        processEvent(event);
      
    }

	/* Receives a Response from Asterisk  */
    public void handleResponse(ManagerResponse event) {
        // just print received events
    	log.info("Response Handler: " + event);
        processEvent(event);
        
    }
    
    /* Deals with TimeoutExceptions */
    public void handleTimeout(TimeoutException event) {
    	
        log.info("TimeoutHandler: " + event);
        processEvent(event);
    }
    
    /* Receives an Event and sends it to the SLEE */
    public void processEvent(Object event){
        
        log.debug("------------------ NEW ASTERISK-RA EVENT -----------------------");
    	log.debug("Processing event: " + event.getClass().getName());
    	
        ComponentKey key = (ComponentKey) AsteriskToSleeEvent.get(event.getClass().getName());
                
        if (key == null) {
        	key = new ComponentKey( event.getClass().getName(), "org.asteriskjava", "1.0");
        	AsteriskToSleeEvent.put(event.getClass().getName(), key);
        }

        log.debug(event.getClass().getName());
        log.debug(key);
        eventObj = event;

        javax.slee.Address address;
        address = new Address(AddressPlan.IP, this.MAGIIP );

        AsteriskManagerMessage actobj = new AsteriskManagerMessage(event,managerConnection);

        AsteriskActivityHandle handle = new AsteriskActivityHandle(actobj.MessageID());
        activities.put(handle, actobj);
        
        log.debug("event lookup for " + key.getName() + " vendor = " +
            key.getVendor() + " version = " + key.getVersion());

        int eventID;
        try {
            eventID = eventLookup.getEventID(key.getName(), key.getVendor(),
                    key.getVersion());
        } catch (FacilityException e2) {
            e2.printStackTrace();
            throw new RuntimeException("Failed to lookup event!", e2);
        } catch (UnrecognizedEventException e2) {
            e2.printStackTrace();
            throw new RuntimeException("Failed to lookup event!", e2);

        }
        
        if (eventID == -1) {
            // Silently drop the message because this is not a registered event type.
            log.debug(
                "event lookup -- could not find event mapping -- check xml/slee-events.xml");
            return;
        }

        try {
            log.info("Resource adaptor delivering event:\n" + eventID + "\n" + address);
            sleeEndpoint.fireEvent(new AsteriskActivityHandle(actobj.MessageID()), eventObj, eventID, address);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (ActivityIsEndingException e) {
            e.printStackTrace();
        } catch (UnrecognizedActivityException e) {
            e.printStackTrace();
        }
        
        try {
            sleeEndpoint.activityEnding(handle);
        } catch (Exception e) {
            log.error("Could not end activity: " + actobj);
        }
        
        
    }
    
    private void cleanNamingContext() throws NamingException { 
    }
   
    public HashMap getActivities() {
        // TODO Auto-generated method stub
        return this.activities;
    }
	
    private void initializeNamingContext() throws NamingException {
        SleeContainer container = SleeContainer.lookupFromJndi();
        ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container.getResourceAdaptorEnitity(this.bootstrapContext.getEntityName()));
        ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity.getInstalledResourceAdaptor()
                                                              .getRaType()
                                                              .getResourceAdaptorTypeID();
        
        // TODO: need to comeup w/ generic way to add JNDI interface.  This code is moving
        // from the ServiceContainer to here
        
        this.acif = new AsteriskActivityContextInterfaceFactoryImpl(resourceAdaptorEntity.getServiceContainer(), this.bootstrapContext.getEntityName());
        
        resourceAdaptorEntity.getServiceContainer()
                             .getActivityContextInterfaceFactories().put(raTypeId,
            this.acif);
        resourceAdaptorEntity.getServiceContainer().registerWithJndi("slee/resources",
            "asteriskacif", this.acif);

    }
    
    /**
     * @return Returns the sleeEndpoint.
     */
    public SleeEndpoint getSleeEndpoint() {
        return sleeEndpoint;
    }

    /**
     * @param sleeEndpoint The sleeEndpoint to set.
     */
    public void setSleeEndpoint(SleeEndpoint sleeEndpoint) {
        this.sleeEndpoint = sleeEndpoint;
    }

    public class AsteriskRASbbInterfaceImpl implements AsteriskResourceAdaptorSbbInterface {
    	
	    public void sendAction(ManagerAction action){
	    	
	    	ManagerResponse managerResponse;
	    	
	    	try {
	    		managerResponse = managerConnection.sendAction(action,responseTimeOut);
	    		handleResponse(managerResponse);
	    	} 
	    	catch (TimeoutException ex){
	    		handleTimeout(ex);
	    	}
	    	catch (Exception ex) {
	    	log.error(ex);
	    	}
	    }
    
    }
}


