package org.mobicents.slee.container.management;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;
import javax.slee.ComponentID;
import javax.slee.CreateException;
import javax.slee.EventTypeID;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LinkNameAlreadyBoundException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.management.UnrecognizedResourceAdaptorException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MResourceAdaptorTypeRef;
import org.mobicents.slee.resource.ConfigPropertyDescriptor;
import org.mobicents.slee.resource.EventLookupFacilityImpl;
import org.mobicents.slee.resource.InstalledResourceAdaptor;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorBoostrapContext;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;
import org.mobicents.slee.resource.SleeEndpointImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
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
	 * the resource adaptor entities
	 */
	private ConcurrentHashMap<String, ResourceAdaptorEntity> resourceAdaptorEntities;

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
	 *      String, Properties)
	 * 
	 * @param id
	 * @param entityName
	 * @param properties
	 * @throws UnrecognizedResourceAdaptorException
	 * @throws ResourceAdaptorEntityAlreadyExistsException
	 * @throws ResourceException
	 */

	public void createResourceAdaptorEntity(ResourceAdaptorID id,
			String entityName, Properties properties)
	throws NullPointerException, InvalidArgumentException,
	UnrecognizedResourceAdaptorException,
	ResourceAdaptorEntityAlreadyExistsException, ResourceException {

		if (logger.isDebugEnabled()) {
			logger.debug("Creating RA Entity. Id: " + id + ", name: "
					+ entityName + ", properties: " + properties);
		}

		// TODO tx aware operation
		synchronized (sleeContainer.getManagementMonitor()) {
			final InstalledResourceAdaptor installedRA = resourceAdaptors.get(id);
			if (installedRA == null) {
				String msg = "Failed to create RA Entity. RA ID: " + id
				+ " not found.";
				logger.error(msg);
				throw new UnrecognizedResourceAdaptorException(msg);
			}

			if (this.resourceAdaptorEntities.get(entityName) != null) {
				String msg = "Failed to create RA Entity. Resource Adpator Entity Name: "
					+ entityName + " already exists! RA ID: " + id;
				logger.error(msg);
				throw new ResourceAdaptorEntityAlreadyExistsException(msg);
			}

			final BootstrapContext bootStrap = new ResourceAdaptorBoostrapContext(
					entityName, new SleeEndpointImpl(sleeContainer
							.getActivityContextFactory(), sleeContainer
							.getEventRouter(), sleeContainer, entityName),
							new EventLookupFacilityImpl(sleeContainer), sleeContainer
							.getAlarmFacility(), sleeContainer
							.getTransactionManager(), sleeContainer
							.getProfileFacility());

			ResourceAdaptorEntity raEntity = null;
			try {
				raEntity = new ResourceAdaptorEntity(entityName, installedRA,
						bootStrap, sleeContainer);
			} catch (CreateException e) {
				throw new ResourceException("failed to create ra entity", e);
			}
			this.resourceAdaptorEntities.put(entityName, raEntity);
			installedRA.getResourceAdaptorEntities().add(raEntity);

			// configure properties
			if (logger.isDebugEnabled()) {
				logger.debug(id + "RA PROPERTIES: " + properties);
			}
			try {
				raEntity.configure(properties);
			} catch (InvalidStateException e) {
				throw new ResourceException("failed to configure ra entity", e);
			}

			logger.info("Created RA Entity. Id: " + id + ", name: " + entityName
					+ ", properties: " + properties);
		}
	}

	/**
	 * @see ResourceManagementMBean#activateResourceAdaptorEntity(String)
	 * @param entityName
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws InvalidStateException
	 * @throws javax.slee.resource.ResourceException
	 */
	public void activateResourceAdaptorEntity(String entityName)
			throws UnrecognizedResourceAdaptorEntityException,
			InvalidStateException, javax.slee.resource.ResourceException {

		if (logger.isDebugEnabled()) {
			logger.debug("Activating RA Entity " + entityName);
		}

		// TODO tx aware operation
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
	 * 
	 * @param name
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws InvalidStateException
	 */
	public void deactivateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Deactivating RA Entity " + entityName);
		}

		// TODO tx aware operation
		synchronized (sleeContainer.getManagementMonitor()) {
			final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
			.get(entityName);
			if (raEntity == null) {
				throw new UnrecognizedResourceAdaptorEntityException(
						"Resource Adaptor Entity " + entityName + " not found.");
			} else {
				raEntity.deactivate();
				logger.info("Deactivated RA Entity " + entityName);
			}
		}
	}

	/**
	 * @see ResourceManagementMBean#removeResourceAdaptorEntity(String)
	 * 
	 * @param entityName
	 * @throws java.lang.NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws InvalidStateException
	 * @throws DependencyException
	 */
	public void removeResourceAdaptorEntity(String entityName)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			DependencyException {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing RA Entity " + entityName);
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
			raEntity.getInstalledResourceAdaptor().getResourceAdaptorEntities()
			.remove(raEntity);
			this.resourceAdaptorEntities.remove(entityName);
			logger.info("Removed RA Entity " + entityName);
		}
	}

	/**
	 * 
	 * @see ResourceManagementMBean#updateConfigurationProperties(String,
	 *      Properties)
	 * 
	 * @param name
	 * @param properties
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws InvalidStateException
	 * @throws ResourceException
	 */
	public void updateConfigurationProperties(String name, Properties properties)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			InvalidArgumentException, ResourceException {

		if (logger.isDebugEnabled()) {
			logger.debug("Updating RA Entity with properties: " + properties);
		}

		final ResourceAdaptorEntity raEntity = this.resourceAdaptorEntities
				.get(name);
		if (raEntity == null) {
			throw new UnrecognizedResourceAdaptorEntityException(
					"Resource Adaptor Entity " + name + " not found.");
		} else {
			raEntity.configure(properties);
			logger.info("Updated RA Entity with properties: " + properties);
		}
	}

	/**
	 * 
	 * @see ResourceManagementMBean#bindLinkName(String, String)
	 * 
	 * @param linkName
	 * @param entityName
	 * @throws NullPointerException
	 * @throws InvalidArgumentException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws LinkNameAlreadyBoundException
	 */
	public void bindLinkName(String linkName, String entityName)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorEntityException,
			LinkNameAlreadyBoundException {

		if (logger.isDebugEnabled()) {
			logger.debug("Binding link between RA Entity " + entityName
					+ " and Name " + linkName);
		}

		// TODO tx aware operation
		synchronized (sleeContainer.getManagementMonitor()) {
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

			this.resourceAdaptorEntityLinks.put(linkName, entityName);

			logger.info("Bound link between RA Entity " + entityName + " and Name "
					+ linkName);
		}

	}

	/**
	 * @see ResourceManagementMBean#unbindLinkName(String)
	 * @param linkName
	 * @throws NullPointerException
	 * @throws UnrecognizedLinkNameException
	 * @throws DependencyException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 */
	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException,
			UnrecognizedResourceAdaptorEntityException {

		if (logger.isDebugEnabled()) {
			logger.debug("Unbinding RA Entity Link " + linkName);
		}

		// TODO tx aware operation
		synchronized (sleeContainer.getManagementMonitor()) {
			if (linkName == null) {
				throw new NullPointerException("null link name");
			}

			final String entityName = this.resourceAdaptorEntityLinks.get(linkName);
			if (entityName == null) {
				throw new UnrecognizedLinkNameException(linkName);
			}

			final ResourceAdaptorEntity resourceAdaptorEntity = this.resourceAdaptorEntities
			.get(entityName);
			if (resourceAdaptorEntity == null) {
				throw new UnrecognizedResourceAdaptorEntityException(entityName);
			}

			final ResourceAdaptorID raID = resourceAdaptorEntity
			.getInstalledResourceAdaptor().getKey();
			ComponentManagement componentManagement = sleeContainer
			.getComponentManagement();
			ComponentID[] refComps = componentManagement
			.getReferringComponents(raID);
			for (int i = 0; i < refComps.length; i++) {
				if (componentManagement.getComponentDescriptor(refComps[i]) instanceof SbbDescriptor) {
					SbbDescriptor sbbDesc = (SbbDescriptor) componentManagement
					.getComponentDescriptor(refComps[i]);
					String[] sbbRALinks = sbbDesc.getResourceAdaptorEntityLinks();
					for (int j = 0; j < sbbRALinks.length; j++) {
						if (linkName.equals(sbbRALinks[j]))
							throw new DependencyException(sbbDesc.getID()
									+ " is referencing this RA link"
									+ " -- cannot remove it!");
					}
				}
			}

			this.resourceAdaptorEntityLinks.remove(linkName);
			logger.info("Unbound RA Entity Link " + linkName);
		}
	}

	/**
	 * 
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorEntityState)
	 * @param state
	 * @return
	 * @throws NullPointerException
	 */
	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA Entities with state " + state);
		}

		if (state == null)
			throw new NullPointerException("null entity state");

		HashSet<String> resultEntityNames = new HashSet<String>();
		for (ResourceAdaptorEntity resourceAdaptorEntity : resourceAdaptorEntities
				.values()) {
			if (resourceAdaptorEntity.getState().equals(state))
				resultEntityNames.add(resourceAdaptorEntity.getName());
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
	 * @return
	 */
	public String[] getLinkNames() {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA link names");
		}

		Set entityLinksSet = resourceAdaptorEntityLinks.keySet();
		String[] entityLinksArray = new String[entityLinksSet.size()];
		entityLinksArray = (String[]) entityLinksSet.toArray(entityLinksArray);
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA link names : " + entityLinksSet);
		}
		return entityLinksArray;

	}

	/**
	 * @see ResourceManagementMBean#getLinkNames(String)
	 * @param entityName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
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
	 * @see ResourceManagementMBean#getConfigurationProperties(ResourceAdaptorID)
	 * @param id
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorException
	 */
	public Properties getConfigurationProperties(ResourceAdaptorID id)
			throws NullPointerException, UnrecognizedResourceAdaptorException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting configuration properties for RA with id "
					+ id);
		}

		if (id == null)
			throw new NullPointerException("null resource adaptor id");

		final InstalledResourceAdaptor installedResourceAdaptor = this.resourceAdaptors
				.get(id);
		if (installedResourceAdaptor == null)
			throw new UnrecognizedResourceAdaptorException(
					"unrecognized resource adaptor " + id.toString());

		Properties properties = new Properties();
		Iterator configPropertyDescriptors = installedResourceAdaptor
				.getDescriptor().getConfigPropertyDescriptors().iterator();
		while (configPropertyDescriptors.hasNext()) {
			ConfigPropertyDescriptor configPropertyDescriptor = (ConfigPropertyDescriptor) configPropertyDescriptors
					.next();
			String value = configPropertyDescriptor.getValue().toString();
			properties.setProperty(configPropertyDescriptor.getName(), value);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Got configuration properties for RA with id");
		}
		return properties;

	}

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(String)
	 * @param entityName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws ResourceException
	 */
	public Properties getConfigurationProperties(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, ResourceException {

		if (logger.isDebugEnabled()) {
			logger
					.debug("Getting configuration properties for RA entity with name "
							+ entityName);
		}

		if (entityName == null)
			throw new NullPointerException("null entity name");

		final ResourceAdaptorEntity resourceAdaptorEntity = (ResourceAdaptorEntity) this.resourceAdaptorEntities
				.get(entityName);
		if (resourceAdaptorEntity == null)
			throw new UnrecognizedResourceAdaptorEntityException(entityName);

		Properties properties = new Properties();
		Iterator configPropertyDescriptors = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getDescriptor()
				.getConfigPropertyDescriptors().iterator();
		while (configPropertyDescriptors.hasNext()) {
			ConfigPropertyDescriptor configPropertyDescriptor = (ConfigPropertyDescriptor) configPropertyDescriptors
					.next();
			String value = resourceAdaptorEntity.getConfigProperty(
					configPropertyDescriptor).toString();
			properties.setProperty(configPropertyDescriptor.getName(), value);
		}
		if (logger.isDebugEnabled()) {
			logger
					.debug("Got configuration properties for RA entity with name "
							+ entityName);
		}
		return properties;

	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptor(String)
	 * @param entityName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorEntityException
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
		if (resourceAdaptorEntity == null)
			throw new UnrecognizedResourceAdaptorEntityException("Entity "
					+ entityName + " not found");

		final ResourceAdaptorID id = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getKey();
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA ID " + id + " for RA entity with name "
					+ entityName);
		}
		return id;
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities()
	 * @return
	 */
	public String[] getResourceAdaptorEntities() {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity names");
		}

		final Set<String> entityNamesSet = resourceAdaptorEntities.keySet();
		String[] entityNames = new String[entityNamesSet.size()];
		entityNames = entityNamesSet.toArray(entityNames);
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA entity names : " + entityNamesSet);
		}
		return entityNames;
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorID)
	 * @param resourceAdaptorID
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedResourceAdaptorException
	 */
	public String[] getResourceAdaptorEntities(
			ResourceAdaptorID resourceAdaptorID) throws NullPointerException,
			UnrecognizedResourceAdaptorException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity names for ra with ID "
					+ resourceAdaptorID);
		}

		if (resourceAdaptorID == null)
			throw new NullPointerException("null resource adaptor");

		final InstalledResourceAdaptor installedRA = (InstalledResourceAdaptor) this.resourceAdaptors
				.get(resourceAdaptorID);
		if (installedRA == null)
			throw new UnrecognizedResourceAdaptorException(resourceAdaptorID
					.toString());

		final HashSet<ResourceAdaptorEntity> resourceAdaptorEntitiesSet = installedRA
				.getResourceAdaptorEntities();
		String[] entityNames = new String[resourceAdaptorEntitiesSet.size()];
		Iterator<ResourceAdaptorEntity> it = resourceAdaptorEntitiesSet
				.iterator();
		for (int i = 0; i < entityNames.length; i++) {
			entityNames[i] = it.next().getName();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Got RA entity names : " + Arrays.asList(entityNames));
		}
		return entityNames;
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(String[])
	 * @param linkNames
	 * @return
	 * @throws NullPointerException
	 */
	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException {

		if (logger.isDebugEnabled()) {
			logger.debug("Getting RA entity name for link names : "
					+ Arrays.asList(linkNames));
		}

		if (linkNames == null)
			throw new NullPointerException("null link names");

		String[] resultEntityNames = new String[linkNames.length];
		for (int i = 0; i < linkNames.length; i++) {
			String entityName = resourceAdaptorEntityLinks.get(linkNames[i]);
			resultEntityNames[i] = entityName;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Got RA entity names : "
					+ Arrays.asList(resultEntityNames));
		}

		return resultEntityNames;
	}

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntity(String linkName)
	 * @param linkName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedLinkNameException
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

	// --- additonal operations

	/**
	 * Retrieves the {@link ResourceAdaptorEntity} with the specified entity
	 * name.
	 * 
	 * @throws UnrecognizedResourceAdaptorEntityException
	 *             if no such entity exists
	 */
	public ResourceAdaptorEntity getResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException {

		if (entityName == null)
			throw new NullPointerException("null entity name");

		final ResourceAdaptorEntity entity = (ResourceAdaptorEntity) this.resourceAdaptorEntities
				.get(entityName);
		if (entity == null)
			throw new UnrecognizedResourceAdaptorEntityException("Entity "
					+ entityName + " not found");

		return entity;
	}

	@Override
	public String toString() {
		return "Resource Management: " + "\n+-- Resource Adaptor Types: "
				+ resourceAdaptorTypes.keySet() + "\n+-- Resource Adaptor: "
				+ resourceAdaptors.keySet()
				+ "\n+-- Resource Adaptor Entities: "
				+ resourceAdaptorEntities.keySet()
				+ "\n+-- Resource Adaptor Entitiy Links: "
				+ resourceAdaptorEntities.keySet()
				+ "\n+-- Resource Adaptor ACI Factories: "
				+ activityContextInterfaceFactories.keySet();
	}

}
