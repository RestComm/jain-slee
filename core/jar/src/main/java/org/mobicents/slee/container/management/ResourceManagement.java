package org.mobicents.slee.container.management;

import java.lang.reflect.Constructor;
import java.util.Arrays;
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
import javax.slee.management.ResourceUsageMBean;
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
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorTypeBinding;
import org.mobicents.slee.container.deployment.ResourceAdaptorClassCodeGenerator;
import org.mobicents.slee.container.deployment.ResourceAdaptorTypeClassCodeGenerator;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * Manages deployed resource adaptor components.
 * 
 * @author Eduardo Martins
 * 
 */
public class ResourceManagement {

	private Logger logger = Logger.getLogger(ResourceManagement.class);

	/**
	 * the resource adaptor entities mapped by name
	 */
	private ConcurrentHashMap<String, ResourceAdaptorEntity> resourceAdaptorEntities;

	/**
	 * the set of resource adaptor entities aggregated per ra type, this is a runtime cache for optimal performance on ra type activity context factories
	 */
	private ConcurrentHashMap<ResourceAdaptorTypeID, Set<ResourceAdaptorEntity>> entitiesPerType = new ConcurrentHashMap<ResourceAdaptorTypeID, Set<ResourceAdaptorEntity>>();
	
	/**
	 * the resource adaptor entity name links
	 */
	private ConcurrentHashMap<String, String> resourceAdaptorEntityLinks;

	/**
	 * the container
	 */
	private SleeContainer sleeContainer;

