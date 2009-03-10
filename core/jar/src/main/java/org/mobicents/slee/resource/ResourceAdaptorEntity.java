package org.mobicents.slee.resource;

import java.lang.reflect.Constructor;

import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.management.NotificationSource;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.SleeState;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBeanImpl;

/**
 * 
 * Implementation of the logical Resource Adaptor Entity and its life cycle.
 * 
 * @author Eduardo Martins
 */
public class ResourceAdaptorEntity {

	private static final Logger logger = Logger
			.getLogger(ResourceAdaptorEntity.class);

	/**
	 * the ra entity name
	 */
	private final String name;

	/**
	 * the ra component related to this entity
	 */
	private final ResourceAdaptorComponent component;

	/**
	 * the ra entity state
	 */
	private ResourceAdaptorEntityState state;

	/**
	 * the ra object
	 */
	private final ResourceAdaptorObject object;

	/**
	 * the slee container
	 */
	private SleeContainer sleeContainer;

	/**
	 * Notification source of this RA Entity
	 */
	private ResourceAdaptorEntityNotification notificationSource;

	/**
	 * the resource usage mbean for this ra, may be null
	 */
	private ResourceUsageMBeanImpl usageMbean;
	
	/**
	 * Creates a new entity with the specified name, for the specified ra
	 * component and with the provided entity config properties. The entity
	 * creation is complete after instantianting the ra object, and then setting
	 * its ra context and configuration.
	 * 
	 * @param name
	 * @param component
	 * @param entityProperties
	 * @param sleeContainer
	 * @throws InvalidConfigurationException
	 * @throws InvalidArgumentException
	 */
	public ResourceAdaptorEntity(String name,
			ResourceAdaptorComponent component,
			ConfigProperties entityProperties, SleeContainer sleeContainer,ResourceAdaptorEntityNotification notificationSource)
			throws InvalidConfigurationException, InvalidArgumentException {
		this.name = name;
		this.component = component;
		this.sleeContainer = sleeContainer;
		this.notificationSource = notificationSource;
		// create ra object
		try {
			Constructor cons = this.component.getResourceAdaptorClass()
					.getConstructor(null);
			ResourceAdaptor ra = (ResourceAdaptor) cons.newInstance(null);
			object = new ResourceAdaptorObject(ra, component
					.getDefaultConfigPropertiesInstance());
		} catch (Exception e) {
			throw new SLEEException(
					"unable to create instance of ra object for " + component);
		}
		// set ra context and configure it
		try {
			object.setResourceAdaptorContext(new ResourceAdaptorContextImpl(
					this, sleeContainer));
		} catch (InvalidStateException e) {
			logger
					.error(
							"should not happen, setting ra context on ra entity creation",
							e);
			throw new SLEEException(e.getMessage(), e);
		}
		object.raConfigure(entityProperties);
		// process to inactive state
		this.state = ResourceAdaptorEntityState.INACTIVE;
		// register the ra entity notification source in the trace facility
		this.sleeContainer.getTraceFacility().registerNotificationSource(this.getNotificationSource());
	}

	/**
	 * Retrieves ra component related to this entity
	 * 
	 * @return
	 */
	public ResourceAdaptorComponent getComponent() {
		return component;
	}

