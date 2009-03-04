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
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedSbbException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceUsageMBean;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;

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
 */
public class ServiceUsageMBeanImpl extends StandardMBean implements
		ServiceUsageMBean, Serializable {

	private static final long serialVersionUID = 2670146310843436229L;

	private static transient Logger logger = Logger
			.getLogger(ServiceUsageMBeanImpl.class);

	private static final String[] emptyUsageParameterSet = new String[0];

	/**
	 * This is the service ID for this service usage mbean.
	 * 
	 */
	private ServiceID serviceID;

	/**
	 * the sbb usage mbeans registred by this service usage mbean
	 */
	private ConcurrentHashMap<String, SbbUsageMBeanImpl> sbbUsageMBeans = new ConcurrentHashMap<String, SbbUsageMBeanImpl>();

	private ObjectName objectName;

	public ServiceUsageMBeanImpl() throws NotCompliantMBeanException {
		super(ServiceUsageMBean.class);
	}

	public ServiceUsageMBeanImpl(ServiceID serviceID)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		this();
		this.serviceID = serviceID;
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
		if (serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		// get sbb usage parameter class
		Class usageParameterClass = sbbComponent
				.getUsageParametersInterfaceConcreteClass();
		if (usageParameterClass == null) {
			if (failIfSbbHasNoUsageParamSet) {
				throw new InvalidArgumentException(sbbId.toString()
						+ " does not define a usage parameters interface");
			} else {
				return;
			}
		}
		// check if the usage parameter name set already exists
		String pathName = generateUsageParametersPathName(sbbId, name);
		if (this.sbbUsageMBeans.containsKey(pathName)) {
			throw new UsageParameterSetNameAlreadyExistsException("name "
					+ name + " already exists for service " + serviceComponent
					+ " and sbb " + sbbComponent);
		}

		SbbUsageMBeanImpl usageMbean = null;
		Thread currentThread = Thread.currentThread();
		ClassLoader currentThreadClassLoader = currentThread
				.getContextClassLoader();
		try {
			// change class loader
			currentThread.setContextClassLoader(sbbComponent.getClassLoader());
			// create the actual usage parameter instance and map it in the
			// mbean
			Constructor cons = usageParameterClass.getConstructor(new Class[] {
					ServiceID.class, SbbID.class });
			InstalledUsageParameterSet installedUsageParameterSet = (InstalledUsageParameterSet) cons
					.newInstance(new Object[] { serviceID, sbbId });
			// create and register the sbb usage mbean
			String usageParameterInterfaceClassName = sbbComponent
					.getUsageParametersInterface().getName();
			String usageParameterMBeanClassName = usageParameterInterfaceClassName
					+ "MBeanImpl";
			Class[] args = { ServiceID.class, SbbID.class, String.class,
					String.class, usageParameterClass };
			Class usageParameterMBeanClass = sbbComponent.getClassLoader()
					.loadClass(usageParameterMBeanClassName);
			Constructor constructor = usageParameterMBeanClass
					.getConstructor(args);
			Object[] objs = { this.serviceID, sbbId, name,
					usageParameterInterfaceClassName + "MBean",
					installedUsageParameterSet };
			ObjectName objectName = new ObjectName("slee:SbbUsageMBean="
					+ pathName);
			usageMbean = (SbbUsageMBeanImpl) constructor.newInstance(objs);
			usageMbean.setObjectName(objectName);
			sleeContainer.getMBeanServer()
					.registerMBean(usageMbean, objectName);
			installedUsageParameterSet.setName(name);
			installedUsageParameterSet.setSbbUsageMBean(usageMbean);
			usageMbean.setUsageParameter(installedUsageParameterSet);
			this.sbbUsageMBeans.put(pathName, usageMbean);

		} catch (Throwable e) {
			if (pathName != null && usageMbean != null) {
				this.sbbUsageMBeans.remove(pathName);
				try {
					sleeContainer.getMBeanServer().unregisterMBean(
							usageMbean.getObjectName());
				} catch (Throwable f) {
					logger.error("failed to unregister usage parameter mbean "
							+ usageMbean.getObjectName());
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
		MBeanServer mBeanServer = SleeContainer.lookupFromJndi().getMBeanServer();		
		for (String pathName : sbbUsageMBeans.keySet()) {
			SbbUsageMBeanImpl usageMbean = sbbUsageMBeans.remove(pathName);
			try {
				mBeanServer.unregisterMBean(
						usageMbean.getObjectName());				
			} catch (Throwable e) {
				logger.error("failed to unregister usage parameter mbean "
							+ usageMbean.getObjectName(),e);				
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
		if (serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		// get sbb usage parameter class
		Class usageParameterClass = sbbComponent
				.getUsageParametersInterfaceConcreteClass();
		if (usageParameterClass == null) {
			throw new InvalidArgumentException(sbbId.toString()
					+ " does not define a usage parameters interface");
		}

		String pathName = generateUsageParametersPathName(sbbId, name);
		SbbUsageMBeanImpl usageMbean = null;
		try {
			// remove from this mbean map
			usageMbean = this.sbbUsageMBeans.remove(pathName);
			if (usageMbean == null) {
				throw new UnrecognizedUsageParameterSetNameException(name);
			}
			sleeContainer.getMBeanServer().unregisterMBean(
					usageMbean.getObjectName());
		} catch (Throwable e) {
			// rollback changes
			if (pathName != null && usageMbean != null) {
				this.sbbUsageMBeans.put(pathName, usageMbean);
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
		if (serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}
		Set<String> resultSet = new HashSet<String>();
		for (SbbUsageMBeanImpl sbbUsageMBeanImpl : sbbUsageMBeans.values()) {
			if (sbbUsageMBeanImpl.getSbb().equals(sbbId)) {
				String name = sbbUsageMBeanImpl.getUsageParameterSet();
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
		if (serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}

		ObjectName oName = null;
		try {
			String pathName = generateUsageParametersPathName(sbbId, name);
			oName = sbbUsageMBeans.get(pathName).getObjectName();
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}

		if (oName == null) {
			throw new UnrecognizedUsageParameterSetNameException(name);
		} else {
			return oName;
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
		if (serviceComponent.getSbbIDs(
				sleeContainer.getComponentRepositoryImpl()).contains(sbbId)) {
			throw new UnrecognizedSbbException(sbbId.toString()
					+ " is not part of " + getService());
		}

		for (SbbUsageMBeanImpl sbbUsageMBeanImpl : sbbUsageMBeans.values()) {
			if (sbbUsageMBeanImpl.getSbb().equals(sbbId)) {
				sbbUsageMBeanImpl.resetAllUsageParameters();
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
			for (SbbUsageMBeanImpl sbbUsageMBeanImpl : sbbUsageMBeans
					.values()) {
				sbbUsageMBeanImpl.resetAllUsageParameters();
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceUsageMBean#close()
	 */
	public void close() throws ManagementException {
		// FIXME the removal of service usage and usage param mbeans should be restored on a rollback
		if (logger.isDebugEnabled()) {
			logger.debug("Unregistring Usage MBean of service "
					+ getService());
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
			logger.debug("Removing all usage parameters of service "
					+ getService());
		}
		removeAllUsageParameterSet();

	}

	public ObjectName getSbbUsageNotificationManager(SbbID arg0)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {
		// TODO Auto-generated method stub

	}

	public ObjectName getSbbUsageNotificationManagerMBean(SbbID arg0)
			throws NullPointerException, UnrecognizedSbbException,
			InvalidArgumentException, ManagementException {
		// TODO Auto-generated method stub

	}

	public ConcurrentHashMap<String, SbbUsageMBeanImpl> getUsageParameterSets() {
		return this.sbbUsageMBeans;
	}

	public ObjectName getObjectName() {
		return objectName;
	}

	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}

	private String generateUsageParametersPathName(SbbID sbbID, String name) {

		String pathName = this.serviceID.toString() + "/" + sbbID;
		if (name != null) {
			pathName += "/" + SleeContainerUtils.toHex(name);
		}
		return pathName;
	}
	
	/**
	 * Convenience method to retrieve the default {@link InstalledUsageParameterSet} for the specified sbb
	 * @param sbbID
	 * @return
	 */
	public InstalledUsageParameterSet getDefaultInstalledUsageParameterSet(SbbID sbbID) {
		return getInstalledUsageParameterSet(sbbID, null);
	}
	
	/**
	 * Convenience method to retrieve the {@link InstalledUsageParameterSet} for the specified sbb and name
	 * @param sbbID
	 * @param name
	 * @return
	 */
	public InstalledUsageParameterSet getInstalledUsageParameterSet(SbbID sbbID, String name) {
		if (sbbID == null) {
			throw new NullPointerException(sbbID.toString());
		}
		String pathName = generateUsageParametersPathName(sbbID, name);
		SbbUsageMBeanImpl sbbUsageMBean = sbbUsageMBeans.get(pathName);
		if (sbbUsageMBean == null) {
			return null;
		}
		else {
			return sbbUsageMBean.getUsageParameter();
		}
	}
}
