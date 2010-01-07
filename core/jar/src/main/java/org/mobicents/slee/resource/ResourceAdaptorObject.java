package org.mobicents.slee.resource;

import java.io.Serializable;

import javax.slee.Address;
import javax.slee.InvalidStateException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;

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
	private ConfigProperties configProperties;

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
			state = ResourceAdaptorObjectState.UNCONFIGURED;
			object.setResourceAdaptorContext(context);
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}
	/**
	 * Sets the ft ra context. 
	 * 
	 * @param context
	 *            the context to provide to the ra object
	 * @throws IllegalArgumentException
	 *             if the ra object is not fault tolerant
	 */
	public void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext<Serializable, Serializable> context)
			throws IllegalArgumentException {
		//FIXME: we dont check for state?
			if(isFaultTolerant())
			{
				((FaultTolerantResourceAdaptor<Serializable, Serializable>)this.object).setFaultTolerantResourceAdaptorContext(context);
			}else
			{
				throw new IllegalArgumentException("RA Object is not fault tolerant!");
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
		object.raConfigure(configProperties);
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
		object.raConfigurationUpdate(configProperties);
	}

	/**
	 * Merges the current properties values with the new ones and uses the ra to
	 * verify the configuration
	 * 
	 * @param newProperties
	 * @throws InvalidConfigurationException
	 *             if the configuration, after merging the specified properties
	 *             with the current properties values, results in an invalid
	 *             configuration
	 */
	private void verifyConfigProperties(ConfigProperties newProperties)
			throws InvalidConfigurationException {
		// merge properties
		for (ConfigProperties.Property configProperty : configProperties
				.getProperties()) {
			if (newProperties.getProperty(configProperty.getName()) == null) {
				newProperties.addProperty(configProperty);				
			}
		}
		// validate result
		for (ConfigProperties.Property entityProperty : newProperties
				.getProperties()) {
			if (entityProperty.getValue() == null) {
				throw new InvalidConfigurationException(
						"the property "
								+ entityProperty.getName()
								+ " has null value");
			}
		}
		// validate in ra object
		object.raVerifyConfiguration(newProperties);
		// ok, switch config
		configProperties = newProperties;
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
			state = ResourceAdaptorObjectState.ACTIVE;
			object.raActive();			
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
			state = ResourceAdaptorObjectState.STOPPING;
			object.raStopping();			
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
			state = ResourceAdaptorObjectState.INACTIVE;
			object.raInactive();			
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
			state = ResourceAdaptorObjectState.UNCONFIGURED;
			object.raUnconfigure();			
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
	 * Unsets the ft context of the ra object.
	 * 
	 * @throws IllegalArgumentException
	 *             if the ra object is not in fault tolerant
	 */
	public void unsetFaultTolerantResourceAdaptorContext() throws IllegalArgumentException {
		if(isFaultTolerant())
		{
			((FaultTolerantResourceAdaptor<Serializable, Serializable>)this.object).unsetFaultTolerantResourceAdaptorContext();
		}else
		{
			throw new IllegalArgumentException("RA Object is not fault tolerant!");
		}
	}
	/**
	 * @see ResourceAdaptor#getResourceAdaptorInterface(ResourceAdaptorTypeID)
	 */
	public Object getResourceAdaptorInterface(String className) {
		return object.getResourceAdaptorInterface(className);
	}

	/**
	 * @see ResourceAdaptor#getMarshaller()
	 */
	public Marshaler getMarshaler() {
		return object.getMarshaler();
	}

	/**
	 * @see ResourceAdaptor#serviceActive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceActive(ReceivableService serviceInfo) {
		object.serviceActive(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#serviceStopping(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceStopping(ReceivableService serviceInfo) {
		object.serviceStopping(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#serviceInactive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceInactive(ReceivableService serviceInfo) {
		object.serviceInactive(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#getActivityHandle(Object)
	 * @param activity
	 * @return null if the activity does not belongs to this ra object
	 */
	public ActivityHandle getActivityHandle(Object activity) {
		return object.getActivityHandle(activity);
	}
	
	/**
	 * @see ResourceAdaptor#getActivity(ActivityHandle)
	 * @param handle
	 */
	public Object getActivity(ActivityHandle handle) {
		return object.getActivity(handle);
	}
	
	/**
	 * @see ResourceAdaptor#activityEnded(ActivityHandle)
	 * @param handle
	 */
	public void activityEnded(ActivityHandle handle) {
		object.activityEnded(handle);
	}

	/**
	 * @see ResourceAdaptor#administrativeRemove(ActivityHandle)
	 * @param handle
	 */
    public void administrativeRemove(ActivityHandle handle) {
    	object.administrativeRemove(handle);
    }
    
    /**
     * @see ResourceAdaptor#activityUnreferenced(ActivityHandle)
     * @param handle
     */
	public void activityUnreferenced(ActivityHandle handle) {
		object.activityUnreferenced(handle);
	}
	
	/**
	 * @see ResourceAdaptor#queryLiveness(ActivityHandle)
	 * @param activityHandle
	 */
	public void queryLiveness(ActivityHandle activityHandle) {
		object.queryLiveness(activityHandle);		
	}
	
	/**
	 * @see ResourceAdaptor#eventProcessingFailed(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int, FailureReason)
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param service
	 * @param flags
	 * @param reason
	 */
    public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {
		object.eventProcessingFailed(handle, eventType, event, address, service, flags, reason);
	}
    
    /**
     * @see ResourceAdaptor#eventProcessingSuccessful(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     * @param handle
     * @param eventType
     * @param event
     * @param address
     * @param service
     * @param flags
     */
    public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    	if (this.state == ResourceAdaptorObjectState.ACTIVE || this.state == ResourceAdaptorObjectState.STOPPING) {
    		object.eventProcessingSuccessful(handle, eventType, event, address, service, flags);
    	}
    }

    /**
     * @see ResourceAdaptor#eventUnreferenced(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     * @param handle
     * @param eventType
     * @param event
     * @param address
     * @param service
     * @param flags
     */
    public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    	if (this.state == ResourceAdaptorObjectState.ACTIVE || this.state == ResourceAdaptorObjectState.STOPPING) {
    		object.eventUnreferenced(handle, eventType, event, address, service, flags);
    	}
    }

    public boolean isFaultTolerant()
    {
    	return (this.object instanceof FaultTolerantResourceAdaptor);
    }
    ResourceAdaptor getResourceAdaptorObject()
    {
    	return object;
    }
}
