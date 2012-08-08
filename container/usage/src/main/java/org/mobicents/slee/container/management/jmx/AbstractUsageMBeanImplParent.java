/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.ProfileTableUsageMBean;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.UsageParameterSetNameAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.runtime.usage.AbstractUsageParameterSet;

/**
 * Abstract class code for a "parent" usage mbean, such as the
 * {@link ResourceManagementMBean} or {@link ProfileTableUsageMBean}
 * 
 * @author martins
 * 
 */
public abstract class AbstractUsageMBeanImplParent extends StandardMBean implements
		UsageMBeanImplParent, 
		Serializable {

	
	//FIXME: maybe part of this logic should be moved to UsageManagement class?
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected abstract Logger getLogger();

	/**
	 * the object name used to register this mbean
	 */
	private ObjectName objectName;

	/**
	 * the child name param usage mbeans registred by this mbean, holds paramName->bean mapping.
	 */
	private ConcurrentHashMap<String, UsageMBeanImpl> usageMBeans = new ConcurrentHashMap<String, UsageMBeanImpl>();

	/**
	 * the default usage mbean
	 */
	private UsageMBeanImpl defaultUsageMBean;

	/**
	 * the usage notification manager mbean
	 */
	private UsageNotificationManagerMBeanImpl notificationManager;

	/**
	 * the container
	 */
	private final SleeContainer sleeContainer;

	/**
	 * the component
	 */
	private final SleeComponentWithUsageParametersInterface component;

	/**
	 * the notification source used by the usage mbeans
	 */
	private final NotificationSource notificationSource;

	/**
	 * 
	 */
	private boolean closed = false;
	
	/**
	 * Creates a new instance of an abstract usage mbean
	 * @param mBeanInterfaceClass the class poiting to the interface this mbean implements
	 * @param component the component related with this mbean
	 * @param notificationSource the notification source to be used in notifications sent by the notification manager mbean
	 * @param sleeContainer "the" container
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public AbstractUsageMBeanImplParent(Class<?> mBeanInterfaceClass,
			SleeComponentWithUsageParametersInterface component,
			NotificationSource notificationSource, SleeContainer sleeContainer)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		super(mBeanInterfaceClass);
		this.sleeContainer = sleeContainer;
		this.component = component;
		this.notificationSource = notificationSource;
	}

	/**
	 * Retrieves the object name of this mbean
	 * 
	 * @return
	 */
	public ObjectName getObjectName() {
		return objectName;
	}

	/**
	 * Sets the object name of this mbean
	 * 
	 * @param objectName
	 */
	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}

	/**
	 * Removes the mbean
	 * 
	 */
	public void remove() {

		Logger logger = getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("Closing " + toString());
		}
		final MBeanServer mbeanServer = sleeContainer.getMBeanServer();
		try {
			mbeanServer.unregisterMBean(getObjectName());
		} catch (Exception e) {
			logger.error("failed to remove " + toString(), e);
		}
		// remove all usage param
		if (logger.isDebugEnabled()) {
			logger
					.debug("Removing all named usage parameters of "
							+ toString());
		}
		for (String name : usageMBeans.keySet()) {
			try {
				_removeUsageParameterSet(name,false);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
		// also remove the default
		try {
			removeUsageParameterSet();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * 
	 * @throws ManagementException
	 */
	public void close() throws ManagementException {
		ensureMBeanIsNotClosed();
		closed = true;		
	}

	public void open() {
		closed = false;		
	}
	
	/**
	 * 
	 * @throws ManagementException
	 */
	protected void ensureMBeanIsNotClosed() throws ManagementException {
		if (closed) {
			throw new ManagementException("closed");
		}
	}
	
	/**
	 * Creates the default usage param (and its mbean)
	 * 
	 * @throws NullPointerException
	 * @throws InvalidArgumentException
	 * @throws UsageParameterSetNameAlreadyExistsException
	 * @throws ManagementException
	 */
	public void createUsageParameterSet() throws NullPointerException,
			InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {
		_createUsageParameterSet(null);	
	}

	/**
	 * Creates the usage param (and its mbean) for the specified name
	 * 
	 * @param paramSetName
	 * @throws NullPointerException
	 * @throws InvalidArgumentException
	 * @throws UsageParameterSetNameAlreadyExistsException
	 * @throws ManagementException
	 */
	public void createUsageParameterSet(String paramSetName)
			throws NullPointerException, InvalidArgumentException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		if (paramSetName == null)
			throw new NullPointerException("usage param set is null");
		if (paramSetName.length() == 0)
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");
		if (!isValidUsageParameterName(paramSetName))
			throw new InvalidArgumentException(
					"The lenght of the Usage Parameter Set Name is zero!");

		_createUsageParameterSet(paramSetName);
	}

	private synchronized void _createUsageParameterSet(String name) throws NullPointerException,
			UsageParameterSetNameAlreadyExistsException, ManagementException {

		ensureMBeanIsNotClosed();
		
		Logger logger = getLogger();

		// get usage parameter class
		Class<?> usageParameterClass = component.getUsageParametersConcreteClass();

		// check if the usage parameter name set already exists
		if (name != null && this.usageMBeans.containsKey(name)) {
				throw new UsageParameterSetNameAlreadyExistsException("name "
					+ name + " already exists for " + this);
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
			AbstractUsageParameterSet installedUsageParameterSet = (AbstractUsageParameterSet) AbstractUsageParameterSet.newInstance(usageParameterClass, notificationSource, name, sleeContainer);
			// create and register the usage mbean
			Class<?> usageParameterMBeanClass = component
					.getUsageParametersMBeanImplConcreteClass();
			Constructor<?> constructor = usageParameterMBeanClass
					.getConstructor(new Class[] { Class.class,
							NotificationSource.class });

			ObjectName usageParameterMBeanObjectName = generateUsageParametersMBeanObjectName(name);
			usageMbean = (UsageMBeanImpl) constructor.newInstance(new Object[] {
					component.getUsageParametersMBeanConcreteInterface(),
					notificationSource });
			usageMbean.setObjectName(usageParameterMBeanObjectName);
			usageMbean.setParent(this);
			sleeContainer.getMBeanServer().registerMBean(usageMbean,
					usageParameterMBeanObjectName);
			// set the usage param data related with the mbean
			
			installedUsageParameterSet.setUsageMBean(usageMbean);
			usageMbean.setUsageParameter(installedUsageParameterSet);
			// store the mbean
			if (name != null) {
				this.usageMBeans.put(name, usageMbean);
			} else {
				// default mbean
				this.defaultUsageMBean = usageMbean;
				// create notification manager
				Class<?> usageNotificationManagerMBeanClass = component
						.getUsageNotificationManagerMBeanImplConcreteClass();
				constructor = usageNotificationManagerMBeanClass
						.getConstructor(new Class[] { Class.class,
								NotificationSource.class,SleeComponentWithUsageParametersInterface.class});
				usageNotificationManagerMBean = (UsageNotificationManagerMBeanImpl) constructor
						.newInstance(new Object[] {
								component
										.getUsageNotificationManagerMBeanConcreteInterface(),
								notificationSource ,component});
				ObjectName usageNotificationManagerMBeanObjectName = generateUsageNotificationManagerMBeanObjectName();
				usageNotificationManagerMBean
						.setObjectName(usageNotificationManagerMBeanObjectName);
				sleeContainer.getMBeanServer().registerMBean(
						usageNotificationManagerMBean,
						usageNotificationManagerMBeanObjectName);
				this.notificationManager = usageNotificationManagerMBean;
			}
		} catch (Throwable e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error creating usage param set named "+name,e);
			}
			if (usageMbean != null) {
				if (name != null) {
					this.usageMBeans.remove(name);
				} else {
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

	/*
	 * verifies a name for an usage mbean
	 */
	private static boolean isValidUsageParameterName(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!(Character.isDigit(c) || Character.isLetter(c) || (c <= '\u007e' && c >= '\u0020'))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Retrieves the object name for the default usage param mbean
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public ObjectName getUsageMBean() throws ManagementException {
		try {
			return _getUsageMBean(null);
		} catch (UnrecognizedUsageParameterSetNameException e) {
			throw new ManagementException(
					"default usage parameter name not found", e);
		}
	}

	/**
	 * Retrieves the object name for the usage param mbean with the specified
	 * name
	 * 
	 * @param paramSetName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedUsageParameterSetNameException
	 * @throws ManagementException
	 */
	public ObjectName getUsageMBean(String paramSetName)
			throws NullPointerException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (paramSetName == null)
			throw new NullPointerException("Sbb usage param set is null");
		return _getUsageMBean(paramSetName);
	}

	private synchronized ObjectName _getUsageMBean(String name)
			throws ManagementException,
			UnrecognizedUsageParameterSetNameException {

		ensureMBeanIsNotClosed();
		
		Logger logger = getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("_getUsageMBean( name = "+name+")");
		}
		
		UsageMBeanImpl usageMBeanImpl = null;
		if (name != null) {
			usageMBeanImpl = usageMBeans.get(name);
		}
		else {
			usageMBeanImpl = defaultUsageMBean;
		}
		if (usageMBeanImpl == null) {
			throw new UnrecognizedUsageParameterSetNameException(name);				
		} else {
			return usageMBeanImpl.getObjectName();
		}
	}

	/**
	 * Retrieves the object name of the {@link UsageNotificationManagerMBean}
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public ObjectName getUsageNotificationManagerMBean()
			throws ManagementException {
		
		ensureMBeanIsNotClosed();
		
		return notificationManager.getObjectName();
	}

	/**
	 * Retrieves the names of the usage params, which exist in this mbean
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public String[] getUsageParameterSets() throws ManagementException {
		
		ensureMBeanIsNotClosed();
		return getUsageParameterNamesSet().toArray(new String[0]);
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getUsageParameterNamesSet() {
		return usageMBeans.keySet();
	}

	/**
	 * Removes the default usage param (and its mbean)
	 * 
	 * @throws ManagementException
	 * @throws UnrecognizedUsageParameterSetNameException
	 */
	private void removeUsageParameterSet() throws ManagementException,
			UnrecognizedUsageParameterSetNameException {
		_removeUsageParameterSet(null,false);
	}

	/**
	 * Removes the usage param (and its mbean) for the specified name
	 * 
	 * @param paramSetName
	 * @throws NullPointerException
	 * @throws UnrecognizedUsageParameterSetNameException
	 * @throws ManagementException
	 */
	public void removeUsageParameterSet(String paramSetName)
			throws NullPointerException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (paramSetName == null)
			throw new NullPointerException("usage param set is null");
		//FIXME: should we recreate beans here as well ?
		_removeUsageParameterSet(paramSetName,true);
	}

	private synchronized void _removeUsageParameterSet(String name, boolean ensureMBeanIsNotClosed)
			throws UnrecognizedUsageParameterSetNameException,
			ManagementException {

		Logger logger = getLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("_removeUsageParameterSet( name = "+name+")");
		}
		
		UsageMBeanImpl usageMbean = null;
		try {
			if (name != null) {
				// remove from this mbean map
				usageMbean = this.usageMBeans.remove(name);
			} else {
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
			//FIXME: Here possibly we should mark to be remove, or remove and throw exception o demands...?
			boolean clustered = !sleeContainer.getCluster().isSingleMember(); 
			if( !clustered)
			{
				//we are last, lets clear cache
				usageMbean.getUsageParameter().remove();
			}
		} catch (Throwable e) {
			// rollback changes
			if (usageMbean != null) {
				if (name != null) {
					// add to this mbean map
					usageMbean = this.usageMBeans.put(name, usageMbean);
				} else {
					defaultUsageMBean = usageMbean;
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
			throw new ManagementException(e.getMessage(), e);
		}
	}

	/**
	 * Resets all usage params
	 * 
	 * @throws ManagementException
	 */
	public void resetAllUsageParameters() throws ManagementException {
		
		ensureMBeanIsNotClosed();
		try {
			for (UsageMBeanImpl usageMBeanImpl : usageMBeans.values()) {
				usageMBeanImpl.resetAllUsageParameters();
			}
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	private void removeNotificationManager() throws ManagementException {

		try {
			sleeContainer.getMBeanServer().unregisterMBean(
					notificationManager.getObjectName());
			notificationManager = null;
		} catch (Throwable e) {
			// rollback changes
			try {
				sleeContainer.getMBeanServer().registerMBean(
						notificationManager,
						notificationManager.getObjectName());
			} catch (Throwable f) {
				getLogger().error(
						"failed to re-register usage parameter mbean "
								+ notificationManager.getObjectName());
			}
			throw new ManagementException(e.getMessage(), e);
		}
	}

	/**
	 * Generates the usage mbean object name for the specified usage param name
	 * 
	 * @param name
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	protected abstract ObjectName generateUsageParametersMBeanObjectName(
			String name) throws MalformedObjectNameException,
			NullPointerException;

	/**
	 * Generates the usage mbean object name for the default usage param
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	protected abstract ObjectName generateUsageNotificationManagerMBeanObjectName()
			throws MalformedObjectNameException, NullPointerException;

	/**
	 * Retrieves the usage notification manager mbean
	 */
	public UsageNotificationManagerMBeanImpl getUsageNotificationManagerMBean(
			NotificationSource notificationSource) {
		return notificationManager;
	}

	/**
	 * Convenience method to retrieve the default
	 * {@link AbstractUsageParameterSet}
	 * 
	 * @return
	 */
	public AbstractUsageParameterSet getDefaultInstalledUsageParameterSet() {
		if (defaultUsageMBean != null) {
			return defaultUsageMBean.getUsageParameter();
		} else {
			return null;
		}
	}

	/**
	 * Convenience method to retrieve the {@link AbstractUsageParameterSet} for
	 * the specified param set name.
	 * 
	 * @param name
	 * @return
	 */
	public AbstractUsageParameterSet getInstalledUsageParameterSet(String name) {

		if (name == null) {
			return getDefaultInstalledUsageParameterSet();
		} else {
			UsageMBeanImpl usageMBean = usageMBeans.get(name);
			if (usageMBean == null) {
				return null;
			} else {
				return usageMBean.getUsageParameter();
			}
		}
	}

	/*
	 * To be defined by concrete mbean, for a proper logging (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

}
