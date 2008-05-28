package org.mobicents.slee.resource.xcapclient;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.Address;
import javax.slee.InvalidStateException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientActivityContextInterfaceFactory;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.XCAPClient;
import org.openxdm.xcap.client.XCAPClientImpl;

/** 
 * @author Eduardo Martins
 * @version 2.0
 * 
 */

public class XCAPClientResourceAdaptor implements ResourceAdaptor,
		Serializable {
    
 	private static final long serialVersionUID = 1L;
	private static transient Logger logger = Logger.getLogger(XCAPClientResourceAdaptor.class);   
    
    private String XCAP_SERVER_HOST;
    private int XCAP_SERVER_PORT;
    private String XCAP_SERVER_ROOT;
   
    private ResourceAdaptorState state;    
    private transient ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl> activities = new ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl>();    
    private transient SleeEndpoint sleeEndpoint;
    private transient BootstrapContext bootstrapContext;
    private transient XCAPClientResourceAdaptorSbbInterface sbbInterface;
    private transient XCAPClientActivityContextInterfaceFactory acif;
    private transient XCAPClient client;  
    private transient int responseEventID;
    private transient ExecutorService executorService = Executors.newCachedThreadPool();
   
    public XCAPClientResourceAdaptor() { }
    
    public String getServerHost() {
        return XCAP_SERVER_HOST;
    }
    
    public void setServerHost(String serverHost) {
        this.XCAP_SERVER_HOST = serverHost;
    }
    
    public Integer getServerPort() {
        return Integer.valueOf(XCAP_SERVER_PORT);
    }
    
    public void setServerPort(Integer port) {
        this.XCAP_SERVER_PORT = port.intValue();
    }
    
    public String getXcapRoot() {
        return XCAP_SERVER_ROOT;
    }
    
    public void setXcapRoot(String xcapRoot) {
        this.XCAP_SERVER_ROOT = xcapRoot;
    }
    
    public void entityCreated(BootstrapContext ctx) throws javax.slee.resource.ResourceException {
    	//TODO
	    if (logger.isDebugEnabled()) {
	    	logger.debug("entityCreated");
	    }
	    this.init(ctx);
	}

	public void entityRemoved() {
		//TODO
	    if (logger.isDebugEnabled()) {
	    	logger.debug("entityRemoved");
	    }
	}

	public void entityActivated() throws ResourceException {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("entityActivated");
	    }
	    
	    try {
	    	this.configure();
	    } catch (InvalidStateException e1) {
	    	logger.warn(e1);
	    }
	    this.start();
        
	}

	public void entityDeactivating() {
		//TODO
		if (logger.isDebugEnabled()) {
			logger.debug("entityDeactivating");
		}
		this.stopping();

	}

	public void entityDeactivated() {
		if (logger.isDebugEnabled()) {
			logger.debug("entityDeactivated");
		}
		this.stop();
	}

	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {
		// TODO Auto-generated method stub
		
	}
	
	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	public void endActivity(ActivityHandle ah) {
		if (logger.isDebugEnabled()) {
			logger.debug("endActivity");
		}
		if (activities.containsKey(ah)) {
			// tell slee to end the activity
			try {
				this.sleeEndpoint.activityEnding(ah);
			} catch (Exception e) {
				logger.error("unable to end activity: ",e);
			}
		}
	}
	
	public void activityEnded(ActivityHandle ah) {		
		if (logger.isDebugEnabled()) {
			logger.debug("activityEnded(ActivityHandle="+ah+")");
		}
		// just remove the handle
	    activities.remove(ah);
	}

	public void activityUnreferenced(ActivityHandle ah) {		
		// ignore
	}

	public void queryLiveness(ActivityHandle ah) {
		// ignore			
	}

	public Object getActivity(ActivityHandle ah) {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("get Activity with ActivityHandle "+ah.toString());
	    }
	    // if handle exists then recreate activity
	    return activities.get(ah);
	}

	public ActivityHandle getActivityHandle(Object arg0) {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("getActivityHandle");
	    }
	    XCAPResourceAdaptorActivityHandle activityHandle = ((AsyncActivityImpl)arg0).getHandle();
	    if (activities.containsKey(activityHandle)) {
	    	return activityHandle;
	    }
	    else {
	    	return null;
	    }
	}

	public Object getSBBResourceAdaptorInterface(String arg0) {
		if (logger.isDebugEnabled()) {
			logger.debug("getSBBResourceAdaptorInterface");
		}
		return this.sbbInterface;
	}

	public Marshaler getMarshaler() {
		//TODO
		if (logger.isDebugEnabled()) {
			logger.debug("getMarshaler");
		}
		return null;
	}

	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {				
		// EVENT FILTERING IS NO GOOD FOR THIS RA	
	}

	public void serviceActivated(String arg0) {		
		// EVENT FILTERING IS NO GOOD FOR THIS RA
	}

	public void serviceDeactivated(String arg0) {
		// EVENT FILTERING IS NO GOOD FOR THIS RA
	}

	public void serviceUninstalled(String arg0) {	
		// EVENT FILTERING IS NO GOOD FOR THIS RA
	}

	public void init(BootstrapContext bootstrapContext) throws javax.slee.resource.ResourceException {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("init");
	    }	    
		this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();        
        try {
        	this.responseEventID = bootstrapContext.getEventLookupFacility().getEventID("ResponseEvent", "org.mobicents", "1.0");
        } catch (Exception e) {
        	throw new ResourceException(e.getMessage());
        }
        // TODO use JSLEE 1.1 facilities
        state = ResourceAdaptorState.UNCONFIGURED;
    }

	public void configure() throws InvalidStateException {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("configure");
	    }
	    if (this.state != ResourceAdaptorState.UNCONFIGURED) {
			throw new InvalidStateException("Cannot configure RA wrong state: " + this.state);
        }				    						
		state = ResourceAdaptorState.CONFIGURED;			
	}
	
	public void start() throws ResourceException {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("start");
	    }
		try {            
            
			SleeContainer container = SleeContainer.lookupFromJndi();
			ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
                .getResourceAdaptorEnitity(this.bootstrapContext.getEntityName()));
			
			ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
                .getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();			
			
			this.acif = new XCAPClientActivityContextInterfaceFactoryImpl(
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
				if (logger.isDebugEnabled()) {
					logger.debug("jndiName prefix =" + prefix + "; jndiName = " + name);
				}
				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
			// init client
			client = new XCAPClientImpl(XCAP_SERVER_HOST,XCAP_SERVER_PORT,XCAP_SERVER_ROOT);
			// create sbb interface
			 sbbInterface = new XCAPClientResourceAdaptorSbbInterfaceImpl(this);
		}
        catch (Exception ex) {
            ex.printStackTrace();
            throw new ResourceException(ex.getMessage());
        }

       
        state = ResourceAdaptorState.ACTIVE;
	}

	/**
	 * Stops this resource adaptor.
	 *
	 */
	public void stop() {
	   
		if (logger.isDebugEnabled()) {
	    	logger.debug("stop");
	    }
	    
		// end all activities
	    synchronized(activities) {	    
	    	for(XCAPResourceAdaptorActivityHandle handle : activities.keySet()) {   		
	    		endActivity(handle);
	    	}
	    }
	    if (logger.isDebugEnabled()) {
	    	logger.debug("All activities ended.");
	    }
	    
	    try {
	    	if (this.acif != null) {
	    		String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();
	    		//remove "java:" prefix
	    		int begind = jndiName.indexOf(':');
	    		String javaJNDIName = jndiName.substring(begind + 1);
	    		SleeContainer.unregisterWithJndi(javaJNDIName);
	    	}	    	
        } catch (Exception e) {
            logger.error("Can't unbind naming context",e);
        }

        this.sbbInterface = null;
        this.client.shutdown();
        this.client = null;
        this.executorService.shutdown();
        this.executorService = null;
        
        if (logger.isDebugEnabled()) {
        	logger.debug("XCAP Client RA Resource Adaptor stopped.");
        }
	}

	public void stopping() {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("stopping");
	    }
		state = ResourceAdaptorState.STOPPING;
	}

	public Object getActivityContextInterfaceFactory() {
	    if (logger.isDebugEnabled()) {
	    	logger.debug("getActivityContextInterfaceFactory");
	    }
		return acif;
	}

	public void setResourceAdaptorEntity( ResourceAdaptorEntity resourceAdaptorEntity) {
	    //TODO
		if (logger.isDebugEnabled()) {
	    	logger.debug("setResourceAdaptorEntity");
	    }
	}
	        
    /* Receives an Event and sends it to the SLEE */
    public void processResponseEvent(ResponseEvent event, XCAPResourceAdaptorActivityHandle handle){
        
    	if (logger.isDebugEnabled()) {    		            		
    		logger.debug("NEW RESPONSE EVENT");        
    	}
                        
        try {                	
        	sleeEndpoint.fireEvent(handle, event, responseEventID, null);        	
        } catch (Exception e) {           
            logger.warn("unable to fire event",e);
        }        
    }
    
    protected ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl> getActivities() {
		return activities;
	} 
        
    /**
     * @return Returns the sleeEndpoint.
     */
    public SleeEndpoint getSleeEndpoint() {
        if (logger.isDebugEnabled()) {
        	logger.debug("getSleeEndpoint");
        }
        return sleeEndpoint;
    }

    /**
     * @param sleeEndpoint The sleeEndpoint to set.
     */
    public void setSleeEndpoint(SleeEndpoint sleeEndpoint) {
        if (logger.isDebugEnabled()) {
        	logger.debug("setSleeEndpoint");
        }
        this.sleeEndpoint = sleeEndpoint;
    }
        
	public XCAPClient getClient() {
		return client;
	}
    
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
}

