package org.mobicents.slee.container.service;

import java.util.Set;

import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.slee.resource.EventFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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

public class Service {

	private static final Logger logger = Logger.getLogger(Service.class);
	
	private static final  SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	// --- service
	
	private final ServiceComponent serviceComponent;
	
	private final ServiceCacheData cacheData;
	
	/**
	 * The Public constructor. This is used to create a runtime representation
	 * of the service.
	 * 
	 * @param serviceComponent --
	 *            the svc descriptor corresponding to this Service.
	 * 
	 * @throws RuntimeException
	 */

	protected Service(ServiceComponent serviceComponent) throws RuntimeException {
		
		if (serviceComponent == null)
			throw new NullPointerException("null descriptor or container");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Service.Service(): creating service "
					+ serviceComponent);
		}

		this.serviceComponent = serviceComponent;
		this.cacheData = new ServiceCacheData(serviceComponent.getServiceID(),sleeContainer.getCluster().getMobicentsCache());
		cacheData.create();			
	}	
	
	/**
	 * get the default priority.
	 */
	public byte getDefaultPriority() {
		return serviceComponent.getDefaultPriority();
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
		if (logger.isDebugEnabled()) {
			try {
				ServiceState oldServiceState = cacheData.getState();
				logger
						.debug("ServiceComponent.setState(): State service ID =  "
								+ getServiceID()
								+ " current State = "
								+ oldServiceState
								+ " new State = "
								+ serviceState
								+ " TX ID: "
								+ sleeContainer.getTransactionManager()
										.getTransaction());				
			} catch (SystemException e) {
				logger.error("error in debugging setState(): ", e);
			}
		}
		cacheData.setState(serviceState);
		// notifying the resource adaptors about service state change if the tx commits
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				ServiceID serviceID = getServiceID();
				for (String raEntityName : resourceManagement
						.getResourceAdaptorEntities()) {
					if (serviceState == ServiceState.ACTIVE) {
						resourceManagement.getResourceAdaptorEntity(raEntityName).serviceActive(serviceID);
					}
					else if (serviceState == ServiceState.STOPPING) {
						resourceManagement.getResourceAdaptorEntity(raEntityName).serviceStopping(serviceID);
					}
					else if (serviceState == ServiceState.INACTIVE) {
						resourceManagement.getResourceAdaptorEntity(raEntityName).serviceInactive(serviceID);
					}					
				}
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterCommitAction(action);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
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

	/**
	 * Check if this service maps the specified convergence name.
	 * 
	 * @param convergenceName
	 * @return
	 */
	public boolean containsConvergenceName(String convergenceName) {
		return cacheData.hasChild(convergenceName);
	}

	/**
	 * Add a child for a given convergence name. This actually creates an Sbb
	 * Entity for the given convergence name and returns it.
	 * 
	 * @param convergenceName
	 */
	public SbbEntity addChild(String convergenceName) {

		if (logger.isDebugEnabled()) {
			logger.debug(getServiceID().toString() + " adding convergence name "+convergenceName);
		}

		// create root sbb entity
		SbbEntity sbbEntity = SbbEntityFactory.createRootSbbEntity(getRootSbbID(),
				this.getServiceID(), convergenceName);
		if (sbbEntity.isCreated()) {
			// set default priority
			sbbEntity.setPriority(getDefaultPriority());
			// store in service's cache data
			cacheData.addChild(convergenceName, sbbEntity.getSbbEntityId());
		}

		return sbbEntity;
	}

	public String getRootSbbEntityId(String convergenceName) {
		return cacheData.getChild(convergenceName);
	}

	public void removeConvergenceName(String convergenceName) {
		if (logger.isDebugEnabled()) {
			logger.debug(getServiceID().toString() + " removing convergence name "+convergenceName);
		}
		cacheData.removeChild(convergenceName);
	}

	/**
	 * @return the service activity for this service.
	 */
	public ServiceActivityImpl getServiceActivity() {
		return new ServiceActivityImpl(this);
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
		ActivityContextHandle ach = ActivityContextHandlerFactory.createServiceActivityContextHandle(new ServiceActivityHandle(serviceComponent.getServiceID()));
		ActivityContext ac = sleeContainer.getActivityContextFactory().createActivityContext(ach);
		
		if (logger.isDebugEnabled()) {
			logger
					.debug("starting service activity for "
							+ serviceComponent);
		}
		
		// fire slee 1.0 and 1.1 service started events
		ServiceStartedEventImpl event = new ServiceStartedEventImpl(getServiceID());
		ac.fireEvent(ServiceStartedEventImpl.SLEE_10_EVENT_TYPE_ID,event,null,null,EventFlags.NO_FLAGS);
		ac.fireEvent(ServiceStartedEventImpl.SLEE_11_EVENT_TYPE_ID,event,null,event.getService(),EventFlags.NO_FLAGS);
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

		ActivityContextHandle ach = ActivityContextHandlerFactory.createServiceActivityContextHandle(new ServiceActivityHandle(serviceComponent.getServiceID()));
		if (logger.isDebugEnabled()) {
			logger.debug("ending service activity "+ach);
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
				.append("serviceActivity = " + this.getServiceActivity() + "\n")
				.append("serviceState = " + this.getState() + "\n").append("}");

		return sb.toString();
	}

}