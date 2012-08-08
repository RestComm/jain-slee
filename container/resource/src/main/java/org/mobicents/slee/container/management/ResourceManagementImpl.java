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

package org.mobicents.slee.container.management;

import java.io.ObjectStreamException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LinkNameAlreadyBoundException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.management.UnrecognizedResourceAdaptorException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.classloading.ReplicationClassLoader;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorEntityBindingDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.resource.ResourceAdaptorObjectState;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.resource.ActivityHandleReferenceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntityImpl;
import org.mobicents.slee.resource.deployment.ResourceAdaptorClassCodeGenerator;
import org.mobicents.slee.resource.deployment.ResourceAdaptorTypeClassCodeGenerator;

/**
 * 
 * Manages deployed resource adaptor components.
 * 
 * @author Eduardo Martins
 * 
 */
public final class ResourceManagementImpl extends AbstractSleeContainerModule implements ResourceManagement {

	private static final Logger logger = Logger.getLogger(ResourceManagement.class);

	/**
	 * the resource adaptor entities mapped by name
	 */
	private final ConcurrentHashMap<String, ResourceAdaptorEntityImpl> resourceAdaptorEntities;

	/**
	 * the set of resource adaptor entities aggregated per ra type, this is a runtime cache for optimal performance on ra type activity context factories
	 */
	private final ConcurrentHashMap<ResourceAdaptorTypeID, Set<ResourceAdaptorEntity>> entitiesPerType = new ConcurrentHashMap<ResourceAdaptorTypeID, Set<ResourceAdaptorEntity>>();
	
	/**
	 * the resource adaptor entity name links
	 */
	private final ConcurrentHashMap<String, String> resourceAdaptorEntityLinks;

	/**
	 * singleton
	 */
	private static final ResourceManagementImpl INSTANCE = new ResourceManagementImpl();
	
	private ActivityHandleReferenceFactory handleReferenceFactory;
	
	public static ResourceManagementImpl getInstance(){
		return INSTANCE;
	}
	
	private ResourceManagementImpl() {
		super();
		resourceAdaptorEntities = new ConcurrentHashMap<String, ResourceAdaptorEntityImpl>();
		resourceAdaptorEntityLinks = new ConcurrentHashMap<String, String>();
	}
	