	public ResourceManagement(SleeContainer sleeContainer) {
		resourceAdaptorEntities = new ConcurrentHashMap<String, ResourceAdaptorEntity>();
		resourceAdaptorEntityLinks = new ConcurrentHashMap<String, String>();
		this.sleeContainer = sleeContainer;
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

		synchronized (sleeContainer.getManagementMonitor()) {

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

			if (this.resourceAdaptorEntities.containsKey(entityName)) {
				throw new ResourceAdaptorEntityAlreadyExistsException(
						"Failed to create RA Entity. Resource Adpator Entity Name: "
								+ entityName + " already exists! RA ID: " + id);
			}
			
			TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceFacility().getTraceMBeanImpl();
			ResourceAdaptorEntityNotification notificationSource = new ResourceAdaptorEntityNotification(entityName);
			traceMBeanImpl.registerNotificationSource(notificationSource);
			ResourceAdaptorEntity raEntity =null;
			try { 
				raEntity = new ResourceAdaptorEntity(
						entityName, component, properties, sleeContainer,notificationSource);
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
			
			if (component.getUsageParametersInterface() != null) {
				// create resource usage mbean
				ResourceUsageMBeanImpl resourceUsageMBeanImpl = null;
				try {
					ObjectName objectName = new ObjectName(ResourceUsageMBean.BASE_OBJECT_NAME+','+ResourceUsageMBean.RESOURCE_ADAPTOR_ENTITY_NAME_KEY+'='+ObjectName.quote(entityName));
					resourceUsageMBeanImpl = new ResourceUsageMBeanImpl(entityName,component,sleeContainer);
					resourceUsageMBeanImpl.setObjectName(objectName);
					sleeContainer.getMBeanServer().registerMBean(resourceUsageMBeanImpl, objectName);
					raEntity.setResourceUsageMBean(resourceUsageMBeanImpl);
					// create default usage param set
					resourceUsageMBeanImpl.createUsageParameterSet();					
				} catch (Throwable e) {
					if (resourceUsageMBeanImpl != null) {
						resourceUsageMBeanImpl.remove();
					}
					for (ResourceAdaptorTypeID resourceAdaptorTypeID : component.getSpecsDescriptor().getResourceAdaptorTypes()) {
						entitiesPerType.get(resourceAdaptorTypeID).remove(raEntity);						
					}
					this.resourceAdaptorEntities.remove(raEntity);
					try {
						raEntity.remove();
					} catch (InvalidStateException e1) {
						logger.error(e.getMessage(),e);
					}
					throw new SLEEException("failed to create and register entity resource usage mbean",e);
				}
			}
						
			logger.info("Created Resource Adaptor Entity "+entityName+" for " + id+" Config Properties: " + properties);
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

		synchronized (sleeContainer.getManagementMonitor()) {
			final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
					.get(entityName);
			if (raEntity == null) {
				throw new UnrecognizedResourceAdaptorEntityException(
						"Resource Adaptor Entity " + entityName + " not found.");
			} else {
				raEntity.activate();
				logger.info("Activated RA Entity " + entityName);
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

		synchronized (sleeContainer.getManagementMonitor()) {
			final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
					.get(entityName);
			if (raEntity == null) {
				throw new UnrecognizedResourceAdaptorEntityException(
						"Resource Adaptor Entity " + entityName + " not found.");
			} else {
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
				}
				logger.info("Deactivated RA Entity " + entityName);
			}
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

		synchronized (sleeContainer.getManagementMonitor()) {
			final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
					.get(entityName);
			if (raEntity == null) {
				throw new UnrecognizedResourceAdaptorEntityException(
						"Resource Adaptor Entity " + entityName + " not found.");
			}

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
			
			ResourceUsageMBeanImpl resourceUsageMBeanImpl = raEntity.getResourceUsageMBean();
			if (resourceUsageMBeanImpl != null) {
				resourceUsageMBeanImpl.remove();													
			}
			
			logger.info("Removed RA Entity " + entityName);
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
			raEntity.updateConfigurationProperties(properties);
			logger.info("Updated RA Entity with properties: " + properties);
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

		synchronized (sleeContainer.getManagementMonitor()) {
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
	}

	/**
	 * @see ResourceManagementMBean#unbindLinkName(String)
	 */
	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException {

		if (logger.isDebugEnabled()) {
			logger.debug("Unbinding RA Entity Link " + linkName);
		}

		synchronized (sleeContainer.getManagementMonitor()) {
			SbbID[] boundSbbs = getBoundSbbs(linkName);
			if (boundSbbs.length > 0) {
				throw new DependencyException(linkName
						+ " link name is still referenced by sbbs "
						+ Arrays.asList(boundSbbs));
			} else {
				this.resourceAdaptorEntityLinks.remove(linkName);
				logger.info("Unbound RA Entity Link " + linkName);
			}
		}
	}

	/**
	 * 
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorEntityState)
	 */
	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA Entities with state " + state);
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
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA Entities with state " + state + " : "
					+ resultEntityNames);
		}
		return resultEntityNamesArray;
	}

	/**
	 * @see ResourceManagementMBean#getLinkNames()
	 */
	public String[] getLinkNames() {
		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA link names");
		}
		String[] linkNames = resourceAdaptorEntityLinks.keySet().toArray(
				new String[0]);
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA link names : " + Arrays.asList(linkNames));
		}
		return linkNames;

	}

	/**
	 * @see ResourceManagementMBean#getLinkNames(String)
	 */
	public String[] getLinkNames(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA link names for entity name " + entityName);
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

		if (logger.isDebugEnabled()) {
			logger.debug("Got RA link names for entity " + entityName + " : "
					+ linkNames);
		}
		return linkNamesArray;

	}

	/**
	 * @see ResourceManagementMBean#getBoundSbbs(String)
	 */
	public SbbID[] getBoundSbbs(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting sbbs bound to link name " + linkName);
		}

		if (linkName == null) {
			throw new NullPointerException("null link name");
		}

		synchronized (sleeContainer.getManagementMonitor()) {
			if (!this.resourceAdaptorEntityLinks.containsKey(linkName)) {
				throw new UnrecognizedLinkNameException(linkName);
			}
			final Set<SbbID> boundSbbsSet = new HashSet<SbbID>();
			final ComponentRepositoryImpl componentRepository = sleeContainer
					.getComponentRepositoryImpl();
			for (SbbID sbbID : componentRepository.getSbbIDs()) {
				SbbComponent sbbComponent = componentRepository
						.getComponentByID(sbbID);
				for (MResourceAdaptorTypeBinding raTypeBinding : sbbComponent
						.getDescriptor().getResourceAdaptorTypeBindings()) {
					for (MResourceAdaptorEntityBinding raEntityBinding : raTypeBinding
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
			if (logger.isDebugEnabled()) {
				logger.debug("Got sbbs bound to link name " + linkName + " : "
						+ boundSbbsSet);
			}
			return result;
		}
	}

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(ResourceAdaptorID)
	 */
	public ConfigProperties getConfigurationProperties(ResourceAdaptorID id)
			throws NullPointerException, UnrecognizedResourceAdaptorException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting configuration properties for RA with id "
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

		if (logger.isDebugEnabled()) {
			logger
					.debug("Getting configuration properties for RA entity with name "
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

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA ID for RA entity with name " + entityName);
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

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity names");
		}

		synchronized (sleeContainer.getManagementMonitor()) {
			return resourceAdaptorEntities.keySet().toArray(new String[0]);
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorID)
	 */
	public String[] getResourceAdaptorEntities(
			ResourceAdaptorID resourceAdaptorID) throws NullPointerException,
			UnrecognizedResourceAdaptorException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity names for ra with ID "
					+ resourceAdaptorID);
		}
		if (resourceAdaptorID == null) {
			throw new NullPointerException("null resource adaptor");
		}
		synchronized (sleeContainer.getManagementMonitor()) {
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
				if (logger.isDebugEnabled()) {
					logger.debug("Got RA entity names : "
							+ Arrays.asList(entityNames));
				}
				return entityNames;
			}
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(String[])
	 */
	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity name for link names : "
					+ Arrays.asList(linkNames));
		}

