package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.SbbID;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.ResourceUsageMBean;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.slee.usage.UsageMBean;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl.SbbUsageMBeanMapKey;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

public class ResourceUsageMBeanImpl extends StandardMBean implements ResourceUsageMBean,UsageMBeanImplParent, UsageNotificationManagerMBeanImplParent, Serializable {

	private static final long serialVersionUID = 2670146310843436229L;
		
	private static transient Logger logger = Logger
	.getLogger(ResourceUsageMBeanImpl.class);
	
	/**
	 * the ra entity name for this mbean
	 */
	private String entityName;
	
	/**
	 * the object name used to register this mbean
	 */
	private ObjectName objectName;
	
	/**
	 * the usage mbeans registred by this resource usage mbean
	 */
	private ConcurrentHashMap<String, UsageMBeanImpl> usageMBeans = new ConcurrentHashMap<String, UsageMBeanImpl>();

	/**
	 * the defualt usage mbean
	 */
	private UsageMBeanImpl defaultUsageMBean;
	
	/**
	 * the usage notification manager mbean
	 */
	private UsageNotificationManagerMBeanImpl notificationManager;
	
	public ResourceUsageMBeanImpl(String entityName)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		super(ResourceUsageMBean.class);
		this.entityName = entityName;
	}
	
	public String getEntityName() throws ManagementException {
		return entityName;
	}
	
	public ObjectName getObjectName() {
		return objectName;
	}

	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}
	
	public void close() throws ManagementException {
		// FIXME the removal of service usage and usage param mbeans should be restored on a rollback
		if (logger.isDebugEnabled()) {
			logger.debug("Unregistring Usage MBean of ra entity "
					+ getEntityName());
		}
		final MBeanServer mbeanServer = SleeContainer.lookupFromJndi().getMBeanServer();
		try {
			mbeanServer.unregisterMBean(getObjectName());
		}
		catch (Exception e) {
			logger.error("failed to remove service usage mbean "+getObjectName(),e);
		}
		// remove all service usage param
		if (logger.isDebugEnabled()) {
			logger.debug("Removing all usage parameters of ra entity "
					+ getEntityName());
		}
		for (String name : usageMBeans.keySet()) {
			try {
				removeUsageParameterSet(name);
			} catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
		}	
		// also remove the default
		try {
			removeUsageParameterSet();
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		}
	}

	public void createUsageParameterSet() throws NullPointerException,
			InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {
		_createUsageParameterSet(null, false);
	}

	public void createUsageParameterSet(String paramSetName)
			throws NullPointerException, InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		if (paramSetName == null)
			throw new NullPointerException("Sbb usage param set is null");
		if (paramSetName.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(paramSetName))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");

		_createUsageParameterSet(paramSetName, true);
	}	

	private synchronized void _createUsageParameterSet(String name, boolean failIfSbbHasNoUsageParamSet)
			throws NullPointerException, UsageParameterSetNameAlreadyExistsException, ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		// get ra entity component
		ResourceAdaptorEntity raEntity = sleeContainer.getResourceManagement().getResourceAdaptorEntity(entityName);
		SleeComponentWithUsageParametersInterface component = raEntity.getComponent();
		
		// get usage parameter class
		Class usageParameterClass = component.getUsageParametersConcreteClass();
		
		// check if the usage parameter name set already exists
		if (this.usageMBeans.containsKey(name)) {
			throw new UsageParameterSetNameAlreadyExistsException("name "
					+ name + " already exists for ra entity " + entityName);
		}

		UsageMBeanImpl usageMbean = null;
		UsageNotificationManagerMBeanImpl usageNotificationManagerMBean = null;
		Thread currentThread = Thread.currentThread();
		ClassLoader currentThreadClassLoader = currentThread
				.getContextClassLoader();
		try {
			// change class loader
			currentThread.setContextClassLoader(component.getClassLoader());
			// create the actual usage parameter instance and map it in the
			// mbean
			InstalledUsageParameterSet installedUsageParameterSet = (InstalledUsageParameterSet) usageParameterClass.getConstructor(null)
					.newInstance(new Object[0]);
			// create and register the usage mbean
			Class usageParameterMBeanClass = component.getUsageParametersMBeanImplConcreteClass();
			Constructor constructor = usageParameterMBeanClass.getConstructor(new Class[] { Class.class, NotificationSource.class });				 
			
			ObjectName usageParameterMBeanObjectName = generateUsageParametersMBeanObjectName(name);
			usageMbean = (UsageMBeanImpl) constructor.newInstance(new Object[] {component.getUsageParametersMBeanConcreteInterface(), raEntity.getNotificationSource()});
			usageMbean.setObjectName(usageParameterMBeanObjectName);
			usageMbean.setParent(this);
			sleeContainer.getMBeanServer().registerMBean(usageMbean, usageParameterMBeanObjectName);
			// set the usage param data related with the mbean
			installedUsageParameterSet.setName(name);
			installedUsageParameterSet.setUsageMBean(usageMbean);
			usageMbean.setUsageParameter(installedUsageParameterSet);
			// store the mbean
			if (name != null) {
				this.usageMBeans.put(name, usageMbean);
			}
			else {
				// defualt mbean
				this.defaultUsageMBean = usageMbean;
				// create notification manager
				Class usageNotificationManagerMBeanClass = component.getUsageNotificationManagerMBeanImplConcreteClass();
				constructor = usageNotificationManagerMBeanClass.getConstructor(new Class[] { Class.class, NotificationSource.class });				
				usageNotificationManagerMBean = (UsageNotificationManagerMBeanImpl) constructor.newInstance(new Object[] {component.getUsageNotificationManagerMBeanConcreteInterface(), raEntity.getNotificationSource()});
				ObjectName usageNotificationManagerMBeanObjectName = generateUsageNotificationManagerMBeanObjectName();
				usageNotificationManagerMBean.setObjectName(usageNotificationManagerMBeanObjectName);
				usageNotificationManagerMBean.setParent(this);
				sleeContainer.getMBeanServer().registerMBean(usageNotificationManagerMBean, usageNotificationManagerMBeanObjectName);
				this.notificationManager = usageNotificationManagerMBean;
			}		
		} catch (Throwable e) {
			if (usageMbean != null) {
				if (name != null) {
					this.usageMBeans.remove(name);
				}
				else {
					this.defaultUsageMBean = null;
				}
				try {
					sleeContainer.getMBeanServer().unregisterMBean(
							usageMbean.getObjectName());
				} catch (Throwable f) {
					logger.error("failed to unregister usage parameter mbean "
							+ usageMbean.getObjectName());
				}
			}
			if (usageNotificationManagerMBean != null) {
				this.notificationManager = null;
				try {
					sleeContainer.getMBeanServer().unregisterMBean(
							usageNotificationManagerMBean.getObjectName());
				} catch (Throwable f) {
					logger.error("failed to unregister usage notification manager mbean "
							+ usageNotificationManagerMBean.getObjectName());
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
	
	public ObjectName getUsageMBean() throws ManagementException {
		try {
			return _getUsageMBean(null);
		} catch (UnrecognizedUsageParameterSetNameException e) {
			throw new ManagementException(
					"default usage parameter name not found", e);
		}
	}

	public ObjectName getUsageMBean(String paramSetName) throws NullPointerException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (paramSetName == null)
			throw new NullPointerException("Sbb usage param set is null");
		return _getUsageMBean(paramSetName);
	}

	private synchronized ObjectName _getUsageMBean(String name)
			throws ManagementException, UnrecognizedUsageParameterSetNameException {

		UsageMBeanImpl usageMBeanImpl = usageMBeans.get(name);
		if (usageMBeanImpl == null) {
			throw new UnrecognizedUsageParameterSetNameException(name);
		} else {
			return usageMBeanImpl.getObjectName();
		}
	}
	
	public ObjectName getUsageNotificationManagerMBean()
			throws ManagementException {
		return notificationManager.getObjectName();
	}

	public String[] getUsageParameterSets() throws ManagementException {
		return usageMBeans.keySet().toArray(new String[0]);
	}

	private void removeUsageParameterSet()
	throws ManagementException, UnrecognizedUsageParameterSetNameException {
		_removeUsageParameterSet(null);
	}

	public void removeUsageParameterSet(String paramSetName)
			throws NullPointerException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (paramSetName == null)
			throw new NullPointerException("usage param set is null");
		_removeUsageParameterSet(paramSetName);
	}

	private synchronized void _removeUsageParameterSet(String name)
			throws UnrecognizedUsageParameterSetNameException, ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		UsageMBeanImpl usageMbean = null;
		try {
			if (name != null) {
				// remove from this mbean map
				usageMbean = this.usageMBeans.remove(name);
			}
			else {
				usageMbean = defaultUsageMBean;
				defaultUsageMBean = null;
			}
			if (usageMbean == null) {
				throw new UnrecognizedUsageParameterSetNameException(name);
			}
			sleeContainer.getMBeanServer().unregisterMBean(
					usageMbean.getObjectName());
			if (name == null) {
				removeNotificationManager();
			}
		} catch (Throwable e) {
			// rollback changes
			if (usageMbean != null) {
				if (name != null) {
					// remove from this mbean map
					usageMbean = this.usageMBeans.put(name,usageMbean);
				}
				else {
					defaultUsageMBean = usageMbean;
				}
			}
			try {
				sleeContainer.getMBeanServer().registerMBean(usageMbean,
						usageMbean.getObjectName());
			} catch (Throwable f) {
				logger.error("failed to re-register usage parameter mbean "
						+ usageMbean.getObjectName());
			}
			// note: removal rollback of notification manager is done by the removeNotificationManager() method 
			throw new ManagementException(e.getMessage(), e);
		}
	}
	
	public void resetAllUsageParameters() throws ManagementException {
		try {
			for (UsageMBeanImpl usageMBeanImpl : usageMBeans
					.values()) {
				usageMBeanImpl.resetAllUsageParameters();
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}		
	}

	public void removeChild(UsageMBeanImpl usageMBeanImpl) {
		try {
			removeUsageParameterSet(usageMBeanImpl.getUsageParameterSet());
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public void removeChild(UsageNotificationManagerMBeanImpl child) {
		try {
			removeNotificationManager();
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	private void removeNotificationManager() throws ManagementException {
		
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		try {
			sleeContainer.getMBeanServer().unregisterMBean(notificationManager.getObjectName());
			notificationManager = null;
		} catch (Throwable e) {
			// rollback changes
			try {
				sleeContainer.getMBeanServer().registerMBean(notificationManager,
						notificationManager.getObjectName());
			} catch (Throwable f) {
				logger.error("failed to re-register usage parameter mbean "
						+ notificationManager.getObjectName());
			}
			throw new ManagementException(e.getMessage(), e);
		}		
	}

	private ObjectName generateUsageParametersMBeanObjectName(String name) throws MalformedObjectNameException, NullPointerException {
		String objectNameAsString = UsageMBean.BASE_OBJECT_NAME + 
		(name != null ? "," + UsageMBean.USAGE_PARAMETER_SET_NAME_KEY + '=' + ObjectName.quote(name) : "") + 
		',' + UsageMBean.NOTIFICATION_SOURCE_KEY + '=' + ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE +
		',' + ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY + '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}
	
	private ObjectName generateUsageNotificationManagerMBeanObjectName() throws MalformedObjectNameException, NullPointerException {
		String objectNameAsString = UsageNotificationManagerMBean.BASE_OBJECT_NAME + 		 
		',' + UsageMBean.NOTIFICATION_SOURCE_KEY + '=' + ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE +
		',' + ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY + '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}

	public UsageNotificationManagerMBeanImpl getUsageNotificationManagerMBean(
			NotificationSource notificationSource) {
		return notificationManager;
	}
	
	/**
	 * Convenience method to retrieve the default {@link InstalledUsageParameterSet}
	 * @return
	 */
	public InstalledUsageParameterSet getDefaultInstalledUsageParameterSet() {
		if (defaultUsageMBean!= null) {
			return defaultUsageMBean.getUsageParameter();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Convenience method to retrieve the {@link InstalledUsageParameterSet} for the specified param set name.
	 * @param name 
	 * @return
	 */
	public InstalledUsageParameterSet getInstalledUsageParameterSet(String name) {
		
		if (name == null) {
			return getDefaultInstalledUsageParameterSet();
		}
		else {
			UsageMBeanImpl usageMBean = usageMBeans.get(name);
			if (usageMBean == null) {
				return null;
			}
			else {
				return usageMBean.getUsageParameter();
			}
		}
	}
}
