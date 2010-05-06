package org.mobicents.slee.container.service;

import java.util.Set;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.slee.resource.ActivityFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbbentity.SbbEntity;

/**
 * Service implementation. This is the run-time representation of the service
 * Represents an instance of a Slee Service. Note that in the SLEE, the Service
 * is a management artifact.
 * 
 * @author eduardomartins
 * @author Francesco Moggia
 * @author M. Ranganathan
 *  
 */

public class ServiceImpl implements Service {

	private static final Logger logger = Logger.getLogger(ServiceImpl.class);
	
	private static final  SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	// --- service
	
	private final ServiceComponent serviceComponent;
	
	private final ServiceCacheData cacheData;
	
	private final boolean doDebugLogs = logger.isDebugEnabled();
	
	/**
	 * The Public constructor. This is used to create a runtime representation
	 * of the service.
	 * 
	 * @param serviceComponent --
	 *            the svc descriptor corresponding to this Service.
	 * 
	 * @throws RuntimeException
	 */
	public ServiceImpl(ServiceComponent serviceComponent) throws RuntimeException {
		
		if (serviceComponent == null)
			throw new NullPointerException("null descriptor or container");

		this.serviceComponent = serviceComponent;
		this.cacheData = new ServiceCacheData(serviceComponent.getServiceID(),sleeContainer.getCluster().getMobicentsCache());
		cacheData.create();			
	}	
	
	/**
	 * get the default priority.
	 */
	public byte getDefaultPriority() {
		return serviceComponent.getDescriptor().getDefaultPriority();
	}
	
	/**
	 * Retrieves the service component
	 * @return
	 */
	public ServiceComponent getServiceComponent() {
		return serviceComponent;
	}
	
	/**
	 * get the component key for the service component from which this service
	 * was created.
	 */
	public ServiceID getServiceID() {
		return serviceComponent.getServiceID();
	}

	/**
	 * Retrieves the {@link SbbID} of the root sbb for this service
	 * @return
	 */
	public SbbID getRootSbbID() {
		return this.serviceComponent.getRootSbbComponent().getSbbID();
	}

	/**
	 * Set the service state.
	 * 
	 * @param serviceState
	 */
	public void setState(final ServiceState serviceState) {
		if (doDebugLogs) {
			logger.debug("Changing "+getServiceID()+" state to "+serviceState);
		}
		cacheData.setState(serviceState);				
	}

	/**
	 * Returns the service state.
	 * 
	 * @return
	 */
	public ServiceState getState() {
		if (cacheData.exists() && !cacheData.isRemoved()) {
			// we need to trap service state retrieval since it may be done for
			// a service that is not in cache
			ServiceState serviceState = cacheData.getState();
			if (serviceState != null) {
				return serviceState;
			}
		}
		return ServiceState.INACTIVE;
	}