	/**
	 * Retrieves the ra entity name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the ra entity state
	 * 
	 * @return
	 */
	public ResourceAdaptorEntityState getState() {
		return state;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ResourceAdaptorEntity) obj).name.equals(this.name);
		} else {
			return false;
		}
	}

	// --------- ra entity/object logic

	/**
	 * Updates the ra entity config properties
	 */
	public void updateConfigurationProperties(ConfigProperties properties)
			throws InvalidConfigurationException, InvalidStateException {
		if (!component.getDescriptor().getSupportsActiveReconfiguration()
				.booleanValue()
				&& (sleeContainer.getSleeState() == SleeState.RUNNING
						&& sleeContainer.getSleeState() == SleeState.STOPPING && sleeContainer
						.getSleeState() == SleeState.STARTING)
				&& (state == ResourceAdaptorEntityState.ACTIVE || state == ResourceAdaptorEntityState.STOPPING)) {
			throw new InvalidStateException(
					"the value of the supports-active-reconfiguration attribute of the resource-adaptor-class element in the deployment descriptor of the Resource Adaptor of the resource adaptor entity is False and the resource adaptor entity is in the Active or Stopping state and the SLEE is in the Starting, Running, or Stopping state");
		} else {
			object.raConfigurationUpdate(properties);
		}
	}

	/**
	 * Signals that the container is in RUNNING state
	 */
	public void sleeRunning() throws InvalidStateException {
		// if entity is active then activate the ra object
		if (this.state.isActive()) {
			object.raActive();
		}
	}

	/**
	 * Signals that the container is in STOPPING state
	 */
	public void sleeStopping() throws InvalidStateException {
		if (this.state.isActive()) {
			object.raStopping();
		}
	}

	/**
	 * Activates the ra entity
	 * 
	 * @throws InvalidStateException
	 *             if the entity is not in INACTIVE state
	 */
	public void activate() throws InvalidStateException {
		if (!this.state.isInactive()) {
			throw new InvalidStateException("entity " + name + " is in state: "
					+ this.state);
		}
		this.state = ResourceAdaptorEntityState.ACTIVE;
		// if slee is running then activate ra object
		if (sleeContainer.getSleeState().isRunning()) {
			object.raActive();
		}
	}

	/**
	 * Deactivates the ra entity
	 * 
	 * @throws InvalidStateException
	 *             if the entity is not in ACTIVE state
	 */
	public void deactivate() throws InvalidStateException {
		if (!this.state.isActive()) {
			throw new InvalidStateException("entity " + name + " is in state: "
					+ this.state);
		}
		object.raStopping();
		this.state = ResourceAdaptorEntityState.STOPPING;
	}

	/**
	 * Signals that all activities, belonging to ra objects of the entity, have
	 * ended. If the entity is in STOPPING state it will change state to
	 * INACTIVE
	 */
	public void allActivitiesEnded() {
		if (this.state.isStopping()) {
			try {
				object.raInactive();
			} catch (InvalidStateException e) {
				logger.error("bug? failed to complete ra entity " + name
						+ " deactivation, the ra object is in wrong state", e);
			}
			this.state = ResourceAdaptorEntityState.INACTIVE;
		}
	}

	/**
	 * Removes the entity, it will unconfigure and unset the ra context, the
	 * entity object can not be reused
	 * 
	 * @throws InvalidStateException
	 */
	public void remove() throws InvalidStateException {
		if (!this.state.isInactive()) {
			throw new InvalidStateException("entity " + name + " is in state: "
					+ this.state);
		}
		object.raUnconfigure();
		object.unsetResourceAdaptorContext();
		this.sleeContainer.getTraceFacility().deregisterNotificationSource(this.getNotificationSource());
		state = null;
	}
	
	/**
	 * Retrieves the active config properties for the entity
	 * 
	 * @return
	 */
	public ConfigProperties getConfigurationProperties() {
		return object.getConfigProperties();
	}

	/**
	 * Retrieves the id of the resource adaptor for this entity
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID() {
		return component.getResourceAdaptorID();
	}

	/**
	 * Retrieves the ra object
	 * @return
	 */
	public ResourceAdaptorObject getResourceAdaptorObject() {
		return object;
	}
	
	/**
	 * Retrieves the ra interface for this entity and the specified ra type
	 * 
	 * @param raType
	 * @return
	 */
	public Object getResourceAdaptorInterface(ResourceAdaptorTypeID raType) {
		return object.getResourceAdaptorInterface(sleeContainer
				.getComponentRepositoryImpl().getComponentByID(raType)
				.getDescriptor().getResourceAdaptorInterface()
				.getResourceAdaptorInterfaceName());
	}

	/**
	 * Retrieves the marshaller from the ra object, if exists
	 * @return
	 */
	public Marshaler getMarshaler() {
		return object.getMarshaler();
	}
	
	/**
	 * Indicates a service was activated, the entity will forward this
	 * notification to the ra object.
	 * 
	 * @param serviceInfo
	 */
	public void serviceActive(ReceivableService serviceInfo) {
		try {
			object.serviceActive(serviceInfo);
		} catch (Throwable e) {
			logger.warn("invocation resulted in unchecked exception", e);
		}
	}

	/**
	 * Indicates a service is stopping, the entity will forward this
	 * notification to the ra object.
	 * 
	 * @param serviceInfo
	 */
	public void serviceStopping(ReceivableService serviceInfo) {
		try {
			object.serviceStopping(serviceInfo);
		} catch (Throwable e) {
			logger.warn("invocation resulted in unchecked exception", e);
		}
	}

	/**
	 * Indicates a service was deactivated, the entity will forward this
	 * notification to the ra object.
	 * 
	 * @param serviceInfo
	 */
	public void serviceInactive(ReceivableService serviceInfo) {
		try {
			object.serviceInactive(serviceInfo);
		} catch (Throwable e) {
			logger.warn("invocation resulted in unchecked exception", e);
		}
	}
	
	/**
	 * Return Notification source representing this RA Entity
	 * @return
	 */
	public NotificationSource getNotificationSource()
	{
		return this.notificationSource;
	}

	/**
	 * Retrieves the resource usage mbean for this ra, may be null
	 * @return
	 */
	public ResourceUsageMBeanImpl getResourceUsageMBean() {
		return usageMbean;
	}
	
	/**
	 * Sets the resource usage mbean for this ra, may be null
	 */
	public void setResourceUsageMBean(ResourceUsageMBeanImpl usageMbean) {
		this.usageMbean = usageMbean;
	}
}
