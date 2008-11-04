/*
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.CreateException;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.slee.serviceactivity.ServiceStartedEvent;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceStartedEventImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

/**
 * Service implementation. This is the run-time representation of the service
 * Represents an instance of a Slee Service. Note that in the SLEE, the Service
 * is a management artifact.
 * 
 * @author Francesco Moggia
 * @author M. Ranganathan
 * @author eduardomartins
 *  
 */

public class Service implements Serializable {

	/**
	 * unique vid is required for safe serialization
	 */
	private static final long serialVersionUID = 4711716462275941571L;

	private static String tcache = TransactionManagerImpl.RUNTIME_CACHE;

	private CacheableMap serviceAttributes;

	private CacheableMap childObj;

	private byte defaultPriority;

	private ServiceIDImpl serviceID;

	// The child Relation object containing the set of sbb entities created for
	// the service

	private static String CHILD_OBJ = "rootSbbEntities";

	private static String SERVICE_ATTRS = "serviceAttributes";

	private static final String SERVICE_STATE = "serviceState";

	private static final String SERVICE_ACTIVITY = "serviceActivity";

	private static Logger logger = Logger.getLogger(Service.class);

	transient private SleeContainer sleeContainer;

	boolean isRemoved;

	//  The named usage parameters.
	// Index is serviceID+ssbbID + name
	// Do these need to be replicated?

	private static ConcurrentHashMap<ServiceID, HashMap> usageParameters = new ConcurrentHashMap<ServiceID, HashMap>();

	// the name of the node in the cache where Service attributes are stored
	private String cacheNodeName;

	private SbbID rootSbbID;

	public static String getUsageParametersPathName(ServiceID serviceId,
			SbbID sbbId) {
		return serviceId.toString() + "/" + sbbId.toString();
	}

	private static HashMap getUsageParameters(ServiceID serviceId) {
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

	/**
	 * Set the service state.
	 * 
	 * @param serviceState
	 */
	public void setState(ServiceState serviceState) {
		if (logger.isDebugEnabled()) {
			try {
				ServiceState oldServiceState = (ServiceState) serviceAttributes
						.get(SERVICE_STATE);
				logger
						.debug("ServiceComponent.setState(): State service ID =  "
								+ this.serviceID
								+ " current State = "
								+ oldServiceState
								+ " new State = "
								+ serviceState
								+ " TX ID: "
								+ SleeContainer.getTransactionManager()
										.getTransaction());
				// logger.info(SleeContainer.getTransactionManager().displayOngoingSleeTransactions());
			} catch (SystemException e) {
				logger.error("error in debugging setState(): ", e);
			}
		}
		serviceAttributes.put(SERVICE_STATE, serviceState);
	}

	/**
	 * The Public constructor. This is used to create a runtime representation
	 * of the service.
	 * 
	 * @param serviceDescriptor --
	 *            the svc descriptor corresponding to this Service.
	 * 
	 * @throws RuntimeException
	 */

	private Service(ServiceDescriptorImpl serviceDescriptor)
			throws RuntimeException {
		if (serviceDescriptor == null)
			throw new NullPointerException("null descriptor or container");
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Service.Service(): creating service"
						+ serviceDescriptor.getID());
			}

			this.serviceID = (ServiceIDImpl) serviceDescriptor.getID();

			if (usageParameters.get(serviceID) == null) {
				HashMap hmap = new HashMap();
				usageParameters.put(serviceID, hmap);
			}
			sleeContainer = SleeContainer.lookupFromJndi();

			this.defaultPriority = serviceDescriptor.getDefaultPriority();

			childObj = new CacheableMap(tcache + "-" + getCacheNodeName() + "#"
					+ CHILD_OBJ);

			serviceAttributes = new CacheableMap(tcache + "-"
					+ getCacheNodeName() + "#" + SERVICE_ATTRS);

