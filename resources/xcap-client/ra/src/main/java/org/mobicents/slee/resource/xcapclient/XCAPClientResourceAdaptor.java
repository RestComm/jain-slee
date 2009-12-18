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
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.SleeEndpoint;

import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.XCAPClient;
import org.openxdm.xcap.client.XCAPClientImpl;

/** 
 * @author Eduardo Martins
 * @author aayush.bhatnagar
 * @version 2.0
 * 
 */

public class XCAPClientResourceAdaptor implements javax.slee.resource.ResourceAdaptor,
		Serializable {
    
 	private static final long serialVersionUID = 1L;
	//private static transient Logger logger = Logger.getLogger(XCAPClientResourceAdaptor.class);   
    
    private String XCAP_SERVER_HOST;
    private int XCAP_SERVER_PORT;
    private String XCAP_SERVER_ROOT;
   
    //private ResourceAdaptorState state;    
    private  ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl> activities;    
    
    private  XCAPClientResourceAdaptorSbbInterface sbbInterface;
  //private transient XCAPClientActivityContextInterfaceFactory acif;
    private  XCAPClient client;
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
    private FireableEventType responseEvent;
   
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
    
//    public void entityCreated(ResourceAdaptorContext ctx) throws Exception {
//    	//TODO
//	    if (tracer.isFineEnabled()) {
//	    	tracer.fine("entityCreated");
//	    }
//	    this.init(ctx);
//	}

//	public void entityRemoved() {
//		//TODO
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("entityRemoved");
//	    }
//	}

//	public void entityActivated() throws Exception {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("entityActivated");
//	    }
//	    
//	    try {
//	    	this.configure();
//	    } catch (InvalidStateException e1) {
//	    	logger.warn(e1);
//	    }
//	    this.start();
//        
//	}

//	public void entityDeactivating() {
//		//TODO
//		if (tracer.isFineEnabled()) {
//			tracer.fine("entityDeactivating");
//		}
//		this.stopping();
//
//	}

//	public void entityDeactivated() {
//		if (tracer.isFineEnabled()) {
//			tracer.fine("entityDeactivating");
//		}
//		this.stop();
//	}

	//done
	public void endActivity(ActivityHandle ah) {
		if (tracer.isFineEnabled()) {
			tracer.fine("endActivity");
		}
		
		if (activities.containsKey(ah)) {
			// tell slee to end the activity
			try {
				//this.sleeEndpoint.activityEnding(ah);
				this.sleeEndpoint.endActivity(ah);
			} catch (Exception e) {
				tracer.severe("unable to end activity: ",e);
			}
		}
	}
	//done
	public void activityEnded(ActivityHandle ah) {		
		if (tracer.isFineEnabled()) {
			tracer.fine("activityEnded( handle = "+ah+")");
		}
		// just remove the handle
	    activities.remove(ah);
	}

	//done
	public void activityUnreferenced(ActivityHandle ah) {		
		// ignore
	}

	//done
	public void queryLiveness(ActivityHandle ah) {
		// guard against activity leaks:
		if (!activities.contains(ah)) {
			raContext.getSleeEndpoint().endActivity(ah);
		}			
	}

	//done
	public Object getActivity(ActivityHandle ah) {
	    if (tracer.isFineEnabled()) {
	    	tracer.fine("get Activity with ActivityHandle "+ah.toString());
	    }
	    // if handle exists then recreate activity
	    return activities.get(ah);
	}

	//done
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

//	public Object getSBBResourceAdaptorInterface(String arg0) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("getSBBResourceAdaptorInterface");
//		}
//		return this.sbbInterface;
//	}

	public Marshaler getMarshaler() {
		//TODO
		if (tracer.isFineEnabled()) {
			tracer.fine("getMarshaler");
		}
		return null;
	}

//	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {				
//		// EVENT FILTERING IS NO GOOD FOR THIS RA	
//	}
//
//	public void serviceActivated(String arg0) {		
//		// EVENT FILTERING IS NO GOOD FOR THIS RA
//	}
//
//	public void serviceDeactivated(String arg0) {
//		// EVENT FILTERING IS NO GOOD FOR THIS RA
//	}
//
//	public void serviceUninstalled(String arg0) {	
//		// EVENT FILTERING IS NO GOOD FOR THIS RA
//	}

//	public void init(ResourceAdaptorContext raContext) throws Exception {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("init");
//	    }	    
//		this.raContext = raContext;
//        this.sleeEndpoint = raContext.getSleeEndpoint();        
//        try {
//        	//this.responseEventID = raContext.getEventLookupFacility().getEventID("ResponseEvent", "org.mobicents", "1.0");
//        	
//        } catch (Exception e) {
//        	throw new Exception(e.getMessage());
//        }
//         TODO use JSLEE 1.1 facilities
//        state = ResourceAdaptorState.UNCONFIGURED;
//    }

//	public void configure() throws InvalidStateException {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("configure");
//	    }
//	    if (this.state != ResourceAdaptorState.UNCONFIGURED) {
//			throw new InvalidStateException("Cannot configure RA wrong state: " + this.state);
//        }				    						
//		state = ResourceAdaptorState.CONFIGURED;			
//	}
	
//	public void start() throws Exception {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("start");
//	    }
//	        SleeContainer container = SleeContainer.lookupFromJndi();
//			ResourceAdaptorEntity resourceAdaptorEntity =container.getResourceManagement().
//			getResourceAdaptorEntity(this.raContext.getEntityName());
//			
//			ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
//                .getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();			
			
//			this.acif = new XCAPClientActivityContextInterfaceFactoryImpl(
//                resourceAdaptorEntity.getServiceContainer(),
//                this.raContext.getEntityName());
			//resourceAdaptorEntity.getServiceContainer().getActivityContextInterfaceFactories().put(raTypeId, this.acif);			
//			if (this.acif != null) {
//				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
//				.getJndiName();
//				int begind = jndiName.indexOf(':');
//				int toind = jndiName.lastIndexOf('/');
//				String prefix = jndiName.substring(begind + 1, toind);
//				String name = jndiName.substring(toind + 1);
//				if (logger.isDebugEnabled()) {
//					logger.debug("jndiName prefix =" + prefix + "; jndiName = " + name);
//				}
//				SleeContainer.registerWithJndi(prefix, name, this.acif);
//			}
			
//		}
	/**
	 * Stops this resource adaptor.
	 *
	 */
//	public void stop() {
//	   
//		if (logger.isDebugEnabled()) {
//	    	logger.debug("stop");
//	    }
//	    
//		// end all activities
//	    synchronized(activities) {	    
//	    	for(XCAPResourceAdaptorActivityHandle handle : activities.keySet()) {   		
//	    		endActivity(handle);
//	    	}
//	    }
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("All activities ended.");
//	    }
//	    
//	    try {
//	    	if (this.acif != null) {
//	    		String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();
//	    		//remove "java:" prefix
//	    		int begind = jndiName.indexOf(':');
//	    		String javaJNDIName = jndiName.substring(begind + 1);
//	    		SleeContainer.unregisterWithJndi(javaJNDIName);
//	    	}	    	
//        } catch (Exception e) {
//            logger.error("Can't unbind naming context",e);
//        }

//        this.sbbInterface = null;
//        this.client.shutdown();
//        this.client = null;
//        this.executorService.shutdown();
//        this.executorService = null;
        
//        if (logger.isDebugEnabled()) {
//        	logger.debug("XCAP Client RA Resource Adaptor stopped.");
//        }
//	}
//
//	public void stopping() {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("stopping");
//	    }
//		//state = ResourceAdaptorState.STOPPING;
//	}

//	public Object getActivityContextInterfaceFactory() {
//	    if (logger.isDebugEnabled()) {
//	    	logger.debug("getActivityContextInterfaceFactory");
//	    }
//		return acif;
//	}

//	public void setResourceAdaptorEntity( ResourceAdaptorEntity resourceAdaptorEntity) {
//	    //TODO
//		if (logger.isDebugEnabled()) {
//	    	logger.debug("setResourceAdaptorEntity");
//	    }
//	}
	        
    /* Receives an Event and sends it to the SLEE */
	//done
    public void processResponseEvent(ResponseEvent event, XCAPResourceAdaptorActivityHandle handle){
        
    	if (tracer.isFineEnabled()) {    		            		
    		tracer.fine("NEW RESPONSE EVENT");        
    	}
                        
        try {                	
        	//sleeEndpoint.fireEvent(handle, event, responseEventID, null);
            sleeEndpoint.fireEvent(handle,responseEvent, event, null, null);
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
    //done
    public SleeEndpoint getSleeEndpoint() {
        if (tracer.isFineEnabled()) {
        	tracer.fine("Returning the SLEE Endpoint handle");
        }
        return sleeEndpoint;
    }
 
    /**
     * 
     * @return the XCAP client API handle
     */
    //done
	public XCAPClient getClient() {
		return client;
	}
    
	/**
	 * 
	 * @return the executor service for this RA
	 */
	//done
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	/**
	 * To add the newly created activity to the activity map
	 * @param activityHandle
	 * @param activity
	 */
	//done
	public void addActivity(XCAPResourceAdaptorActivityHandle activityHandle,
			AsyncActivityImpl activity) {
		activities.put(activityHandle, activity);
	}
	
	/**
     * 
     * @param tracerName
     * @return
     */
	//done
    public Tracer getTracer(String tracerName) {
            return this.tracer;
    }
    
    /**
     * 
     * @return the resource adaptor context.
     */
    //done
    public ResourceAdaptorContext getXCAPResourceAdaptorContext()
    {
    	return this.raContext;
    }

    // JAIN SLEE 1.1 XCAP RA Callbacks:
    
    //done
	public void administrativeRemove(ActivityHandle arg0) {
    // not done for this RA
		
	}

	//done
	public void eventProcessingFailed(ActivityHandle ah,
			FireableEventType arg1, Object arg2, Address ad,
			ReceivableService arg4, int arg5, FailureReason fr) {
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Event processing failed for:\n"+
					"Activity Handle: "+ah+
					"Address: "+ad+" with\n"+
					"Failure Reason: "+fr);
		}
		
	}

	//done
	public void eventProcessingSuccessful(ActivityHandle ah,
			FireableEventType arg1, Object arg2, Address ad,
			ReceivableService arg4, int arg5) {
		
		if (tracer.isFineEnabled()) {
			tracer.fine("Event processing successful for:\n"+
					"Activity Handle: "+ah+
					"Address: "+ad+" with\n");
		}
		
	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	public Object getResourceAdaptorInterface(String arg0) {
		// TODO Auto-generated method stub
		return this.sbbInterface;
	}

	//done
	public void raActive() {
		// init client
		try {
			client = new XCAPClientImpl(XCAP_SERVER_HOST,XCAP_SERVER_PORT,XCAP_SERVER_ROOT);
		} catch (InterruptedException e) {
			tracer.severe("Unable to init XCAP Client API");
			e.printStackTrace();
		}
		// create sbb interface
		 sbbInterface = new XCAPClientResourceAdaptorSbbInterfaceImpl(this);
		activities = new ConcurrentHashMap<XCAPResourceAdaptorActivityHandle, AsyncActivityImpl>();
		executorService = Executors.newCachedThreadPool();
		sbbInterface = new XCAPClientResourceAdaptorSbbInterfaceImpl(this);
		tracer.info("XCAP Client RA entity has been activated.");
		
		
	}

	//done
	public void raConfigurationUpdate(ConfigProperties arg0) {
		//no configuration
		
	}
 
	//done
	public void raConfigure(ConfigProperties arg0) {
		//no config
		
	}

	//done
	public void raInactive() {
		// end all activities
	    synchronized(activities) {	    
	    	for(XCAPResourceAdaptorActivityHandle handle : activities.keySet()) {   		
	    		endActivity(handle);
	    	}
	    }
		activities.clear();
		activities = null;
		client.shutdown();
		client=null;
		executorService.shutdown();
		executorService = null;
		sbbInterface = null;
		
	}

	//done
	public void raStopping() {
		// not implmented for this RA
		
	}

	//done
	public void raUnconfigure() {
		// not impl for this RA
		
	}

	//done
	public void raVerifyConfiguration(ConfigProperties arg0)
			throws InvalidConfigurationException {
		// no verification for this RA.
		
	}

	//done
	public void serviceActive(ReceivableService arg0) {
		// no event filtering
		
	}

	//done
	public void serviceInactive(ReceivableService arg0) {
		//no event filtering
		
	}

	//done
	public void serviceStopping(ReceivableService arg0) {
		//no event filtering
		
	}
    // done
	public void setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext raContext) {
		this.raContext = raContext; 
		this.tracer = raContext.getTracer("XCAPClientResourceAdaptor");
		
        this.sleeEndpoint = raContext.getSleeEndpoint();
        this.eventLookupFacility = raContext.getEventLookupFacility();
        
        try {
        	this.responseEvent = eventLookupFacility.getFireableEventType
        	(new EventTypeID("ResponseEvent", "org.mobicents", "1.0"));
            }
				catch(Exception e)
				{
					if(tracer.isWarningEnabled())
						tracer.warning("Could not look up Response Event");
					e.printStackTrace();
				}	
	}
	//done
	public void unsetResourceAdaptorContext()
	{
		this.raContext = null;
		this.tracer = null;
		this.sleeEndpoint = null;
		this.eventLookupFacility = null;
	}
	
}