		if (linkNames == null)
			throw new NullPointerException("null link names");

		synchronized (sleeContainer.getManagementMonitor()) {
			String[] resultEntityNames = new String[linkNames.length];
			for (int i = 0; i < linkNames.length; i++) {
				String entityName = resourceAdaptorEntityLinks
						.get(linkNames[i]);
				resultEntityNames[i] = entityName;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Got RA entity names : "
						+ Arrays.asList(resultEntityNames));
			}

			return resultEntityNames;
		}
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntity(String linkName)
	 */
	public String getResourceAdaptorEntityName(String linkName)
			throws NullPointerException, UnrecognizedLinkNameException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity name for link name " + linkName);
		}

		if (linkName == null)
			throw new NullPointerException("null link name");

		final String entityName = resourceAdaptorEntityLinks.get(linkName);
		if (entityName == null)
			throw new UnrecognizedLinkNameException(linkName);

		if (logger.isDebugEnabled()) {
			logger.debug("Got RA entity name " + entityName + " for link name "
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

		if (logger.isDebugEnabled()) {
			logger.debug("Getting state for RA entity with name " + entityName);
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

		final ResourceAdaptorEntity resourceAdaptorEntity = this.resourceAdaptorEntities
				.get(entityName);
		
		if (resourceAdaptorEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(entityName);
		} else {
			ResourceUsageMBeanImpl mbean = resourceAdaptorEntity.getResourceUsageMBean();
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
	public ResourceAdaptorEntity getResourceAdaptorEntity(String entityName) {
		return this.resourceAdaptorEntities
				.get(entityName);
	}

	/**
	 * Retrieves the set of resource adaptor entities aggregated per ra type, this is a runtime cache for optimal performance on ra type activity context factories
	 * @return
	 */
	public ConcurrentHashMap<ResourceAdaptorTypeID, Set<ResourceAdaptorEntity>> getResourceAdaptorEntitiesPerType() {
		return entitiesPerType;
	}
	
	private ResourceAdaptorComponent getResourceAdaptorComponent(
			ResourceAdaptorID id) {
		return sleeContainer.getComponentRepositoryImpl().getComponentByID(id);
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
			+ "\n+-- Resource Adaptor Entities per RA Type size: "	+ entitiesPerType.size();
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
		getResourceAdaptorEntitiesPerType().put(
				resourceAdaptorTypeID,
				new HashSet<ResourceAdaptorEntity>());
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				getResourceAdaptorEntitiesPerType().remove(resourceAdaptorTypeID);
				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		
	}

	/**
	 * Installs the specified {@link ResourceAdaptorComponent} in the container
	 * @param component
	 * @throws DeploymentException 
	 */
	public void installResourceAdaptor(ResourceAdaptorComponent component) throws DeploymentException {
		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + component);

		}
		new ResourceAdaptorClassCodeGenerator().process(component);
	}

	/**
	 * Uninstalls the specified {@link ResourceAdaptorTypeComponent} from the container
	 * @param component
	 */
	public void uninstallResourceAdaptorType(
			ResourceAdaptorTypeComponent component) {
		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling " + component);

		}
		final ResourceAdaptorTypeID resourceAdaptorTypeID = component.getResourceAdaptorTypeID();
		final Set<ResourceAdaptorEntity> raEntities = getResourceAdaptorEntitiesPerType().remove(resourceAdaptorTypeID);
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				getResourceAdaptorEntitiesPerType().put(
						resourceAdaptorTypeID,
						raEntities);				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
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

}
