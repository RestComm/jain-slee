package org.mobicents.slee.container.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.CreateException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.cache.ServiceCacheData;
import org.mobicents.slee.runtime.eventrouter.DeferredActivityEndEvent;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;

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

public class Service implements Serializable {

	/**
	 * unique vid is required for safe serialization
	 */
	private static final long serialVersionUID = 1L;

	private static final transient Logger logger = Logger.getLogger(Service.class);
	
	private static final transient SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	//  The named usage parameters.
	// Index is serviceID+ssbbID + name
	// Do these need to be replicated?

	private static ConcurrentHashMap<ServiceID, HashMap> _usageParameters;

	public static String getUsageParametersPathName(ServiceID serviceId,
			SbbID sbbId) {
		return serviceId.toString() + "/" + sbbId.toString();
	}

	private static ConcurrentHashMap<ServiceID, HashMap> getUsageParameters() {
		if (_usageParameters == null) {
			_usageParameters = new ConcurrentHashMap<ServiceID, HashMap>();
		}
		return _usageParameters;
	}
	
	private static HashMap getUsageParameters(ServiceID serviceId) {
		ConcurrentHashMap<ServiceID, HashMap> usageParameters = getUsageParameters();
		HashMap hashMap = (HashMap) usageParameters.get(serviceId);
		if (hashMap == null) {
			hashMap = new HashMap();
			HashMap anotherHashMap = usageParameters.putIfAbsent(serviceId,
					hashMap);
			if (anotherHashMap != null) {
				hashMap = anotherHashMap;
			}
		}
		return hashMap;
	}

	public static String getUsageParametersPathName(ServiceID serviceID,
			SbbID sbbId, String name) {

		return getUsageParametersPathName(serviceID, sbbId) + "/"
				+ SleeContainerUtils.toHex(name);
	}

	// --- service
	
	private byte defaultPriority;

	private final ServiceIDImpl serviceID;

	private final SbbID rootSbbID;

	private final ServiceCacheData cacheData;
	
	/**
	 * The Public constructor. This is used to create a runtime representation
	 * of the service.
	 * 
	 * @param serviceDescriptor --
	 *            the svc descriptor corresponding to this Service.
	 * 
	 * @throws RuntimeException
	 */