	/**
	 * Get the SBB entity values for the service.  Note operation is rather
	 * expensive as reading all the SBB entities from the cache.  Avoid using
	 * it whenever possible
	 * 
	 *  
	 */
	public Set<String> getChildObj() {
		return cacheData.getChildSbbEntities();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.service.Service#containsConvergenceName(java.lang.String)
	 */
	public boolean containsConvergenceName(String convergenceName) {
		return cacheData.hasChild(convergenceName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.service.Service#addChild(java.lang.String)
	 */
	public SbbEntity addChild(String convergenceName) {

		if (doDebugLogs) {
			logger.debug(getServiceID().toString() + " adding convergence name "+convergenceName);
		}

		// create root sbb entity
		SbbEntity sbbEntity = sleeContainer.getSbbEntityFactory().createRootSbbEntity(getRootSbbID(),
				serviceComponent.getServiceID(), convergenceName);
		if (sbbEntity.isCreated()) {
			// set default priority
			sbbEntity.setPriority(getDefaultPriority());
			// store in service's cache data
			cacheData.addChild(convergenceName, sbbEntity.getSbbEntityId());
		}

		return sbbEntity;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.service.Service#getRootSbbEntityId(java.lang.String)
	 */
	public String getRootSbbEntityId(String convergenceName) {
		return cacheData.getChild(convergenceName);
	}

	public void removeConvergenceName(String convergenceName) {
		if (doDebugLogs) {
			logger.debug(getServiceID().toString() + " removing convergence name "+convergenceName);
		}
		cacheData.removeChild(convergenceName);
	}

	/**
	 * Activate the Service and send out ServiceStartedEvent on the Service
	 * Activity associated with the Service.
	 * 
	 * The following steps describe the life cycle of a Service: · A Service
	 * enters the Inactive state when the Service is installed successfully into
	 * the SLEE. · A Service enters the Active state from the Inactive state
	 * when the Service is activated. At this point, the SLEE may start a
	 * Service Activity for the Service, and fire a Service Started Event on
	 * this Activity, as described in Section 8.7. The operational state of a
	 * Service is persistent, i.e. the SLEE remembers the last state the Service
	 * is in. If the SLEE is shut down and then restarted, the SLEE restores
	 * these Services to their previous operational state.
	 *  
	 */
	public void startActivity() {

		// create ac for the activity
		ActivityContextHandle ach = new ServiceActivityContextHandle(new ServiceActivityHandleImpl(serviceComponent.getServiceID()));
		ActivityContext ac = sleeContainer.getActivityContextFactory().createActivityContext(ach,ActivityFlags.NO_FLAGS);
		
		if (doDebugLogs) {
			logger
					.debug("Starting "+getServiceID()+" activity.");
		}
		
		// fire slee 1.0 and 1.1 service started events
		ServiceStartedEventImpl event = new ServiceStartedEventImpl(getServiceID());
		ac.fireEvent(ServiceStartedEventImpl.SLEE_10_EVENT_TYPE_ID,event,null,null,null,null,null);
		ac.fireEvent(ServiceStartedEventImpl.SLEE_11_EVENT_TYPE_ID,event,null,event.getService(),null,null,null);
	}

	/**
	 * This sets the service state to STOPPING and sends out activity end events
	 * on the ServiceActivity. The state transitions to INACTIVE happens in the
	 * EventRouter after the EndActivity for the service activty is Consumed.
	 * The root sbb entity trees are forcefully removed when the service
	 * activity is consumed.
	 * 
	 * A Service enters the Stopping state from the Active state when the
	 * Service is deactivated. At this point, the SLEE ends the Activity
	 * associated with the Service, if it exists (see Section 8.7.2), and fires
	 * an Activity End Event on this Activity. SBB entities belonging to the
	 * Service that require clean-up when the Service is deactivated should
	 * listen to this event and terminate their processing quickly but
	 * gracefully when this event is received. Optionally, after some SLEE
	 * implementation determined time, the SLEE may also forcefully remove the
	 * outstanding SBB entity trees of the Service. · The SLEE moves a Service
	 * to the Inactive state from the Stopping state spontaneously when all
	 * outstanding SBB entity trees of the Service complete their processing.
	 * The operational state of a Service is persistent, i.e. the SLEE remembers
	 * the last state the Service is in. If the SLEE is shut down and then
	 * restarted, the SLEE restores these Services to their previous operational
	 * state.
	 * 
	 *  
	 */
	public void endActivity() {

		ActivityContextHandle ach = new ServiceActivityContextHandle(new ServiceActivityHandleImpl(serviceComponent.getServiceID()));
		if (doDebugLogs) {
			logger
					.debug("Ending "+getServiceID()+" activity.");
		}
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
		if (ac != null) {
			ac.endActivity();
		}
		else {
			logger.error("unable to find and end ac "+ach);
		}
	}
	
	/**
	 * Removes the service data
	 */
	public void removeFromCache() {
		cacheData.remove();
	}	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Service.printNode() { serviceID = " + getServiceID() + "\n");
		if (logger.isDebugEnabled()) {
			// very expensive operation.  Use w/ care
			sb.append("childObj = " + cacheData.getChildSbbEntities() + "/n");
		} else {
			sb.append("childObj = <not fully loaded from cache>\n");
		}
		sb
				.append(
						"defaultPriority =  " + this.getDefaultPriority()
								+ "\n")
				.append("serviceState = " + this.getState() + "\n").append("}");

		return sb.toString();
	}

}