	private Object readResolve() throws ObjectStreamException {
		// fools serialization
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	// --- MBEAN methods

	/**
	 * @see ResourceManagementMBean#createResourceAdaptorEntity(ResourceAdaptorID,
	 *      String, ConfigProperties)
	 */
	public void createResourceAdaptorEntity(ResourceAdaptorID id,
			String entityName, ConfigProperties properties)
	throws NullPointerException, InvalidArgumentException,
	UnrecognizedResourceAdaptorException,
	ResourceAdaptorEntityAlreadyExistsException,
	InvalidConfigurationException {

		if (logger.isDebugEnabled()) {
			logger.debug("Creating RA Entity. Id: " + id + ", name: "
					+ entityName + ", properties: " + properties);
		}

		if (id == null) {
			throw new NullPointerException("null ra id");
		}

		if (properties == null) {
			throw new NullPointerException("null config properties");
		}

		/*
		 * javax.slee.InvalidArgumentExceptionThis exception is thrown if
		 * the resource adaptor entity name argument is not a valid. A
		 * resource adaptor entity name cannot be null or zero-length, and
		 * may only contain letter or digit characters as defined by
		 * java.lang.Character.isLetterOrDigit. Additionally, any other
		 * character in the Unicode range 0x0020-0x007e may be included in a
		 * resource adaptor entity name.
		 */
		if (entityName == null) {
			throw new InvalidArgumentException("entityName is null");
		}
		if (entityName.length() == 0) {
			throw new InvalidArgumentException("entityName is zero length");
		}
		validateNewEntityOrLinkName(entityName);

		ResourceAdaptorComponent component = getResourceAdaptorComponent(id);
		if (component == null) {
			throw new UnrecognizedResourceAdaptorException(
					"Failed to create RA Entity. RA ID: " + id
					+ " not found.");
		}
		else {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(component.getClassLoader());
			try {
				if (this.resourceAdaptorEntities.containsKey(entityName)) {
					throw new ResourceAdaptorEntityAlreadyExistsException(
							"Failed to create RA Entity. Resource Adpator Entity Name: "
							+ entityName + " already exists! RA ID: " + id);
				}

				TraceManagement traceMBeanImpl = sleeContainer.getTraceManagement();
				ResourceAdaptorEntityNotification notificationSource = new ResourceAdaptorEntityNotification(entityName);
				traceMBeanImpl.registerNotificationSource(notificationSource);
				
				// create resource usage mbean
				ResourceUsageMBean usageMBean = null;
				if (component.getUsageParametersInterface() != null) {
					try {
						usageMBean = sleeContainer.getUsageParametersManagement().newResourceUsageMBean(entityName, component);
					} catch (Throwable e) {
						if (usageMBean != null) {
							usageMBean.remove();
						}
						throw new SLEEException("failed to create and register entity resource usage mbean",e);
					}
				}
				
				ResourceAdaptorEntityImpl raEntity =null;
				try { 
					raEntity = new ResourceAdaptorEntityImpl(
							entityName, component, properties, this,notificationSource, usageMBean);
				}
				catch (InvalidConfigurationException e) {
					traceMBeanImpl.deregisterNotificationSource(notificationSource);
					throw e;
				}
				catch (InvalidArgumentException e) {
					traceMBeanImpl.deregisterNotificationSource(notificationSource);
					throw e;
				}
				catch (SLEEException e) {
					traceMBeanImpl.deregisterNotificationSource(notificationSource);
					throw e;
				}
				catch (Throwable e) {
					traceMBeanImpl.deregisterNotificationSource(notificationSource);
					throw new SLEEException(e.getMessage(),e);
				}

				for (ResourceAdaptorTypeID resourceAdaptorTypeID : component.getSpecsDescriptor().getResourceAdaptorTypes()) {
					Set<ResourceAdaptorEntity> set = entitiesPerType.get(resourceAdaptorTypeID);
					if (set == null) {
						throw new SLEEException("there is no set of ra entities for "+resourceAdaptorTypeID); 
					}
					else {
						set.add(raEntity);
					}
				}
				this.resourceAdaptorEntities.put(entityName, raEntity);

				logger.info("Created Resource Adaptor Entity "+entityName+" for " + id+" Config Properties: " + properties);
			}
			finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
		}


	}

	/**
	 * @see ResourceManagementMBean#activateResourceAdaptorEntity(String)
	 */
	public void activateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Activating RA Entity " + entityName);
		}

		if (entityName == null) {
			throw new NullPointerException("null entity name");
		}

