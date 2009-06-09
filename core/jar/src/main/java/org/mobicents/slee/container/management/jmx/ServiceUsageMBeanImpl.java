/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedSbbException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.SbbNotification;
import javax.slee.management.ServiceUsageMBean;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.SbbUsageMBean;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.slee.usage.UsageMBean;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;

/**
 * 
 * See SLEE 1.0 #14.9.
 * 
 * The ServiceUsageMBean interface defines the management interface used to
 * interact with SBB usage parameter sets for SBBs in a Service. It defines the
 * methods to create, lookup, and remove SBB usage parameter sets from SBBs in
 * the Service.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 */
@SuppressWarnings("deprecation")
public class ServiceUsageMBeanImpl extends StandardMBean implements
		ServiceUsageMBean, UsageMBeanImplParent,
		Serializable {

	private static final long serialVersionUID = 2670146310843436229L;

	private static transient Logger logger = Logger
			.getLogger(ServiceUsageMBeanImpl.class);

	/**
	 * This is the service ID for this service usage mbean.
	 * 
	 */
	private ServiceID serviceID;

	/**
	 * the sbb usage mbeans registred by this service usage mbean
	 */
	private ConcurrentHashMap<SbbUsageMBeanMapKey, UsageMBeanImpl> usageMBeans = new ConcurrentHashMap<SbbUsageMBeanMapKey, UsageMBeanImpl>();

	/**
	 * the usage notification manager mbeans per sbb id
	 */
	private ConcurrentHashMap<SbbID, UsageNotificationManagerMBeanImpl> notificationManagers = new ConcurrentHashMap<SbbID, UsageNotificationManagerMBeanImpl>();

	public ServiceUsageMBeanImpl(ServiceComponent serviceComponent)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		super(ServiceUsageMBean.class);
		this.serviceID = serviceComponent.getServiceID();
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		try {
			sleeContainer.getMBeanServer().registerMBean(this, getObjectName());
		} catch (Throwable e) {
			throw new SLEEException(
					"unable to register service usage mbean for " + serviceID,
					e);
		}
		try {
			// install all the default usage parameters
			for (SbbID sbbID : serviceComponent.getSbbIDs(sleeContainer
					.getComponentRepositoryImpl())) {
				createUsageParameterSet(sbbID);
			}
		} catch (Throwable e) {
			throw new SLEEException(
					"unable to create default usage param sets for "
							+ serviceID, e);
		}
		serviceComponent.setServiceUsageMBean(this);
	}

	@Override
	public String toString() {
		return serviceID + " Usage MBean : " 
			+ "\n+-- Notification Managers: "	+ notificationManagers.keySet()
			+ "\n+-- Usage MBeans: "	+ usageMBeans.keySet();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getService()
	 */
	public ServiceID getService() throws ManagementException {
		return this.serviceID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#createUsageParameterSet(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public void createUsageParameterSet(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		if (name == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (name.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(name))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");

		_createUsageParameterSet(sbbId, name, true);

	}

	/*
	 * creates the default usage parameter set
	 */
	public void createUsageParameterSet(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {
		_createUsageParameterSet(sbbId, null, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#createUsageParameterSet(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	private synchronized void _createUsageParameterSet(SbbID sbbId,
			String name, boolean failIfSbbHasNoUsageParamSet)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		// get sbb usage parameter class
		Class<?> usageParameterClass = sbbComponent
				.getUsageParametersConcreteClass();
		if (usageParameterClass == null) {
			if (failIfSbbHasNoUsageParamSet) {
				throw new InvalidArgumentException(sbbId.toString()
						+ " does not define a usage parameters interface");
			} else {
				return;
			}
		}
		// check if the usage parameter name set already exists
		SbbUsageMBeanMapKey mapKey = new SbbUsageMBeanMapKey(sbbId, name);
		if (this.usageMBeans.containsKey(mapKey)) {
			throw new UsageParameterSetNameAlreadyExistsException("name "
					+ name + " already exists for service " + serviceComponent
					+ " and sbb " + sbbComponent);
		}

		UsageMBeanImpl usageMbean = null;
		UsageNotificationManagerMBeanImpl usageNotificationManagerMBean = null;
		Thread currentThread = Thread.currentThread();
		ClassLoader currentThreadClassLoader = currentThread
				.getContextClassLoader();
		try {
			// change class loader
			currentThread.setContextClassLoader(sbbComponent.getClassLoader());
			// create the actual usage parameter instance and map it in the
			// mbean
			InstalledUsageParameterSet installedUsageParameterSet = (InstalledUsageParameterSet) usageParameterClass.newInstance();
			// create and register the usage mbean
			Class<?> usageParameterMBeanClass = sbbComponent
					.getUsageParametersMBeanImplConcreteClass();
			SbbNotification sbbNotification = new SbbNotification(serviceID,
					sbbId);
			Constructor<?> constructor = null;
			if (sbbComponent.isSlee11()) {
				constructor = usageParameterMBeanClass
						.getConstructor(new Class[] { Class.class,
								NotificationSource.class });
			} else {
				constructor = usageParameterMBeanClass
						.getConstructor(new Class[] { Class.class,
								SbbNotification.class });
			}
			ObjectName usageParameterMBeanObjectName = generateUsageParametersMBeanObjectName(
					name, sbbId, sbbComponent.isSlee11());
			usageMbean = (UsageMBeanImpl) constructor.newInstance(new Object[] {
					sbbComponent.getUsageParametersMBeanConcreteInterface(),
					sbbNotification });
			usageMbean.setObjectName(usageParameterMBeanObjectName);
			usageMbean.setParent(this);
			sleeContainer.getMBeanServer().registerMBean(usageMbean,
					usageParameterMBeanObjectName);
			// set the usage param data related with the mbean
			installedUsageParameterSet.setName(name);
			installedUsageParameterSet.setUsageMBean(usageMbean);
			usageMbean.setUsageParameter(installedUsageParameterSet);
			// store the mbean
			this.usageMBeans.put(mapKey, usageMbean);
			// if it's the default usage param set and it's an slee 1.1. sbb
			// then we have to create the notification manager too
			if (sbbComponent.isSlee11() && name == null) {
				Class<?> usageNotificationManagerMBeanClass = sbbComponent
						.getUsageNotificationManagerMBeanImplConcreteClass();
				constructor = usageNotificationManagerMBeanClass
						.getConstructor(new Class[] { Class.class,
								NotificationSource.class,SleeComponentWithUsageParametersInterface.class });
				usageNotificationManagerMBean = (UsageNotificationManagerMBeanImpl) constructor
						.newInstance(new Object[] {
								sbbComponent
										.getUsageNotificationManagerMBeanConcreteInterface(),
								sbbNotification ,sbbComponent});
				ObjectName usageNotificationManagerMBeanObjectName = generateUsageNotificationManagerMBeanObjectName(sbbId);
				usageNotificationManagerMBean
						.setObjectName(usageNotificationManagerMBeanObjectName);
				sleeContainer.getMBeanServer().registerMBean(
						usageNotificationManagerMBean,
						usageNotificationManagerMBeanObjectName);
				this.notificationManagers.put(sbbId,
						usageNotificationManagerMBean);
			}

		} catch (Throwable e) {
			if (mapKey != null && usageMbean != null) {
				this.usageMBeans.remove(mapKey);
				try {
					sleeContainer.getMBeanServer().unregisterMBean(
							usageMbean.getObjectName());
				} catch (Throwable f) {
					logger.error("failed to unregister usage parameter mbean "
							+ usageMbean.getObjectName());
				}
			}
			if (usageNotificationManagerMBean != null) {
				this.notificationManagers.remove(sbbId);
				try {
					sleeContainer.getMBeanServer().unregisterMBean(
							usageNotificationManagerMBean.getObjectName());
				} catch (Throwable f) {
					logger
							.error("failed to unregister usage notification manager mbean "
									+ usageNotificationManagerMBean
											.getObjectName());
				}
			}
			throw new ManagementException(e.getMessage(), e);
		} finally {
			currentThread.setContextClassLoader(currentThreadClassLoader);
		}
	}

	private boolean isValidUsageParameterName(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!(Character.isDigit(c) || Character.isLetter(c) || (c <= '\u007e' && c >= '\u0020'))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#removeUsageParameterSet(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public void removeUsageParameterSet(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {

		if (name == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (name.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(name))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");

		_removeUsageParameterSet(sbbId, name);
	}

	public void removeAllUsageParameterSet() {
		for (SbbUsageMBeanMapKey mapKey : usageMBeans.keySet()) {
			try {
				_removeUsageParameterSet(mapKey.sbbID, mapKey.paramName);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private synchronized void _removeUsageParameterSet(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		// get sbb usage parameter class
		Class<?> usageParameterClass = sbbComponent
				.getUsageParametersConcreteClass();
		if (usageParameterClass == null) {
			throw new InvalidArgumentException(sbbId.toString()
					+ " does not define a usage parameters interface");
		}

		SbbUsageMBeanMapKey mapKey = new SbbUsageMBeanMapKey(sbbId, name);
		UsageMBeanImpl usageMbean = null;
		try {
			// remove from this mbean map
			usageMbean = this.usageMBeans.remove(mapKey);
			if (usageMbean == null) {
				throw new UnrecognizedUsageParameterSetNameException(name);
			}
			sleeContainer.getMBeanServer().unregisterMBean(
					usageMbean.getObjectName());
			if (name == null && sbbComponent.isSlee11()) {
				removeNotificationManager(sbbId);
			}
		} catch (Throwable e) {
			// rollback changes
			if(usageMbean!=null)
			{
				if (mapKey != null && usageMbean != null) {
					this.usageMBeans.put(mapKey, usageMbean);
				}
				try {
					sleeContainer.getMBeanServer().registerMBean(usageMbean,
							usageMbean.getObjectName());
				} catch (Throwable f) {
					logger.error("failed to re-register usage parameter mbean "
							+ usageMbean.getObjectName());
				}
			}
			// note: removal rollback of notification manager is done by the
			// removeNotificationManager() method
			if(e instanceof UnrecognizedUsageParameterSetNameException)
			{
				throw (UnrecognizedUsageParameterSetNameException)e;
			}else
			{	
				throw new ManagementException(e.getMessage(), e);
			}
		}
	}

	/**
	 * This method returns a list containing the names of the named SBB usage
	 * parameter sets that belong to the SBB specified by the sbbID argument and
	 * the Service represented by the ServiceUsageMBean object.
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getUsageParameterSets(javax.slee.SbbID)
	 */
	public synchronized String[] getUsageParameterSets(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		} else {
			if (sbbComponent.getUsageParametersInterface() == null) {
				throw new InvalidArgumentException(
						"no usage parameter interface for " + sbbId);
			}
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		Set<String> resultSet = new HashSet<String>();
		for (UsageMBeanImpl usageMBeanImpl : usageMBeans.values()) {
			if (((SbbNotification) usageMBeanImpl.getNotificationSource())
					.getSbb().equals(sbbId)) {
				String name = usageMBeanImpl.getUsageParameterSet();
				if (name != null) {
					resultSet.add(name);
				}
			}
		}
		return resultSet.toArray(new String[resultSet.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getSbbUsageMBean(javax.slee.SbbID)
	 */
	public ObjectName getSbbUsageMBean(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {
		try {
			return _getSbbUsageMBean(sbbId, null);
		} catch (UnrecognizedUsageParameterSetNameException e) {
			throw new ManagementException(
					"default usage parameter name not found", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#getSbbUsageMBean(javax.slee.SbbID,
	 *      java.lang.String)
	 */
	public ObjectName getSbbUsageMBean(SbbID sbbId, String name)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (name == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (name.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(name))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		return _getSbbUsageMBean(sbbId, name);
	}

	private synchronized ObjectName _getSbbUsageMBean(SbbID sbbId, String name)
			throws UnrecognizedSbbException, InvalidArgumentException,
			ManagementException, UnrecognizedUsageParameterSetNameException {

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		} else {
			if (sbbComponent.getUsageParametersInterface() == null) {
				throw new InvalidArgumentException(
						"no usage parameter interface for " + sbbId);
			}
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}

		UsageMBeanImpl bean = null;
		try {
			SbbUsageMBeanMapKey mapKey = new SbbUsageMBeanMapKey(sbbId, name);
			bean=  usageMBeans.get(mapKey);
			
			
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
		
		if (bean == null || bean.getObjectName() == null) {
			throw new UnrecognizedUsageParameterSetNameException(name);
		} else {
			return bean.getObjectName();
		}
	}

	/**
	 * Resets the usage parameters of only the SBB specified by the sbbID
	 * argument argument (within the Service represented by the
	 * ServiceUsageMBean object).
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#resetAllUsageParameters(javax.slee.SbbID)
	 */
	public synchronized void resetAllUsageParameters(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("resetAllUsageParameters: " + sbbId);
		}

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		} else {
			if (sbbComponent.getUsageParametersInterface() == null) {
				throw new InvalidArgumentException(
						"no usage parameter interface for " + sbbId);
			}
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}

		for (UsageMBeanImpl usageMBeanImpl : usageMBeans.values()) {
			SbbNotification sbbNotification = (SbbNotification) usageMBeanImpl
					.getNotificationSource();
			if (sbbNotification.getSbb().equals(sbbId)) {
				usageMBeanImpl.resetAllUsageParameters();
			}
		}
	}

	/**
	 * Resets the usage parameters of all SBBs within the Service represented by
	 * the ServiceUsageMBean object. The SLEE sets counter-type usage parameters
	 * to zero and removes all samples from sample-type usage parameters.
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#resetAllUsageParameters()
	 */
	public synchronized void resetAllUsageParameters()
			throws ManagementException {
		try {
			for (UsageMBeanImpl usageMBeanImpl : usageMBeans.values()) {
				usageMBeanImpl.resetAllUsageParameters();
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	public void remove() {
		if (logger.isDebugEnabled()) {
			logger.debug("Unregistring Usage MBean of service " + serviceID);
		}
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final MBeanServer mbeanServer = sleeContainer.getMBeanServer();
		try {
			mbeanServer.unregisterMBean(getObjectName());
		} catch (Exception e) {
			logger.error("failed to remove service usage mbean "
					+ getObjectName(), e);
		}
		// remove all service usage param
		if (logger.isDebugEnabled()) {
			logger.debug("Removing all usage parameters of service "
					+ serviceID);
		}
		removeAllUsageParameterSet();
		// remove usage mbean from service component
		sleeContainer.getComponentRepositoryImpl().getComponentByID(serviceID)
				.setServiceUsageMBean(null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#close()
	 */
	public void close() throws ManagementException {
		// ignore
	}

	public ObjectName getSbbUsageNotificationManagerMBean(SbbID sbbId)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {

		if (sbbId == null)
			throw new NullPointerException("Sbb ID is null!");

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get the sbb component
		SbbComponent sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(sbbId);
		if (sbbComponent == null) {
			throw new UnrecognizedSbbException(sbbId.toString());
		} else {
			if (sbbComponent
					.getUsageNotificationManagerMBeanConcreteInterface() == null) {
				throw new InvalidArgumentException(
						"no notification manager defined for " + sbbId);
			}
		}
		// get service component and check if the sbb belongs to the service
		ServiceComponent serviceComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(getService());
		if (!serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}

		try {
			return generateUsageNotificationManagerMBeanObjectName(sbbId);
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	public ConcurrentHashMap<SbbUsageMBeanMapKey, UsageMBeanImpl> getUsageParameterSets() {
		return this.usageMBeans;
	}

	public ObjectName getObjectName() {
		try {
			return getObjectName(serviceID);
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

	private ObjectName generateUsageParametersMBeanObjectName(String name,
			SbbID sbbID, boolean isSlee11) throws MalformedObjectNameException,
			NullPointerException {
		String objectNameAsString = UsageMBean.BASE_OBJECT_NAME
				+ (name != null ? "," + UsageMBean.USAGE_PARAMETER_SET_NAME_KEY
						+ '=' + ObjectName.quote(name) : "")
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ (isSlee11 ? SbbNotification.USAGE_NOTIFICATION_TYPE
						: SbbUsageMBean.USAGE_NOTIFICATION_TYPE) + ','
				+ SbbNotification.SERVICE_NAME_KEY + '='
				+ ObjectName.quote(serviceID.getName()) + ','
				+ SbbNotification.SERVICE_VENDOR_KEY + '='
				+ ObjectName.quote(serviceID.getVendor()) + ','
				+ SbbNotification.SERVICE_VERSION_KEY + '='
				+ ObjectName.quote(serviceID.getVersion()) + ','
				+ SbbNotification.SBB_NAME_KEY + '='
				+ ObjectName.quote(sbbID.getName()) + ','
				+ SbbNotification.SBB_VENDOR_KEY + '='
				+ ObjectName.quote(sbbID.getVendor()) + ','
				+ SbbNotification.SBB_VERSION_KEY + '='
				+ ObjectName.quote(sbbID.getVersion());
		return new ObjectName(objectNameAsString);
	}

	private ObjectName generateUsageNotificationManagerMBeanObjectName(
			SbbID sbbID) throws MalformedObjectNameException,
			NullPointerException {
		String objectNameAsString = UsageNotificationManagerMBean.BASE_OBJECT_NAME
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ SbbNotification.USAGE_NOTIFICATION_TYPE
				+ ','
				+ SbbNotification.SERVICE_NAME_KEY
				+ '='
				+ ObjectName.quote(serviceID.getName())
				+ ','
				+ SbbNotification.SERVICE_VENDOR_KEY
				+ '='
				+ ObjectName.quote(serviceID.getVendor())
				+ ','
				+ SbbNotification.SERVICE_VERSION_KEY
				+ '='
				+ ObjectName.quote(serviceID.getVersion())
				+ ','
				+ SbbNotification.SBB_NAME_KEY
				+ '='
				+ ObjectName.quote(sbbID.getName())
				+ ','
				+ SbbNotification.SBB_VENDOR_KEY
				+ '='
				+ ObjectName.quote(sbbID.getVendor())
				+ ','
				+ SbbNotification.SBB_VERSION_KEY
				+ '='
				+ ObjectName.quote(sbbID.getVersion());
		return new ObjectName(objectNameAsString);
	}

	/**
	 * Convenience method to retrieve the default
	 * {@link InstalledUsageParameterSet} for the specified sbb
	 * 
	 * @param sbbID
	 * @return
	 * @throws UnrecognizedUsageParameterSetNameException
	 */
	public InstalledUsageParameterSet getDefaultInstalledUsageParameterSet(
			SbbID sbbID) {
		return _getInstalledUsageParameterSet(sbbID, null);
	}

	/**
	 * Convenience method to retrieve the {@link InstalledUsageParameterSet} for
	 * the specified sbb and name
	 * 
	 * @param sbbID
	 * @param name
	 * @return
	 * @throws UnrecognizedUsageParameterSetNameException
	 */
	public InstalledUsageParameterSet getInstalledUsageParameterSet(
			SbbID sbbID, String name)
			throws UnrecognizedUsageParameterSetNameException {
		if (name == null) {
			throw new NullPointerException("null name");
		}
		InstalledUsageParameterSet installedUsageParameterSet = _getInstalledUsageParameterSet(
				sbbID, name);
		if (installedUsageParameterSet == null) {
			throw new UnrecognizedUsageParameterSetNameException(name);
		}
		return installedUsageParameterSet;
	}

	private InstalledUsageParameterSet _getInstalledUsageParameterSet(
			SbbID sbbID, String name) {
		if (sbbID == null) {
			throw new NullPointerException("null sbb id");
		}
		SbbUsageMBeanMapKey mapKey = new SbbUsageMBeanMapKey(sbbID, name);
		UsageMBeanImpl usageMBean = usageMBeans.get(mapKey);
		if (usageMBean == null) {
			return null;
		} else {
			return usageMBean.getUsageParameter();
		}
	}

	public UsageNotificationManagerMBeanImpl getUsageNotificationManagerMBean(
			NotificationSource notificationSource) {
		return notificationManagers.get(((SbbNotification) notificationSource)
				.getSbb());
	}

	public void removeChild(UsageMBeanImpl usageMBeanImpl) {
		try {
			removeUsageParameterSet(((SbbNotification) usageMBeanImpl
					.getNotificationSource()).getSbb(), usageMBeanImpl
					.getUsageParameterSet());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void removeChild(UsageNotificationManagerMBeanImpl child) {
		try {
			removeNotificationManager(((SbbNotification) child
					.getNotificationSource()).getSbb());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void removeNotificationManager(SbbID sbbId)
			throws ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		UsageNotificationManagerMBeanImpl usageMbean = null;
		try {
			// remove from this mbean map
			usageMbean = this.notificationManagers.remove(sbbId);
			if (usageMbean != null) {
				sleeContainer.getMBeanServer().unregisterMBean(
						usageMbean.getObjectName());
			}
		} catch (Throwable e) {
			// rollback changes
			if (usageMbean != null) {
				this.notificationManagers.put(sbbId, usageMbean);
			}
			try {
				sleeContainer.getMBeanServer().registerMBean(usageMbean,
						usageMbean.getObjectName());
			} catch (Throwable f) {
				logger.error("failed to re-register usage parameter mbean "
						+ usageMbean.getObjectName());
			}
			throw new ManagementException(e.getMessage(), e);
		}
	}

	public class SbbUsageMBeanMapKey {

		private final SbbID sbbID;
		private final String paramName;

		public SbbUsageMBeanMapKey(SbbID sbbID, String paramName) {
			this.sbbID = sbbID;
			this.paramName = paramName;
		}

		@Override
		public int hashCode() {
			return sbbID.hashCode() * 31
					+ (paramName != null ? paramName.hashCode() : 0);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == this.getClass()) {
				SbbUsageMBeanMapKey other = (SbbUsageMBeanMapKey) obj;
				// check sbb id
				if (!this.sbbID.equals(other.sbbID)) {
					return false;
				}
				// check param name, may be null
				if (other.paramName == null) {
					if (this.paramName != null) {
						return false;
					}
				} else {
					if (!other.paramName.equals(this.paramName)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
	 */
	public static ObjectName getObjectName(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		if (serviceID == null)
			throw new NullPointerException("Null service ID ");

		try {
			return new ObjectName(ServiceUsageMBean.BASE_OBJECT_NAME + ','
					+ ServiceUsageMBean.SERVICE_NAME_KEY + '='
					+ ObjectName.quote(serviceID.getName()) + ','
					+ ServiceUsageMBean.SERVICE_VENDOR_KEY + '='
					+ ObjectName.quote(serviceID.getVendor()) + ','
					+ ServiceUsageMBean.SERVICE_VERSION_KEY + '='
					+ ObjectName.quote(serviceID.getVersion()));
		} catch (Exception e) {
			throw new ManagementException(
					"Exception while getting service usage mbean for service with id "
							+ serviceID, e);
		}
	}
}
