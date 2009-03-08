package org.mobicents.slee.resource;

import javax.slee.InvalidStateException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

/**
 * A wrapper for an ra object, managing its state and configuration
 * 
 * @author Eduardo Martins
 * 
 */
public class ResourceAdaptorObject {

	/**
	 * the ra object
	 */
	private final ResourceAdaptor object;

	/**
	 * the state of the ra object
	 */
	private ResourceAdaptorObjectState state = null;

	/**
	 * the properties of the ra object/entity
	 */
	private final ConfigProperties configProperties;

	/**
	 * Creates a new instance, for the specified ra object and with the
	 * specified configuration properties
	 * 
	 * @param object
	 * @param configProperties
	 */
	public ResourceAdaptorObject(ResourceAdaptor object,
			ConfigProperties configProperties) {
		this.object = object;
		this.configProperties = configProperties;
	}

	/**
	 * Retrieves the current ra object configuration
	 * 
	 * @return
	 */
	public ConfigProperties getConfigProperties() {
		return configProperties;
	}

	/**
	 * Retrieves the current ra object state
	 * 
	 * @return
	 */
	public ResourceAdaptorObjectState getState() {
		return state;
	}

	// OPERATIONS ON RA OBJECT

	/**
	 * Sets the ra context. If the operation succeeds the ra will transition to
	 * UNCONFIGURED state.
	 * 
	 * @param context
	 *            the context to provide to the ra object
	 * @throws InvalidStateException
	 *             if the ra object is not in null state
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext context)
			throws InvalidStateException {
		if (state == null) {
			object.setResourceAdaptorContext(context);
			state = ResourceAdaptorObjectState.UNCONFIGURED;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * Configures the ra.
	 * 
	 * @param properties
	 * @throws InvalidConfigurationException
	 *             if the configuration, after merging the specified properties
	 *             with the current properties values, results in an invalid
	 *             configuration
	 */
	public void raConfigure(ConfigProperties properties)
			throws InvalidConfigurationException {
		verifyConfigProperties(properties);
		object.raConfigure(properties);
		if (state == ResourceAdaptorObjectState.UNCONFIGURED) {
			state = ResourceAdaptorObjectState.INACTIVE;
		}
	}

	/**
	 * Updates the ra configuration.
	 * 
	 * @param properties
	 * @throws InvalidConfigurationException
	 *             if the configuration, after merging the specified properties
	 *             with the current properties values, results in an invalid
	 *             configuration
	 */
	public void raConfigurationUpdate(ConfigProperties properties)
			throws InvalidConfigurationException {
		verifyConfigProperties(properties);
		object.raConfigurationUpdate(properties);
	}

	/**
	 * merges the current properties values with the new ones and uses the ra to
	 * verify the configuration
	 * 
	 * @param entityProperties
	 * @throws InvalidConfigurationException
	 *             if the configuration, after merging the specified properties
	 *             with the current properties values, results in an invalid
	 *             configuration
	 */
	private void verifyConfigProperties(ConfigProperties entityProperties)
			throws InvalidConfigurationException {
		// merge and check properties values
		for (ConfigProperties.Property entityProperty : entityProperties
				.getProperties()) {
			if (entityProperty.getValue() == null) {
				throw new InvalidConfigurationException(
						"the specified property " + entityProperty.getName()
								+ " has null value");
			} else {
				ConfigProperties.Property configProperty = configProperties
						.getProperty(entityProperty.getName());
				if (configProperty == null) {
					throw new InvalidConfigurationException(
							"the specified property "
									+ entityProperty.getName()
									+ " is not defined in the resource adaptor");
				} else {
					configProperty.setValue(entityProperty.getValue());
				}
			}
		}
		for (ConfigProperties.Property configProperty : configProperties
				.getProperties()) {
			if (configProperty.getValue() == null) {
				throw new InvalidConfigurationException(
						"the property "
								+ configProperty.getName()
								+ " value is not defined after merging default/active configuration with provided properties");
			}
		}
		object.raVerifyConfiguration(configProperties);
	}

	/**
	 * Requests the activation of the ra object. If the operation succeeds the
	 * ra will transition to ACTIVE state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in INACTIVE state
	 */
	public void raActive() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.INACTIVE) {
			object.raActive();
			state = ResourceAdaptorObjectState.ACTIVE;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * Requests the stopping of the ra object. If the operation succeeds the ra
	 * will transition to STOPPING state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in ACTIVE state
	 */
	public void raStopping() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.ACTIVE) {
			object.raStopping();
			state = ResourceAdaptorObjectState.STOPPING;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * Requests the deactivation of the ra object. If the operation succeeds the
	 * ra will transition to INACTIVE state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in STOPPING state
	 */
	public void raInactive() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.STOPPING) {
			object.raInactive();
			state = ResourceAdaptorObjectState.INACTIVE;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * Unconfigures the ra object
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in INACTIVE state
	 */
	public void raUnconfigure() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.INACTIVE) {
			object.raUnconfigure();
			state = ResourceAdaptorObjectState.UNCONFIGURED;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * Unsets the context of the ra object.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in UNCONFIGURED state
	 */
	public void unsetResourceAdaptorContext() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.UNCONFIGURED) {
			object.unsetResourceAdaptorContext();
			state = null;
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/**
	 * @see ResourceAdaptorEntity#getResourceAdaptorInterface(ResourceAdaptorTypeID)
	 */
	public Object getResourceAdaptorInterface(String className) {
		return object.getResourceAdaptorInterface(className);
	}

	/**
	 * @see ResourceAdaptorEntity#getMarshaller()
	 */
	public Marshaler getMarshaler() {
		return object.getMarshaler();
	}

	/**
	 * @see ResourceAdaptorEntity#serviceActive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceActive(ReceivableService serviceInfo) {
		object.serviceActive(serviceInfo);
	}

	/**
	 * @see ResourceAdaptorEntity#serviceStopping(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceStopping(ReceivableService serviceInfo) {
		object.serviceStopping(serviceInfo);
	}

	/**
	 * @see ResourceAdaptorEntity#serviceInactive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceInactive(ReceivableService serviceInfo) {
		object.serviceInactive(serviceInfo);
	}
}
