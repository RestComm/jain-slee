/*
 * MgcpResourceAdaptor.java
 *
 * Media Gateway Control Protocol (MGCP) Resource Adaptor.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.AuditConnection;
import jain.protocol.ip.mgcp.message.AuditEndpoint;
import jain.protocol.ip.mgcp.message.Constants;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.DeleteConnectionResponse;
import jain.protocol.ip.mgcp.message.EndpointConfiguration;
import jain.protocol.ip.mgcp.message.ModifyConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.RestartInProgress;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.event.TransactionTimeout;

import org.apache.log4j.Logger;
import org.mobicents.mgcp.JainMgcpProviderImpl;
import org.mobicents.mgcp.JainMgcpStackImpl;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

/**
 *
 * @author Oleg Kulikov
 * @author eduardomartins
 */
public class MgcpResourceAdaptor implements ResourceAdaptor, Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -3161437298088878963L;
    
	/**
	 * the manager of mgcp activities of this ra
	 */
    private MgcpActivityManager mgcpActivityManager = new MgcpActivityManager();
    
    /**
     * The goal of the resource adaptor architecture is to define a management
     * contract between a SLEE application server and a resource adaptor that
     * allows the SLEE to manage to lifecycle of the resource adaptor. This
     * contract enables a SLEE to bootstrap a resource instance at deployment
     * time or during application server startup and to notify the resource
     * adaptor of its undeployment or during the orderly shutdown of the SLEE.
     */
    private BootstrapContext bootstrapContext;
    
    /**
     * This interface defines the methods that are required to represent a
     * proprietary JAIN MGCP protocol stack, the implementation of which will be
     * vendor-specific. Methods are defined for creating and deleting instances
     * of a JainMgcpProvider.
     */
    private JainMgcpStackImpl stack;
    
    /**
     * A MGCP resource object refers to an object created by a resource adaptor
     * entity. An SBB can obtain access to a resource object looking up a
     * resource object in the JNDI component environment of the SBB.
     */
    private JainMgcpProviderImpl mgcpProvider;
    
    /**
     * The SLEE endpoint defines the contract between the SLEE and the resource
     * adaptor that enables the resource adaptor to deliver events asynchronously
     * to SLEE endpoints residing in the SLEE. This contract serves as a generic
     * contract that allows a wide range of resources to be plugged into a SLEE
     * environment via the resource adaptor architecture.
     *
     * For further information see JSLEE v1.1 Specification, Early Draft Review
     * Page 307.
     * The sleeEndpoint will be initialized in entityCreated() method.
     */
    private transient SleeEndpoint sleeEndpoint = null;
    
    /**
     * EventLookupFacility is used to look up the event id of incoming events
     */
    private transient EventLookupFacility eventLookup = null;
    
    private Integer port = new Integer(2728);
    private String localAddress;
    
    private Logger logger = Logger.getLogger(MgcpResourceAdaptor.class);
    
    private MgcpActivityContextInterfaceFactoryImpl acif;
    
    /** Creates a new instance of MgcpResourceAdaptor */
    public MgcpResourceAdaptor() {
    }
    
    public Integer getPort() {
        return port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
    }
    
    public MgcpActivityManager getMgcpActivityManager() {
		return mgcpActivityManager;
	}
    
    public JainMgcpStackImpl getStack() {
		return stack;
	}
    
    /**
     * This method is called by the SLEE when a resource adaptor object instance
     * is bootstrapped, either when a resource adaptor entity is created or
     * during SLEE startup. The configuration properties of the resource adaptor
     * entity are set before this method is invoked. The SLEE implementation
     * will construct the resource
     *
     * @param bootstrapContext contains references to the SLEE endpoint and the
     * facilities that may be used by the resource adaptor instance.
     */
    public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
        if(logger.isInfoEnabled()) {
        	logger.info("RA entity "+bootstrapContext.getEntityName()+" created");
        }
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
        this.eventLookup = bootstrapContext.getEventLookupFacility();
    }
    
    /**
     * This method is called by the SLEE when a resource adaptor object instance
     * is being removed, either when a resource adaptor entity is deleted or
     * during SLEE shutdown. When receiving this invocation the resource adaptor
     * object is expected to close any system resources it has allocated.
     * The SLEE may garbage collect the resource adaptor object at this time.
     */
    public void entityRemoved() {
    }
    
    /**
     * This method is called by the SLEE to notify a resource adaptor object
     * that the resource adaptor entity is being activated. Before this method
     * is invoked the SLEE transitions the SleeEndpoint associated with the
     * resource adaptor object to the Active state. This allows the resource
     * adaptor object to fire events to the SLEE for new activities during the
     * entityActivated method invocation. If this method returns successfully,
     * the resource adaptor entity transitions to the Active state.
     */
    public void entityActivated() throws ResourceException {
        try {
            localAddress = InetAddress.getLocalHost().getHostName() + ":" + port;
        } catch (UnknownHostException e) {
            //should never happen
        }
        
        try {
        	mgcpProvider = new JainMgcpProviderImpl(this);
        	stack = new JainMgcpStackImpl(mgcpProvider,port.intValue());
        } catch (Exception e) {
            throw new ResourceException(e.getMessage());
        }
        
        initializeNamingContext();
        if(logger.isInfoEnabled()) {
        	logger.info("RA entity "+bootstrapContext.getEntityName()+" activated");
        }
    }
    
    /**
     * The method is called by the SLEE to notify the resource adaptor object
     * that the resource adaptor entity is deactivating. On receipt of this
     * invocation the resource adaptor object is expected to perform any
     * internal transitions necessary such that it does not create any new
     * activities.
     *
     * Remove this resource adaptor entity from provider's listeners.
     */
    public void entityDeactivating() {
        
    }
    
    /**
     * This method is called by the SLEE to notify the resource adaptor object
     * that the SLEE is not aware of any activities owned by the resource
     * adaptor entity and thus is transitioning the resource adaptor entity
     * from the Stopping state to the Inactive state.
     */
    public void entityDeactivated() {
        clearNamingContext();
        stack.close();
        stack = null;
        mgcpProvider = null;
        if(logger.isInfoEnabled()) {
        	logger.info("RA entity "+bootstrapContext.getEntityName()+" deactivated");
        }
    }
    
    /**
     * The SLEE calls this method to inform the resource adaptor object that the
     * specified event was processed successfully by the SLEE. An event is
     * considered to be processed successfully if the SLEE has attempted to
     * deliver the event to all interested SBBs. It is not required that the SBB
     * event handler or SBB rolled back transactions commit for event processing
     * to still be successful, unless failure to commit is caused by a system
     * level failure. This method need not be called by the same thread that
     * invoked the fireEvent method for this event.
     *
     * @param handle the activity handle of the activity upon which the event was fired.
     * @param event is the event fired on the activity. This value may be null
     * if no actual event was fired.
     * @param eventID the unique identifier of the event type fired to the SLEE.
     * If the event argument is null, the value of this parameter is undefined.
     * @param address the default address on which the event was fired. If a null
     * default address was specified, this value is also null. If the event
     * argument is null, the value of this parameter is undefined.
     * @param flags the necessary information that enables any post-event
     * processing required by the resource adaptor.
     */
    public void eventProcessingSuccessful(ActivityHandle handle, Object event,
            int eventID, Address address, int flags) {
    }
    
    /**
     * The SLEE calls this method to inform the resource adaptor object that
     * the specified event was processed unsuccessfully by the SLEE. Event
     * processing can fail if, for example, the SLEE doesn't have enough resource
     * to process the event, a SLEE node fails during event processing or a system
     * level failure prevents the SLEE from committing transactions.
     * This method need not be called by the same thread that invoked the fireEvent
     * method for this event.
     *
     * @param handle the activity handle of the activity upon which the event was fired.
     * @param event is the event fired on the activity. This value may be null
     * if no actual event was fired.
     * @param eventID the unique identifier of the event type fired to the SLEE.
     * If the event argument is null, the value of this parameter is undefined.
     * @param address the default address on which the event was fired. If a null
     * default address was specified, this value is also null. If the event
     * argument is null, the value of this parameter is undefined.
     * @param flags the necessary information that enables any post-event
     * processing required by the resource adaptor.
     * @param reason informs the resource adaptor why the event processing failed.
     */
    public void eventProcessingFailed(ActivityHandle handle, Object event,
            int eventID, Address address, int flags, FailureReason reason) {
    }
    
    /**
     * The SLEE calls this method to inform the resource adaptor that the SLEE
     * has completed activity end processing for the activity represented by
     * the activity handle. The resource adaptor should release any resource
     * related to this activity as the SLEE will not ask for it again.
     *
     * @param handle the activity handle of the activity which has ended.
     */
    public void activityEnded(ActivityHandle handle) {
    	
    	mgcpActivityManager.removeMgcpActivity(handle);
    	
        if(logger.isDebugEnabled()) {
        	logger.debug("Activity with handle "+handle+" ended");
        }
    }
    
    /**
     * The SLEE calls this method to inform the resource adaptor that the
     * activity's Activity Context object is no longer attached to any SBB
     * entities and is no longer referenced by any SLEE Facilities.
     * This enables the resource adaptor to implicitly end the Activity object.
     *
     * @param handle the activity handle of the activity which has been unreferenced.
     */
    public void activityUnreferenced(ActivityHandle handle) {
        // do nothing
    }
    
    /**
     * The SLEE calls this method to query if a specific activity belonging to
     * this resource adaptor object is alive. This implies that the resource
     * adaptor will check the underlying resource to see if the activity is
     * still active. In this case the SLEE would retain the activity context of
     * the activity and SBB attached to the activity context.
     * If the activity is not alive the resource adaptor should call the
     * appropriate activityEnding method on the SleeEndpoint, if the activity is
     * still alive the resource adaptor is not expected to do anything.
     *
     * The resource adaptor object should not block on this method, for example
     * if the resource adaptor object needs to query an external system to
     * determine of an activity is alive, it should not block this method until
     * a response is received.
     *
     * @param handle the activity which the SLEE is querying.
     */
    public void queryLiveness(ActivityHandle handle) {
        if (!mgcpActivityManager.containsActivityHandle(handle)) {
            try {
                sleeEndpoint.activityEnding(handle);
            } catch (Exception e) {
                logger.error("Unexpected error while ending activity", e);
            }
        }
    }
    
    /**
     * The SLEE calls this method to get access to the underlying activity for
     * an activity handle. The resource adaptor is expected to pass back a
     * non-null object.
     *
     * @param the activity handle of the requested activity.
     * @return the activity object.
     */
    public Object getActivity(ActivityHandle handle) {
        return mgcpActivityManager.getActivity(handle);
    }
    
    /**
     * The SLEE calls this method to get an activity handle for an activity
     * created by the underlying resource. This method is invoked by the SLEE
     * when it needs to construct an activity context for an activity via an
     * activity context interface factory method invoked by an SBB.
     *
     * @param activity the activity object.
     * @return activity handle or null if there is no handlers for specified activity.
     */
    public ActivityHandle getActivityHandle(Object activity) {
    	return mgcpActivityManager.getActivityHandle(activity);
    }
    
    /**
     * The SLEE calls this method to get access to the underlying resource
     * adaptor interface that enables the SBB to invoke the resource adaptor,
     * to send messages for example.
     *
     * @return Jain MGCP provider.
     */
    public Object getSBBResourceAdaptorInterface(String string) {
        return mgcpProvider;
    }
    
    /**
     * The SLEE calls this method to get reference to the Marshaler object.
     * The resource adaptor implements the <code>Marshaler</code> interface.
     * The Marshaler is used by the SLEE to convert between object and
     * distributable forms of events and event handles. This ensures the SLEE
     * has flexibility to process events, for example in a clustered environment
     * it is conceptually possible for the SLEE to process the event on a node
     * other than the node the event was created on. The method takes no input
     * parameters. All resource adaptors must implement a Marshaler object hence
     * this method must return a non-null value.
     *
     * @return Marshaler object.
     */
    public Marshaler getMarshaler() {
        return null;
    }
    
    /**
     * The SLEE calls this method to signify to the resource adaptor that a
     * service has been installed and is interested in a specific set of events.
     * The SLEE passes an event filter which identifies a set of event types that
     * services in the SLEE are interested in. The SLEE calls this method once
     * a service is installed.
     *
     * @param serviceKey is the key of the service associated to the resource adaptor.
     * @param eventIDs the set of events the service identified by the service
     * key is interested in.
     * @param resourceOptions the set of resource options that map to the events
     * that are included in this filter.
     */
    public void serviceInstalled(String serviceKey, int[] eventIDs, String[] resourceOptions) {
    }
    
    /**
     * The SLEE calls this method to signify that a service has been uninstalled
     * in the SLEE. The event types associated to the service key are no longer
     * of interest to a particular application. The association between the
     * event types and the service key is deleted from the resource adaptor.
     * This doesn't preclude that another service may be interested in these
     * event types. The SLEE calls this method once a service is uninstalled.
     *
     * @param serviceKey the key of the service associated to the resource adaptor.
     */
    public void serviceUninstalled(String serviceKey) {
    }
    
    /**
     * The SLEE calls this method to inform the resource adaptor that a service
     * has been activated and is interested in the event types associated to the
     * service key. The service must be installed with the resource adaptor
     * via the serviceInstalled method before it can be activated.
     * The SLEE can call this method irrespective of the state of the resource adaptor.
     *
     * @param serviceKey the key of the service associated to the resource adaptor.
     */
    public void serviceActivated(String serviceKey) {
    }
    
    /**
     * The SLEE calls this method to inform the SLEE that a service has been
     * deactivated and is no longer interested in the event types associated to
     * the service key. The SLEE can call this method irrespective of the state
     * of the resource adaptor.
     *
     * @param the key of the service currently active in the resource adaptor.
     */
    public void serviceDeactivated(String serviceKey) {
    }
    
    public SleeEndpoint getSleeEndpoint() {
		return sleeEndpoint;
	}
    
    protected void endActivity(ActivityHandle handle) {
    	if (handle != null && mgcpActivityManager.containsActivityHandle(handle)) {
    		try {
    			// send activity end event to the container
    			getSleeEndpoint().activityEnding(handle);
    		} catch (Exception e) {
    			logger.error("Failed to end activity with handle "+handle, e);
    		}
    	}
    }
    
    /**
     * Processes a Command Event object received from a JainMgcpProvider.
     * 
     *
     * @param event received command event.
     */
    public void processMgcpCommandEvent(JainMgcpCommandEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug(bootstrapContext.getEntityName() + " received event of type "+event.getObjectIdentifier()+". Request TX ID = "  + event.getTransactionHandle());
        }
        switch (event.getObjectIdentifier()) {
            
        	case Constants.CMD_AUDIT_CONNECTION :
        		AuditConnection auditConnection = (AuditConnection) event;
            	processNonCreateConnectionMgcpEvent(auditConnection.getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle(),"net.java.slee.resource.mgcp.AUDIT_CONNECTION",event);
                break;
            
            case Constants.CMD_AUDIT_ENDPOINT :
            	AuditEndpoint auditEndpoint = (AuditEndpoint)event;
            	processEndpointMgcpEvent(auditEndpoint.getEndpointIdentifier(),"net.java.slee.resource.mgcp.AUDIT_ENDPOINT",event); 
                break;
            
            case Constants.CMD_CREATE_CONNECTION : 
            	CreateConnection createConnection = (CreateConnection) event;
            	processCreateConnectionMgcpEvent(createConnection);
            	break;
            
            case Constants.CMD_DELETE_CONNECTION :            	
            	DeleteConnection deleteConnection = (DeleteConnection) event;
    			if (deleteConnection.getConnectionIdentifier() != null) {
                	processNonCreateConnectionMgcpEvent(deleteConnection.getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle(),"net.java.slee.resource.mgcp.DELETE_CONNECTION",event);
    			} else {
    				processEndpointMgcpEvent(deleteConnection.getEndpointIdentifier(),
    						"net.java.slee.resource.mgcp.DELETE_CONNECTION", event);
    			}
            	
                break;
                
            case Constants.CMD_ENDPOINT_CONFIGURATION :            	
            	EndpointConfiguration endpointConfiguration = (EndpointConfiguration)event;
            	processEndpointMgcpEvent(endpointConfiguration.getEndpointIdentifier(),"net.java.slee.resource.mgcp.ENDPOINT_CONFIGURATION",event); 
                break;
                
            case Constants.CMD_MODIFY_CONNECTION :
            	ModifyConnection modifyConnection = (ModifyConnection)event;
            	processNonCreateConnectionMgcpEvent(modifyConnection.getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle(),"net.java.slee.resource.mgcp.MODIFY_CONNECTION",event);
                break;
                
            case Constants.CMD_NOTIFICATION_REQUEST :
            	NotificationRequest notificationRequest = (NotificationRequest)event;
            	processEndpointMgcpEvent(notificationRequest.getEndpointIdentifier(),"net.java.slee.resource.mgcp.NOTIFICATION_REQUEST",event); 
                break;
                
            case Constants.CMD_NOTIFY :
            	Notify notify = (Notify)event;
            	processEndpointMgcpEvent(notify.getEndpointIdentifier(),"net.java.slee.resource.mgcp.NOTIFY",event); 
                break;
                
            case Constants.CMD_RESTART_IN_PROGRESS :
            	RestartInProgress restartInProgress = (RestartInProgress)event;
            	processEndpointMgcpEvent(restartInProgress.getEndpointIdentifier(),"net.java.slee.resource.mgcp.RESTART_IN_PROGRESS",event);            
                break;
            
            default :
                logger.warn("Unexpected event type: " + event.getObjectIdentifier());
        }      
    }
    
    /**
     * Processes a Response Event object (acknowledgment to a Command Event
     * object) received from a JainMgcpProvider.
     *
     * @param response The JAIN MGCP Response Event Object that is to be processed.
     */
    public void processMgcpResponseEvent(JainMgcpResponseEvent response, JainMgcpEvent command) {
        if (logger.isDebugEnabled()) {
            logger.debug("Receive response TX ID = "  + response.getTransactionHandle());
        }
        
        switch (response.getObjectIdentifier()) {
            case Constants.RESP_AUDIT_CONNECTION :
            	AuditConnection auditConnection = (AuditConnection)command;
            	processNonCreateConnectionMgcpEvent(auditConnection.getConnectionIdentifier(),auditConnection.getEndpointIdentifier(),response.getTransactionHandle(),"net.java.slee.resource.mgcp.AUDIT_CONNECTION_RESPONSE",response);           
                break;
            case Constants.RESP_AUDIT_ENDPOINT :
            	processEndpointMgcpEvent(((AuditEndpoint)command).getEndpointIdentifier(),"net.java.slee.resource.mgcp.AUDIT_ENDPOINT_RESPONSE",response);
                break;
            case Constants.RESP_CREATE_CONNECTION :
            	ConnectionIdentifier cId = null;
            	EndpointIdentifier endpointIdentifier = null;
            	if (!isProvisional(response.getReturnCode())) {
            		CreateConnectionResponse createConnectionResponse = (CreateConnectionResponse)response;
            		cId = createConnectionResponse.getConnectionIdentifier();
            		endpointIdentifier = createConnectionResponse.getSpecificEndpointIdentifier();
            	}
            	
            	if( endpointIdentifier == null ){
            		endpointIdentifier = ((CreateConnection)command).getEndpointIdentifier();
            	}            	
            	processNonCreateConnectionMgcpEvent(cId,endpointIdentifier,response.getTransactionHandle(),"net.java.slee.resource.mgcp.CREATE_CONNECTION_RESPONSE",response);
        		if (isFailure(response.getReturnCode())) {
        			// create connection didn't succeed, end the activity
        			endActivity(mgcpActivityManager.getMgcpConnectionActivityHandle(cId, endpointIdentifier,response.getTransactionHandle()));
        		}
                break;
            case Constants.RESP_DELETE_CONNECTION :
            	DeleteConnection deleteConnection = (DeleteConnection) command;
    			if (deleteConnection.getConnectionIdentifier() != null) {
    				processNonCreateConnectionMgcpEvent(deleteConnection.getConnectionIdentifier(),deleteConnection.getEndpointIdentifier(),response.getTransactionHandle(),"net.java.slee.resource.mgcp.DELETE_CONNECTION_RESPONSE",response);
    				} else {
    				processEndpointMgcpEvent(deleteConnection.getEndpointIdentifier(),
    						"net.java.slee.resource.mgcp.DELETE_CONNECTION_RESPONSE", response);
    			}            	
            	
                break;
            case Constants.RESP_ENDPOINT_CONFIGURATION :
            	processEndpointMgcpEvent(((EndpointConfiguration)command).getEndpointIdentifier(),"net.java.slee.resource.mgcp.ENDPOINT_CONFIGURATION_RESPONSE",response);
                break;
            case Constants.RESP_MODIFY_CONNECTION :
            	ModifyConnection modifyConnection = (ModifyConnection)command;
            	processNonCreateConnectionMgcpEvent(modifyConnection.getConnectionIdentifier(),modifyConnection.getEndpointIdentifier(),response.getTransactionHandle(),"net.java.slee.resource.mgcp.MODIFY_CONNECTION_RESPONSE",response);
                break;
            case Constants.RESP_NOTIFICATION_REQUEST :
            	processEndpointMgcpEvent(((NotificationRequest)command).getEndpointIdentifier(),"net.java.slee.resource.mgcp.NOTIFICATION_REQUEST_RESPONSE",response);
                break;
            case Constants.RESP_NOTIFY :
            	processEndpointMgcpEvent(((Notify)command).getEndpointIdentifier(),"net.java.slee.resource.mgcp.NOTIFY_RESPONSE",response);
                break;
            case Constants.RESP_RESTART_IN_PROGRESS :
            	processEndpointMgcpEvent(((RestartInProgress)command).getEndpointIdentifier(),"net.java.slee.resource.mgcp.RESTART_IN_PROGRESS_RESPONSE",response);
                break;
            default :
                logger.warn("Unexpected event type: " + response.getObjectIdentifier());
        }
    }
    
    /**
     * Fires specified event on specified context.
     *
     * @param eventName the name of the event that is to be fired.
     * @param handle the activity handle object.
     * @param event the Jain MGCP event object.
     */
    private void fireEvent(String eventName, ActivityHandle handle, Object event) {       
        int eventID = -1;
        try {
            eventID = eventLookup.getEventID(eventName, "net.java","1.0");
        } catch (Exception fe) {
            logger.error("Caught a exception while getting id of event to fire", fe);
        } 
        
        if (eventID == -1) {
            logger.warn("Unknown event type: " + eventName);
            return;
        }
        
        try {
            Address address = new Address(AddressPlan.IP, "localhost");
            sleeEndpoint.fireEvent(handle, event, eventID, address);
            logger.info("Fired event: " + eventName);
        } catch (Exception e) {
            logger.error("Caught an exception while firing event", e);
        } 
    }
    
    // ---------------- EVENT PROCESSORS ------------------------
    
    /**
     * processes a new non {@link CreateConnection} event coming from stack
     */
    private void processNonCreateConnectionMgcpEvent(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier, int transactionHandle, String eventName, Object eventObject) {
    	// get connection activity handle, create activity if does not exists
    	MgcpConnectionActivityHandle handle = mgcpActivityManager.getMgcpConnectionActivityHandle(connectionIdentifier, endpointIdentifier, transactionHandle);
    	if (handle == null) {
    		MgcpConnectionActivityImpl activity = new MgcpConnectionActivityImpl(connectionIdentifier,endpointIdentifier,this);
    		handle = mgcpActivityManager.putMgcpConnectionActivity(activity);  
    	}
    	// fire event
    	fireEvent(eventName, handle, eventObject);
    	// end activity if delete connection request or response
    	if ((eventObject instanceof DeleteConnection) ||
			((eventObject instanceof DeleteConnectionResponse)&&
			 !isProvisional(((DeleteConnectionResponse) eventObject).getReturnCode()))) {
    		try {
        		// send activity end event to the container
        		getSleeEndpoint().activityEnding(handle);
        	} catch (Exception e) {
        		logger.error("Failed to end activity with handle "+handle, e);
        	}
    	}
    }
    
    /**
     * processes a new {@link CreateConnection} event coming from stack
     * @param createConnection
     */
    private void processCreateConnectionMgcpEvent(CreateConnection createConnection) {
    	// fire on new connection activity
    	MgcpConnectionActivityImpl	activity = new MgcpConnectionActivityImpl(createConnection.getTransactionHandle(),createConnection.getEndpointIdentifier(),this);
    	MgcpConnectionActivityHandle handle = mgcpActivityManager.putMgcpConnectionActivity(activity);
    	fireEvent("net.java.slee.resource.mgcp.CREATE_CONNECTION", handle, createConnection);
    }
  
    /**
     * 
     * processes a new {@link NotificationRequest} event coming from stack
     * @param notificationRequest
     */
    private void processEndpointMgcpEvent(EndpointIdentifier endpointIdentifier, String eventName, Object eventObject) {
    	// fire on endpoint activity
    	MgcpEndpointActivityHandle handle = new MgcpEndpointActivityHandle(endpointIdentifier.toString());
    	if (!mgcpActivityManager.containsMgcpEndpointActivityHandle(handle)) {
    		mgcpActivityManager.putMgcpEndpointActivity(handle,new MgcpEndpointActivityImpl(this,endpointIdentifier));  
    	}
    	fireEvent(eventName, handle, eventObject);
    	
		// end activity if delete connection request or response
		if (eventObject instanceof DeleteConnection || eventObject instanceof DeleteConnectionResponse) {
			try {
				// send activity end event to the container
				getSleeEndpoint().activityEnding(handle);
			} catch (Exception e) {
				logger.error("Failed to end activity with handle " + handle, e);
			}
		}    	
    }

    private ActivityHandle getActivityHandle(JainMgcpCommandEvent event) {
    	ActivityHandle handle = null;
    	  
    	switch (event.getObjectIdentifier()) {

    	case Constants.CMD_AUDIT_CONNECTION :
    		
    		handle = mgcpActivityManager.getMgcpConnectionActivityHandle(((AuditConnection) event).getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle());
    		break;

    	case Constants.CMD_AUDIT_ENDPOINT :    	
    		handle = new MgcpEndpointActivityHandle(((AuditEndpoint)event).getEndpointIdentifier().toString()); 
    		if (!mgcpActivityManager.containsActivityHandle(handle)) {
    			handle = null;
    		}
    		break;

    	case Constants.CMD_CREATE_CONNECTION : 
    		handle = mgcpActivityManager.getMgcpConnectionActivityHandle(null,event.getEndpointIdentifier(),event.getTransactionHandle());
    		break;

    	case Constants.CMD_DELETE_CONNECTION :            	
    		handle = mgcpActivityManager.getMgcpConnectionActivityHandle(((DeleteConnection)event).getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle());            	
    		break;

    	case Constants.CMD_ENDPOINT_CONFIGURATION :            	
    		handle = new MgcpEndpointActivityHandle(((EndpointConfiguration)event).getEndpointIdentifier().toString());  
    		if (!mgcpActivityManager.containsActivityHandle(handle)) {
    			handle = null;
    		}
    		break;

    	case Constants.CMD_MODIFY_CONNECTION :
    		handle = mgcpActivityManager.getMgcpConnectionActivityHandle(((ModifyConnection)event).getConnectionIdentifier(),event.getEndpointIdentifier(),event.getTransactionHandle());
    		break;

    	case Constants.CMD_NOTIFICATION_REQUEST :
    		handle = new MgcpEndpointActivityHandle(((NotificationRequest)event).getEndpointIdentifier().toString());
    		if (!mgcpActivityManager.containsActivityHandle(handle)) {
    			handle = null;
    		}
    		break;

    	case Constants.CMD_NOTIFY :
    		handle = new MgcpEndpointActivityHandle(((Notify)event).getEndpointIdentifier().toString());
    		if (!mgcpActivityManager.containsActivityHandle(handle)) {
    			handle = null;
    		}
    		break;

    	case Constants.CMD_RESTART_IN_PROGRESS :
    		handle = new MgcpEndpointActivityHandle(((RestartInProgress)event).getEndpointIdentifier().toString());          
    		if (!mgcpActivityManager.containsActivityHandle(handle)) {
    			handle = null;
    		}
    		break;

    	default :
    		logger.warn("Unexpected event type: " + event.getObjectIdentifier());
    		break;
    	}
    	return handle;
    }

    /**
	 * Processes a timeout occurred in a sent {@link JainMgcpCommandEvent} event
	 * transaction, to prevent further failures the activity will end after the
	 * RA fires the {@link TransactionTimeout} event.
	 * 
	 * @param event the command sent.
	 */
    public void processTxTimeout(JainMgcpCommandEvent event) {
    	ActivityHandle handle = null;
    	
    	handle = getActivityHandle(event);
    	if (handle != null) {
    		// fire timeout event
    		fireEvent("net.java.slee.resource.mgcp.TRANSACTION_TIMEOUT", handle, new TransactionTimeout());
    		// end activity
    		endActivity(handle);
    	}
    }

    /**
	 * Processes a timeout on a received {@link JainMgcpCommandEvent} event
	 * transaction, to prevent further failures the activity will end.
	 * 
	 * @param event the command sent.
	 */
    public void processRxTimeout(JainMgcpCommandEvent event) {
    	ActivityHandle handle = null;
    	
    	handle = getActivityHandle(event);
    	if (handle != null) {
    		endActivity(handle);
    	}
    }

    /**
	 * Indicates the provider will send the specified
	 * {@link CreateConnectionResponse} event, which contains the
	 * {@link ConnectionIdentifier} to update the {@link MgcpConnectionActivity}
	 * related with the Mgcp transaction.
	 * 
	 * @param event
	 */
    public void sendingCreateConnectionResponse(CreateConnectionResponse event) {
    	mgcpActivityManager.updateMgcpConnectionActivity(event.getTransactionHandle(), event.getConnectionIdentifier(),event.getSpecificEndpointIdentifier());
    }
    
    /**
     * Registers MgcpActivityContextInterfaceFactory with JNDI name.
     *
     */
    private void initializeNamingContext() {
        logger.info("Initialize naming context");
        
        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        logger.debug("SLEE container: " + sleeContainer);
        
        String entityName = bootstrapContext.getEntityName();
        logger.debug("Entity name: " + entityName);
        
        ResourceAdaptorEntity resourceAdaptorEntity =
                (ResourceAdaptorEntity) sleeContainer.getResourceAdaptorEnitity(entityName);
        logger.debug("Resource Adaptor Entity: " + resourceAdaptorEntity);
        
        ResourceAdaptorTypeID resourceAdaptorTypeId =
                resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();
        logger.debug("Resource Adaptor Type ID: " + resourceAdaptorTypeId);
        
        acif = new MgcpActivityContextInterfaceFactoryImpl(
                resourceAdaptorEntity.getServiceContainer(), this, entityName);
        sleeContainer.getActivityContextInterfaceFactories().put(resourceAdaptorTypeId, acif);
        
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory)acif).getJndiName();
        
        int i = jndiName.indexOf(':');
        int j = jndiName.lastIndexOf('/');
        
        String prefix = jndiName.substring(i + 1, j);
        String name = jndiName.substring(j + 1);
        
        sleeContainer.registerWithJndi(prefix, name, acif);
    }
    
    /**
     * Unregisters MgcpActivityContextInterfaceFactory
     */
    private void clearNamingContext() {
        String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory)
        acif).getJndiName();
        int i = jndiName.indexOf(':');
        String name = jndiName.substring(i + 1);
        
        SleeContainer.unregisterWithJndi(name);
    }

    /**
     * Check whether the given return code is a provisional response.
     * @param rc	the return code
     * @return		true when the code is provisional
     */
    private static boolean isProvisional(ReturnCode rc) {
    	final int rval = rc.getValue();

    	return ((99 < rval) && (rval < 200));
    }

    /**
     * Check whether the given return code indicates a failure/error.
     * @param rc	the return code
     * @return		true when the code indicates an error
     */
    private static boolean isFailure(ReturnCode rc) {
    	final int rval = rc.getValue();

    	return (rval > 299);
    }

}