		final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
		.get(entityName);
		if (raEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(
					"Resource Adaptor Entity " + entityName + " not found.");
		} else {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(raEntity.getComponent().getClassLoader());
			try {
				raEntity.activate();
				logger.info("Activated RA Entity " + entityName);
			}
			finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
		}
	}

	/**
	 * @see ResourceManagementMBean#deactivateResourceAdaptorEntity(String)
	 */
	public void deactivateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Deactivating RA Entity " + entityName);
		}

		if (entityName == null) {
			throw new NullPointerException("null entity name");
		}

		final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
		.get(entityName);
		if (raEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(
					"Resource Adaptor Entity " + entityName + " not found.");
		} else {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(raEntity.getComponent().getClassLoader());
			boolean rollback = true;
			try {
				sleeContainer.getTransactionManager().begin();
				raEntity.deactivate();
				rollback = false;
			} catch (NotSupportedException e) {
				throw new SLEEException(e.getMessage(),e);
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			} catch (TransactionRequiredLocalException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			finally {
				try {
					if (rollback) {
						sleeContainer.getTransactionManager().rollback();
					}
					else {
						sleeContainer.getTransactionManager().commit();
					}
				}
				catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
				finally {
					Thread.currentThread().setContextClassLoader(classLoader);
				}
			}
			logger.info("Deactivated RA Entity " + entityName);
		}
		
	}

	/**
	 * @see ResourceManagementMBean#removeResourceAdaptorEntity(String)
	 */
	public void removeResourceAdaptorEntity(String entityName)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			DependencyException {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing RA Entity " + entityName);
		}

		if (entityName == null) {
			throw new NullPointerException("null entity name");
		}

		final ResourceAdaptorEntityImpl raEntity = this.resourceAdaptorEntities
		.get(entityName);
		if (raEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(
					"Resource Adaptor Entity " + entityName + " not found.");
		}
		else {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(raEntity.getComponent().getClassLoader());
			try {
				if (this.resourceAdaptorEntityLinks.containsValue(entityName)) {
					throw new DependencyException("entity name has link(s)");
				}

				raEntity.remove();

				this.resourceAdaptorEntities.remove(entityName);

				for (ResourceAdaptorTypeID resourceAdaptorTypeID : raEntity.getComponent().getSpecsDescriptor().getResourceAdaptorTypes()) {
					Set<ResourceAdaptorEntity> set = entitiesPerType.get(resourceAdaptorTypeID);
					if (set == null) {
						throw new SLEEException("there is no set of ra entities for "+resourceAdaptorTypeID); 
					}
					else {
						set.remove(raEntity);
					}
				}

				ResourceUsageMBean resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
				if (resourceUsageMBeanImpl != null) {
					resourceUsageMBeanImpl.remove();													
				}

				logger.info("Removed RA Entity " + entityName);
			}
			finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}

		}
	}

	/**
	 * 
	 * @see ResourceManagementMBean#updateConfigurationProperties(String,
	 *      ConfigProperties)
	 */
	public void updateConfigurationProperties(String entityName,
			ConfigProperties properties) throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			InvalidConfigurationException {

		if (logger.isDebugEnabled()) {
			logger.debug("Updating RA Entity with properties: " + properties);
		}

		if (entityName == null) {
			throw new NullPointerException("null entity name");
		}

		if (properties == null) {
			throw new NullPointerException("null config properties");
		}

		final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
				.get(entityName);
		if (raEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(
					"Resource Adaptor Entity " + entityName + " not found.");
		} else {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(raEntity.getComponent().getClassLoader());
			try {
				raEntity.updateConfigurationProperties(properties);
				logger.info("Updated RA Entity with properties: " + properties);
			}
			finally {
				Thread.currentThread().setContextClassLoader(classLoader);
			}
		}
	}

	/**
	 * @see ResourceManagementMBean#bindLinkName(String, String)
	 */
	public void bindLinkName(String linkName, String entityName)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorEntityException,
			LinkNameAlreadyBoundException {

		if (logger.isDebugEnabled()) {
			logger.debug("Binding link between RA Entity " + entityName
					+ " and Name " + linkName);
		}
		if (linkName == null) {
			throw new NullPointerException("null link name");
		}
		if (entityName == null) {
			throw new NullPointerException("null entity name");
		}

		if (this.resourceAdaptorEntityLinks.containsKey(linkName)) {
			throw new LinkNameAlreadyBoundException(linkName);
		}
		if (!this.resourceAdaptorEntities.containsKey(entityName)) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		}
		validateNewEntityOrLinkName(linkName);
		this.resourceAdaptorEntityLinks.put(linkName, entityName);
		logger.info("Bound link between RA Entity " + entityName
				+ " and Name " + linkName);

	}

	/**
	 * @see ResourceManagementMBean#unbindLinkName(String)
	 */
	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException {

		if (logger.isDebugEnabled()) {
			logger.debug("Unbinding RA Entity Link " + linkName);
		}

		if (sleeContainer.getServiceManagement().isRAEntityLinkNameReferenced(linkName)) {
			throw new DependencyException(linkName + " link name is still used by sbbs");
		} else {
			this.resourceAdaptorEntityLinks.remove(linkName);
			logger.info("Unbound RA Entity Link " + linkName);
		}

	}

	/**
	 * 
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorEntityState)
	 */
	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA Entities with state " + state);
		}
		if (state == null) {
			throw new NullPointerException("null entity state");
		}
		HashSet<String> resultEntityNames = new HashSet<String>();
		for (ResourceAdaptorEntity resourceAdaptorEntity : resourceAdaptorEntities
				.values()) {
			if (resourceAdaptorEntity.getState() == state) {
				resultEntityNames.add(resourceAdaptorEntity.getName());
			}
		}
		String[] resultEntityNamesArray = new String[resultEntityNames.size()];
		resultEntityNamesArray = resultEntityNames
				.toArray(resultEntityNamesArray);
		if (logger.isTraceEnabled()) {
			logger.trace("Got RA Entities with state " + state + " : "
					+ resultEntityNames);
		}
		return resultEntityNamesArray;
	}

	/**
	 * @see ResourceManagementMBean#getLinkNames()
	 */
	public String[] getLinkNames() {
		
		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA link names");
		}
		String[] linkNames = getLinkNamesSet().toArray(
				new String[0]);
		if (logger.isTraceEnabled()) {
			logger.trace("Got RA link names : " + Arrays.asList(linkNames));
		}
		return linkNames;

	}

	/**
	 * Retrieves a copy of the current set of ra entity links
	 * @return
	 */
	public Set<String> getLinkNamesSet() {
		return Collections.unmodifiableSet(resourceAdaptorEntityLinks.keySet());
	}
	
	/**
	 * @see ResourceManagementMBean#getLinkNames(String)
	 */
	public String[] getLinkNames(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA link names for entity name " + entityName);
		}

		if (entityName == null)
			throw new NullPointerException("null entity name");

		if (!this.resourceAdaptorEntities.containsKey(entityName)) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		}

		HashSet<String> linkNames = new HashSet<String>();
		for (String linkName : resourceAdaptorEntityLinks.keySet()) {
			if (resourceAdaptorEntityLinks.get(linkName).equals(entityName)) {
				linkNames.add(linkName);
			}
		}

		String[] linkNamesArray = new String[linkNames.size()];
		linkNamesArray = linkNames.toArray(linkNamesArray);

		if (logger.isTraceEnabled()) {
			logger.trace("Got RA link names for entity " + entityName + " : "
					+ linkNames);
		}
		return linkNamesArray;

	}

	/**
	 * @see ResourceManagementMBean#getBoundSbbs(String)
	 */
	public SbbID[] getBoundSbbs(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting sbbs bound to link name " + linkName);
		}

		if (linkName == null) {
			throw new NullPointerException("null link name");
		}

		if (!this.resourceAdaptorEntityLinks.containsKey(linkName)) {
			throw new UnrecognizedLinkNameException(linkName);
		}
		final Set<SbbID> boundSbbsSet = new HashSet<SbbID>();
		final ComponentRepository componentRepository = sleeContainer
		.getComponentRepository();
		for (SbbID sbbID : componentRepository.getSbbIDs()) {
			SbbComponent sbbComponent = componentRepository
			.getComponentByID(sbbID);
			for (ResourceAdaptorTypeBindingDescriptor raTypeBinding : sbbComponent
					.getDescriptor().getResourceAdaptorTypeBindings()) {
				for (ResourceAdaptorEntityBindingDescriptor raEntityBinding : raTypeBinding
						.getResourceAdaptorEntityBinding()) {
					if (raEntityBinding.getResourceAdaptorEntityLink()
							.equals(linkName)) {
						boundSbbsSet.add(sbbID);
					}
				}
			}
		}
		SbbID[] result = boundSbbsSet
		.toArray(new SbbID[boundSbbsSet.size()]);
		if (logger.isTraceEnabled()) {
			logger.trace("Got sbbs bound to link name " + linkName + " : "
					+ boundSbbsSet);
		}
		return result;

	}

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(ResourceAdaptorID)
	 */
	public ConfigProperties getConfigurationProperties(ResourceAdaptorID id)
			throws NullPointerException, UnrecognizedResourceAdaptorException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting configuration properties for RA with id "
					+ id);
		}

		if (id == null)
			throw new NullPointerException("null resource adaptor id");

		ResourceAdaptorComponent component = getResourceAdaptorComponent(id);
		if (component == null) {
			throw new UnrecognizedResourceAdaptorException(
					"unrecognized resource adaptor " + id.toString());
		} else {
			return component.getDefaultConfigPropertiesInstance();
		}
	}

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(String)
	 */
	public ConfigProperties getConfigurationProperties(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting configuration properties for RA entity with name "
							+ entityName);
		}

		if (entityName == null)
			throw new NullPointerException("null entity name");

		final ResourceAdaptorEntity resourceAdaptorEntity = this.resourceAdaptorEntities
				.get(entityName);
		if (resourceAdaptorEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		} else {
			return resourceAdaptorEntity.getConfigurationProperties();
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptor(String)
	 */
	public ResourceAdaptorID getResourceAdaptor(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA ID for RA entity with name " + entityName);
		}

		if (entityName == null)
			throw new NullPointerException("null entity name");

		ResourceAdaptorEntity resourceAdaptorEntity = (ResourceAdaptorEntity) resourceAdaptorEntities
				.get(entityName);
		if (resourceAdaptorEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException("Entity "
					+ entityName + " not found");
		} else {
			return resourceAdaptorEntity.getResourceAdaptorID();
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities()
	 */
	public String[] getResourceAdaptorEntities() {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA entity names");
		}

		return resourceAdaptorEntities.keySet().toArray(new String[0]);
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorID)
	 */
	public String[] getResourceAdaptorEntities(
			ResourceAdaptorID resourceAdaptorID) throws NullPointerException,
			UnrecognizedResourceAdaptorException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA entity names for ra with ID "
					+ resourceAdaptorID);
		}
		if (resourceAdaptorID == null) {
			throw new NullPointerException("null resource adaptor");
		}
		
		ResourceAdaptorComponent component = getResourceAdaptorComponent(resourceAdaptorID);
		if (component == null) {
			throw new UnrecognizedResourceAdaptorException(
					resourceAdaptorID.toString());
		} else {
			Set<String> entityNameSet = new HashSet<String>();
			for (ResourceAdaptorEntity raEntity : resourceAdaptorEntities.values()) {
				if (raEntity.getResourceAdaptorID().equals(resourceAdaptorID)) {
					entityNameSet.add(raEntity.getName());
				}
			}
			String[] entityNames = entityNameSet
			.toArray(new String[entityNameSet.size()]);
			if (logger.isTraceEnabled()) {
				logger.trace("Got RA entity names : "
						+ Arrays.asList(entityNames));
			}
			return entityNames;
		}

	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(String[])
	 */
	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA entity name for link names : "
					+ Arrays.asList(linkNames));
		}

		if (linkNames == null)
			throw new NullPointerException("null link names");


		String[] resultEntityNames = new String[linkNames.length];
		for (int i = 0; i < linkNames.length; i++) {
			String entityName = resourceAdaptorEntityLinks
			.get(linkNames[i]);
			resultEntityNames[i] = entityName;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Got RA entity names : "
					+ Arrays.asList(resultEntityNames));
		}

		return resultEntityNames;

	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntity(String linkName)
	 */
	public String getResourceAdaptorEntityName(String linkName)
			throws NullPointerException, UnrecognizedLinkNameException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting RA entity name for link name " + linkName);
		}

		if (linkName == null)
			throw new NullPointerException("null link name");

		final String entityName = resourceAdaptorEntityLinks.get(linkName);
		if (entityName == null)
			throw new UnrecognizedLinkNameException(linkName);

		if (logger.isTraceEnabled()) {
			logger.trace("Got RA entity name " + entityName + " for link name "
					+ linkName);
		}

		return entityName;
	}

	/**
	 * @see ResourceManagementMBean#getState(String)
	 */
	public ResourceAdaptorEntityState getState(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isTraceEnabled()) {
			logger.trace("Getting state for RA entity with name " + entityName);
		}

		if (entityName == null)
			throw new NullPointerException("null entity name");

		final ResourceAdaptorEntity resourceAdaptorEntity = this.resourceAdaptorEntities
				.get(entityName);
		if (resourceAdaptorEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		} else {
			return resourceAdaptorEntity.getState();
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceUsageMBean(String)
	 */
	public ObjectName getResourceUsageMBean(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException,
			InvalidArgumentException {
		
		if (entityName == null)
			throw new NullPointerException("null entity name");

		final ResourceAdaptorEntityImpl resourceAdaptorEntity = this.resourceAdaptorEntities
				.get(entityName);
		
		if (resourceAdaptorEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		} else {
			ResourceUsageMBean mbean = resourceAdaptorEntity.getResourceUsageMBean();
			if (mbean == null) {
				throw new InvalidArgumentException(" Resource Adaptor entity "+entityName+" related resource adaptor does not defines a usage paramters interface. See section 14.2 in SLEE 1.1 specs for more info");
			}
			else {
				// ensure it is open
				mbean.open();
				// return its object name
				return mbean.getObjectName();
			}			
		}
	}

	// --- additonal operations

	/**
	 * Retrieves the {@link ResourceAdaptorEntity} with the specified entity
	 * name.
	 * 
	 */
	public ResourceAdaptorEntityImpl getResourceAdaptorEntity(String entityName) {
		return this.resourceAdaptorEntities
				.get(entityName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ResourceManagement#getResourceAdaptorEntitiesPerType(javax.slee.resource.ResourceAdaptorTypeID)
	 */
	public Set<ResourceAdaptorEntity> getResourceAdaptorEntitiesPerType(ResourceAdaptorTypeID resourceAdaptorTypeID) {
		return entitiesPerType.get(resourceAdaptorTypeID);
	}
	
	private ResourceAdaptorComponent getResourceAdaptorComponent(
			ResourceAdaptorID id) {
		return sleeContainer.getComponentRepository().getComponentByID(id);
	}

	private void validateNewEntityOrLinkName(String name)
			throws InvalidArgumentException {
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				if (c < '\u0020' || c > '\u007e') {
					throw new InvalidArgumentException(
							name
									+ " includes char "
									+ c
									+ " ,which is not a letter or digit or in unicode range 0x0020-0x007e");
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Resource Management: " 
			+ "\n+-- Resource Adaptor Entities: " + resourceAdaptorEntities.keySet()
			+ "\n+-- Resource Adaptor Entity Links: " + resourceAdaptorEntityLinks.keySet()
			+ "\n+-- Resource Adaptor Entities per RA Type size: "	+ entitiesPerType.size()
			+ "\n+-- Activity Handle Reference Factory: "	+ handleReferenceFactory;

	}

	/**
	 * Installs the specified {@link ResourceAdaptorTypeComponent} in the container
	 * @param component
	 * @throws DeploymentException 
	 */
	public void installResourceAdaptorType(
			ResourceAdaptorTypeComponent component) throws DeploymentException {
		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);

		}
		// generate code for aci factory
		new ResourceAdaptorTypeClassCodeGenerator().process(component);
		// create instance of aci factory and store it in the
		// component
		if (component.getActivityContextInterfaceFactoryConcreteClass() != null) {
			try {
				Constructor<?> constructor = component
						.getActivityContextInterfaceFactoryConcreteClass()
						.getConstructor(
								new Class[] { SleeContainer.class,
										ResourceAdaptorTypeID.class });
				Object aciFactory = constructor.newInstance(new Object[] {
						sleeContainer, component.getResourceAdaptorTypeID() });
				component.setActivityContextInterfaceFactory(aciFactory);
			} catch (Throwable e) {
				throw new SLEEException(
						"unable to create ra type aci factory instance", e);
			}
		}
		final ResourceAdaptorTypeID resourceAdaptorTypeID = component.getResourceAdaptorTypeID();
		entitiesPerType.put(
				resourceAdaptorTypeID,
				new HashSet<ResourceAdaptorEntity>());
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				entitiesPerType.remove(resourceAdaptorTypeID);
				
			}
		};
		sleeContainer.getTransactionManager().getTransactionContext().getAfterRollbackActions().add(action);				
	}

	/**
	 * Installs the specified {@link ResourceAdaptorComponent} in the container
	 * @param component
	 * @throws DeploymentException 
	 */
	public void installResourceAdaptor(final ResourceAdaptorComponent component) throws DeploymentException {
		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);

		}
		new ResourceAdaptorClassCodeGenerator().process(component);
		// if we are in cluster mode we need to add the RA class loader domain to the replication class loader
		if (!sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			final ReplicationClassLoader replicationClassLoader = sleeContainer.getReplicationClassLoader();
			replicationClassLoader.addDomain(component.getClassLoaderDomain());
			final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
			if (txContext != null) {
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						replicationClassLoader.removeDomain(component.getClassLoaderDomain());
					}
				};
				txContext.getAfterRollbackActions().add(action);
			}
		}
	}

	/**
	 * Uninstalls the specified {@link ResourceAdaptorTypeComponent} from the container
	 * @param component
	 */
	public void uninstallResourceAdaptorType(final ResourceAdaptorTypeComponent component) {
		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling " + component);

		}
		final ResourceAdaptorTypeID resourceAdaptorTypeID = component.getResourceAdaptorTypeID();
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		TransactionalAction action1 = new TransactionalAction() {
			public void execute() {
				entitiesPerType.remove(resourceAdaptorTypeID);
			}
		};
		if (txContext != null) {
			txContext.getAfterCommitActions().add(action1);
		}
		else {
			action1.execute();
		}		
		
		// if we are in cluster mode we need to remove the RA class loader domain from the replication class loader
		if (!sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			final ReplicationClassLoader replicationClassLoader = sleeContainer.getReplicationClassLoader();
			TransactionalAction action2 = new TransactionalAction() {
				public void execute() {
					replicationClassLoader.removeDomain(component.getClassLoaderDomain());
				}
			};
			if (txContext != null) {
				txContext.getAfterCommitActions().add(action2);
			}
			else {
				action2.execute();
			}
		}
		
	}

	/**
	 * Uninstalls the specified {@link ResourceAdaptorComponent} from the container
	 * @param component
	 * @throws DependencyException 
	 */
	public void uninstallResourceAdaptor(ResourceAdaptorComponent component) throws DependencyException {
		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling " + component);

		}
		for (ResourceAdaptorEntity raEntity : resourceAdaptorEntities.values()) {
			if (raEntity.getResourceAdaptorID().equals(component.getResourceAdaptorID())) {
				throw new DependencyException("can't uninstall "+component.getResourceAdaptorID()+" since ra entity "+raEntity.getName()+" refers it");
			}
		}
	}
	
	@Override
	public void sleeInitialization() {
		if (!sleeContainer.getCluster().getMobicentsCache().isLocalMode()) {
			handleReferenceFactory = new ActivityHandleReferenceFactory(this);
		}		
	}
	
	@Override
	public void sleeStarting() {
		if (handleReferenceFactory != null) {
			handleReferenceFactory.init();
		}			
	}
	
	@Override
	public void sleeRunning() {
		final Thread currentThread = Thread.currentThread();
		final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
		for (ResourceAdaptorEntity raEntity : resourceAdaptorEntities.values()) {
			try {
				currentThread.setContextClassLoader(raEntity.getComponent().getClassLoader());
				raEntity.sleeRunning();
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}
		currentThread.setContextClassLoader(currentThreadClassLoader);
	}
	
	@Override
	public void sleeStopping() {
		
		logger.info("Stopping all active resource adaptors ...");
		
		// inform all ra entities that we are stopping the container
		final Thread currentThread = Thread.currentThread();
		final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
		for (ResourceAdaptorEntity raEntity : resourceAdaptorEntities.values()) {
			try {
				currentThread.setContextClassLoader(raEntity.getComponent().getClassLoader());
				raEntity.sleeStopping();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}			
		}
		currentThread.setContextClassLoader(currentThreadClassLoader);
		
		// wait till all ra entity objects are stopped
		boolean loop;
		boolean sleepInLastLoop = false;
		do {
			loop = false;
			for (ResourceAdaptorEntity raEntity : resourceAdaptorEntities.values()) {
				try {
					if (raEntity.getResourceAdaptorObject()
							.getState() == ResourceAdaptorObjectState.STOPPING) {
						logger.info("Waiting for ra entity "+raEntity.getName()+" to stop...");
						loop = true;
						sleepInLastLoop = true;
					}
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.debug(e.getMessage(), e);
					}
				}
			}
			if (loop || sleepInLastLoop) {
				try {
					// wait a sec
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		} while (loop);
		if (handleReferenceFactory != null) {
			handleReferenceFactory.remove();
		}
	}

	/**
	 * 
	 * @return
	 */
	public ActivityHandleReferenceFactory getHandleReferenceFactory() {
		return handleReferenceFactory;
	}
}