			rootSbbID = serviceDescriptor.getRootSbb();

		} catch (Exception ex) {
			String s = "Exception encountered while loading service ";
			logger.error(s, ex);
			throw new RuntimeException(s, ex);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Service.printNode() { serviceID = " + this.serviceID + "\n");
		if (logger.isDebugEnabled()) {
			// very expensive operation.  Use w/ care
			sb.append("childObj = " + childObj + "/n");
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

	/**
	 * Returns the service state.
	 * 
	 * @return
	 */
	public ServiceState getState() {
		ServiceState serviceState = null;
		serviceState = (ServiceState) serviceAttributes.get(SERVICE_STATE);
		if (serviceState == null) {
			serviceState = ServiceState.INACTIVE;
		}
		return serviceState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.Cacheable#getNodeName()
	 */
	public String getCacheNodeName() {
		if (this.cacheNodeName != null)
			return this.cacheNodeName;
		else
			this.cacheNodeName = "services:"
					+ serviceID.getComponentKey().getName() + "-"
					+ serviceID.getComponentKey().getVendor() + "-"
					+ serviceID.getComponentKey().getVersion();
		return cacheNodeName;
	}

	/**
	 * get the component key for the service component from which this service
	 * was created.
	 */
	public ServiceID getServiceID() {
		return this.serviceID;
	}

	/**
	 * get the default priority.
	 */
	public byte getDefaultPriority() {

		return this.defaultPriority;
	}

	/**
	 * Get the SBB entity values for the service.  Note operation is rather
	 * expensive as reading all the SBB entities from the cache.  Avoid using
	 * it whenever possible
	 * 
	 *  
	 */
	public Collection getChildObj() {
		return this.childObj.values();
	}

	/**
	 * Check if this service maps a given convergence name.
	 * 
	 * @param convergenceName
	 * @return
	 */
	public boolean containsConvergenceName(String convergenceName) {
		return this.childObj.containsKey(convergenceName);
	}

	/**
	 * Add a child for a given convergence name. This actually creates an Sbb
	 * Entity for the given convergence name and returns it.
	 * 
	 * @param convergenceName
	 */
	public SbbEntity addChild(String convergenceName) throws CreateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Service.addChild " + this.serviceID
					+ " convergence name " + convergenceName);
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		SbbEntity sbbEntity = SbbEntityFactory.createRootSbbEntity(rootSbbID,
				this.getServiceID(), convergenceName);

		sbbEntity.setPriority(getDefaultPriority());

		childObj.put(convergenceName, sbbEntity.getSbbEntityId());

		return sbbEntity;

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
	public void deactivate() {

		this.setState(ServiceState.STOPPING);

		if (logger.isDebugEnabled()) {
			logger.debug("Service.deactivate()  " + this.serviceID);
		}

		sleeContainer.getSleeEndpoint().scheduleActivityEndedEvent(
				this.getServiceActivity());

	}

	public String getRootSbbEntityId(String convergenceName) {
		return (String) this.childObj.get(convergenceName);
	}

	public void removeConvergenceName(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("Service.removeConvergenceName() " + this.serviceID
					+ " name = " + name);
		}
		this.childObj.remove(name);
	}

	/**
	 * @return the service activity for this service.
	 */
	public ServiceActivityImpl getServiceActivity() {
		return (ServiceActivityImpl) serviceAttributes.get(SERVICE_ACTIVITY);
	}

	/**
	 * Remove my cached image.
	 *  
	 */
	public void removeFromCache() throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Service.removeFromCache() " + this.serviceID);
		}

		serviceAttributes.remove();
		childObj.remove();
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
		return usageParameters.get(serviceID);
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
					+ usageParameters + " key = " + key);
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
		usageParameters.remove(this.serviceID);
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
	public void activate() throws SystemException {

		// change service state
		this.setState(ServiceState.ACTIVE);
		// create service activity
		ServiceActivityImpl serviceActivityImpl = new ServiceActivityImpl(this);
		// get ac for the activity
		ActivityContext ac = sleeContainer.getActivityContextFactory()
				.getActivityContext(serviceActivityImpl);
		// set ac id in service activity
		serviceActivityImpl.setActivityContxtId(ac.getActivityContextId());
		// save service activity
		this.setServiceActivity(serviceActivityImpl);
		// create event
		EventTypeID eventTypeID = sleeContainer.getEventManagement().getEventType(new ComponentKey("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
				"1.0"));
		ServiceStartedEvent ev = new ServiceStartedEventImpl(serviceID);

		if (logger.isDebugEnabled()) {
			logger
					.debug("Service.activate(): sending ServiceStartedEvent for service "
							+ serviceID);
		}

		new DeferredEvent(eventTypeID, ev, ac, null);

	}

	private void setServiceActivity(ServiceActivityImpl serviceActivity) {
		serviceAttributes.put(SERVICE_ACTIVITY, serviceActivity);
	}

	public static Service getService(ServiceDescriptorImpl serviceDescriptorImpl) {

		final SleeTransactionManager txMgr = SleeContainer.lookupFromJndi()
				.getTransactionManager();

		txMgr.mandateTransaction();

		// check local tx data
		Service service = (Service) txMgr.getTxLocalData(serviceDescriptorImpl
				.getID());

		if (service == null) {
			// not in tx local data so recreate the service and store in tx 
			service = new Service(serviceDescriptorImpl);
			txMgr.putTxLocalData(serviceDescriptorImpl.getID(), service);
		}

		return service;

	}
}