	protected Service(ServiceDescriptorImpl serviceDescriptor, boolean initCachedData) throws RuntimeException {
		
		if (serviceDescriptor == null)
			throw new NullPointerException("null descriptor or container");
		
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Service.Service(): creating service"
						+ serviceDescriptor.getID());
			}

			this.serviceID = (ServiceIDImpl) serviceDescriptor.getID();
			this.defaultPriority = serviceDescriptor.getDefaultPriority();	
			this.rootSbbID = serviceDescriptor.getRootSbb();
			
			ConcurrentHashMap<ServiceID, HashMap> usageParameters = getUsageParameters();
			if (usageParameters.get(serviceID) == null) {
				HashMap hmap = new HashMap();
				usageParameters.put(serviceID, hmap);
			}
			
			this.cacheData = sleeContainer.getCache().getServiceCacheData(this.serviceID);
			if (initCachedData && !cacheData.exists()) {
				cacheData.create();
			}
			
		} catch (Exception ex) {
			String s = "Exception encountered while loading service ";
			logger.error(s, ex);
			throw new RuntimeException(s, ex);
		}
	}
	
	/**
	 * get the default priority.
	 */
	public byte getDefaultPriority() {
		return this.defaultPriority;
	}
	
	/**
	 * get the component key for the service component from which this service
	 * was created.
	 */
	public ServiceID getServiceID() {
		return this.serviceID;
	}

	/**
	 * Retrieves the {@link SbbID} of the root sbb for this service
	 * @return
	 */
	public SbbID getRootSbbID() {
		return rootSbbID;
	}

	/**
	 * Set the service state.
	 * 
	 * @param serviceState
	 */
	public void setState(ServiceState serviceState) {
		if (logger.isDebugEnabled()) {
			try {
				ServiceState oldServiceState = cacheData.getState();
				logger
						.debug("ServiceComponent.setState(): State service ID =  "
								+ this.serviceID
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
	public Collection getChildObj() {
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
	public SbbEntity addChild(String convergenceName) throws CreateException {

		if (logger.isDebugEnabled()) {
			if (logger.isDebugEnabled()) {
				logger.debug(getServiceID().toString() + " adding convergence name "+convergenceName);
			}
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		// create root sbb entity
		SbbEntity sbbEntity = SbbEntityFactory.createRootSbbEntity(rootSbbID,
				this.getServiceID(), convergenceName);
		// set default priority
		sbbEntity.setPriority(getDefaultPriority());
		// store in cache
		cacheData.addChild(convergenceName, sbbEntity.getSbbEntityId());

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
	 * Get the table that maps usage parameter name to usage parameter class
	 * instance.
	 * 
	 * @return
	 */
	public static HashMap getUsageParameterTable(ServiceID serviceID) {
		if (logger.isDebugEnabled()) {
			logger.debug("Service.getUsageParameterTable() " + serviceID);
		}
		return getUsageParameters().get(serviceID);
	}

	/**
	 * Return the default usage parameter set for the given sbb id.
	 * 
	 * @param sbbId
	 * @return
	 */
	public static InstalledUsageParameterSet getDefaultUsageParameterSet(
			ServiceID serviceId, SbbID sbbId) {

		if (logger.isDebugEnabled()) {
			logger.debug("Service.getDefaultUsageParameterSet(): " + serviceId
					+ " sbbID = " + sbbId);
		}
		String key = Service.getUsageParametersPathName(serviceId, sbbId);
		if (logger.isDebugEnabled()) {
			logger.debug("Service.getDefaultUsageParameterSet: "
					+ getUsageParameters() + " key = " + key);
		}
		return (InstalledUsageParameterSet) getUsageParameters(serviceId).get(
				key);
	}

	/**
	 * Return the named usage parameter set given the sbb id and name of the
	 * set.
	 * 
	 * @param sbbId --
	 *            sbb id for which to retrieve the set.
	 * @param name --
	 *            name of the set to retrieve.
	 * 
	 * @return the named usage parameters given the sbbid.
	 */
	public static InstalledUsageParameterSet getNamedUsageParameter(
			ServiceID serviceId, SbbID sbbId, String name) {
		String key = Service.getUsageParametersPathName(serviceId, sbbId, name);
		return (InstalledUsageParameterSet) getUsageParameters(serviceId).get(
				key);
	}

	/**
	 * Return an iterator containing the InstalledUsageParameterSet s for this
	 * service.
	 * 
	 * @return all the usage parameters.
	 */
	public static Iterator getAllUsageParameters(ServiceID serviceId) {
		return getUsageParameters(serviceId).values().iterator();
	}

	/**
	 * @param pathName
	 */
	protected static void removeUsageParameter(ServiceID serviceID,
			String pathName) {
		getUsageParameters(serviceID).remove(pathName);
	}

	/**
	 * @param pathName
	 * @param usageParam
	 */
	protected static void addUsageParameter(ServiceID serviceID,
			String pathName, Object usageParam) {
		getUsageParameters(serviceID).put(pathName, usageParam);
	}

	/**
	 * @param serviceID --
	 *            service id for which we want to remove all usage mbeans
	 */
	public void removeAllUsageParameters() {
		getUsageParameters().remove(this.serviceID);
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
	public void startActivity() throws SystemException {

		// create ac for the activity
		ActivityContextHandle ach = ActivityContextHandlerFactory.createServiceActivityContextHandle(new ServiceActivityHandle(serviceID));
		ActivityContext ac = null;
		try {
			ac = sleeContainer.getActivityContextFactory().createActivityContext(ach);
		} catch (ActivityAlreadyExistsException e) {
			final String msg = "service activity already exists";
			logger.error(msg,e);
			throw new SystemException(msg);
		}
		
		if (logger.isDebugEnabled()) {
			logger
					.debug("starting service activity for "
							+ serviceID);
		}
		new DeferredServiceStartedEvent(ac, new ServiceStartedEventImpl(serviceID));
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

		ActivityContextHandle ach = ActivityContextHandlerFactory.createServiceActivityContextHandle(new ServiceActivityHandle(serviceID));
		if (logger.isDebugEnabled()) {
			logger.debug("ending service activity "+ach);
		}
		ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach,false);
		if (ac != null && ac.getState() == ActivityContextState.ACTIVE) {
			ac.setState(ActivityContextState.ENDING);
			try {
				new DeferredActivityEndEvent(ac,null);
			} catch (SystemException e) {
				logger.error("failed to create deferred activity end event", e);
			}
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
		sb.append("Service.printNode() { serviceID = " + this.serviceID + "\n");
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