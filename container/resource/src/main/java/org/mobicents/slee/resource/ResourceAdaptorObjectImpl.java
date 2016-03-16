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

import org.apache.log4j.Logger;
import org.mobicents.slee.container.resource.ResourceAdaptorObject;
import org.mobicents.slee.container.resource.ResourceAdaptorObjectState;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;

/**
 * A wrapper for an ra object, managing its state and configuration
 * 
 * @author Eduardo Martins
 * 
 */
public class ResourceAdaptorObjectImpl implements ResourceAdaptorObject {

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

	private final ResourceAdaptorEntityImpl raEntity;

	private MarshallerWrapper marshaler;

	private final static Logger logger = Logger
			.getLogger(ResourceAdaptorObjectImpl.class);
	private static boolean doTraceLogs = logger.isTraceEnabled();

	/**
	 * Creates a new instance, for the specified ra object and with the
	 * specified configuration properties.
	 * 
	 * @param raAdaptorEntity
	 * @param object
	 * @param configProperties
	 */
	public ResourceAdaptorObjectImpl(ResourceAdaptorEntityImpl raEntity,
			ResourceAdaptor object, ConfigProperties configProperties) {
		this.raEntity = raEntity;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.resource.ResourceAdaptorObject#
	 * setResourceAdaptorContext(javax.slee.resource.ResourceAdaptorContext)
	 */
	public void setResourceAdaptorContext(ResourceAdaptorContext context)
			throws InvalidStateException {

		if (doTraceLogs) {
			logger.trace("setResourceAdaptorContext( context = " + context
					+ " )");
		}

		if (state == null) {
			state = ResourceAdaptorObjectState.UNCONFIGURED;
			object.setResourceAdaptorContext(context);
		} else {
			throw new InvalidStateException("ra object is in state " + state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.resource.ResourceAdaptorObject#
	 * setFaultTolerantResourceAdaptorContext
	 * (org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext)
	 */
	@SuppressWarnings("unchecked")
	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<Serializable, Serializable> context)
			throws IllegalArgumentException {

		if (doTraceLogs) {
			logger.trace("setFaultTolerantResourceAdaptorContext( context = "
					+ context + " )");
		}

		if (isFaultTolerant()) {
			((FaultTolerantResourceAdaptor<Serializable, Serializable>) this.object)
					.setFaultTolerantResourceAdaptorContext(context);
		} else {
			throw new IllegalArgumentException(
					"RA Object is not fault tolerant!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.resource.ResourceAdaptorObject#raConfigure
	 * (javax.slee.resource.ConfigProperties)
	 */
	public void raConfigure(ConfigProperties properties)
			throws InvalidConfigurationException {

		if (doTraceLogs) {
			logger.trace("raConfigure( properties = " + properties + " )");
		}

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

		if (doTraceLogs) {
			logger.trace("raConfigurationUpdate( properties = " + properties
					+ " )");
		}

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

		if (doTraceLogs) {
			logger.trace("verifyConfigProperties( newProperties = "
					+ newProperties + " )");
		}

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
				throw new InvalidConfigurationException("the property "
						+ entityProperty.getName() + " has null value");
			}
		}
		// validate in ra object
		object.raVerifyConfiguration(newProperties);
		// ok, switch config
		configProperties = newProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.resource.ResourceAdaptorObject#raActive()
	 */
	public void raActive() throws InvalidStateException {

		if (doTraceLogs) {
			logger.trace("raActive()");
		}

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

		if (doTraceLogs) {
			logger.trace("raStopping()");
		}

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

		if (doTraceLogs) {
			logger.trace("raInactive()");
		}

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

		if (doTraceLogs) {
			logger.trace("raUnconfigure()");
		}

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

		if (doTraceLogs) {
			logger.trace("unsetResourceAdaptorContext()");
		}

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
	@SuppressWarnings("unchecked")
	public void unsetFaultTolerantResourceAdaptorContext()
			throws IllegalArgumentException {

		if (doTraceLogs) {
			logger.trace("unsetFaultTolerantResourceAdaptorContext()");
		}

		if (isFaultTolerant()) {
			((FaultTolerantResourceAdaptor<Serializable, Serializable>) this.object)
					.unsetFaultTolerantResourceAdaptorContext();
		} else {
			throw new IllegalArgumentException(
					"RA Object is not fault tolerant!");
		}
	}

	/**
	 * @see ResourceAdaptor#getResourceAdaptorInterface(ResourceAdaptorTypeID)
	 */
	public Object getResourceAdaptorInterface(String className) {

		if (doTraceLogs) {
			logger.trace("getResourceAdaptorInterface( className = "
					+ className + " )");
		}

		return object.getResourceAdaptorInterface(className);
	}

	/**
	 * @see ResourceAdaptor#getMarshaller()
	 */
	public Marshaler getMarshaler() {

		if (doTraceLogs) {
			logger.trace("getMarshaler()");
		}

		if (marshaler == null) {
			Marshaler realMarshaler = object.getMarshaler();
			marshaler = realMarshaler == null ? null : new MarshallerWrapper(
					realMarshaler, raEntity);
		}

		return marshaler;
	}

	/**
	 * @see ResourceAdaptor#serviceActive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceActive(ReceivableService serviceInfo) {

		if (doTraceLogs) {
			logger.trace("serviceActive( serviceInfo = " + serviceInfo + " )");
		}

		object.serviceActive(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#serviceStopping(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceStopping(ReceivableService serviceInfo) {

		if (doTraceLogs) {
			logger.trace("serviceStopping( serviceInfo = " + serviceInfo + " )");
		}

		object.serviceStopping(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#serviceInactive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceInactive(ReceivableService serviceInfo) {

		if (doTraceLogs) {
			logger.trace("serviceInactive( serviceInfo = " + serviceInfo + " )");
		}

		object.serviceInactive(serviceInfo);
	}

	/**
	 * @see ResourceAdaptor#getActivityHandle(Object)
	 * @param activity
	 * @return null if the activity does not belongs to this ra object
	 */
	public ActivityHandle getActivityHandle(Object activity) {

		if (doTraceLogs) {
			logger.trace("getActivityHandle( activity = " + activity + " )");
		}

		ActivityHandle activityHandle = object.getActivityHandle(activity);
		if (raEntity.getHandleReferenceFactory() != null
				&& activityHandle != null) {
			ActivityHandle reference = raEntity.getHandleReferenceFactory()
					.getReferenceTransacted(activityHandle);
			if (reference != null) {
				activityHandle = reference;
			}
		}
		return activityHandle;
	}

	/**
	 * @see ResourceAdaptor#getActivity(ActivityHandle)
	 * @param handle
	 */
	public Object getActivity(ActivityHandle handle) {

		if (doTraceLogs) {
			logger.trace("getActivity( handle = " + handle + " )");
		}

		final ActivityHandle origHandle = raEntity
				.derreferActivityHandle(handle);
		return origHandle != null ? object.getActivity(origHandle) : null;
	}

	/**
	 * @see ResourceAdaptor#activityEnded(ActivityHandle)
	 * @param handle
	 */
	public void activityEnded(ActivityHandle handle) {

		if (doTraceLogs) {
			logger.trace("activityEnded( handle = " + handle + " )");
		}

		object.activityEnded(handle);
	}

	/**
	 * @see ResourceAdaptor#administrativeRemove(ActivityHandle)
	 * @param handle
	 */
	public void administrativeRemove(ActivityHandle handle) {

		if (doTraceLogs) {
			logger.trace("administrativeRemove( handle = " + handle + " )");
		}

		object.administrativeRemove(raEntity.derreferActivityHandle(handle));
	}

	/**
	 * @see ResourceAdaptor#activityUnreferenced(ActivityHandle)
	 * @param handle
	 */
	public void activityUnreferenced(ActivityHandle handle) {

		if (doTraceLogs) {
			logger.trace("activityUnreferenced( handle = " + handle + " )");
		}

		object.activityUnreferenced(raEntity.derreferActivityHandle(handle));
	}

	/**
	 * @see ResourceAdaptor#queryLiveness(ActivityHandle)
	 * @param handle
	 */
	public void queryLiveness(ActivityHandle handle) {

		if (doTraceLogs) {
			logger.trace("queryLiveness( handle = " + handle + " )");
		}

		object.queryLiveness(raEntity.derreferActivityHandle(handle));
	}

	/**
	 * @see ResourceAdaptor#eventProcessingFailed(ActivityHandle,
	 *      FireableEventType, Object, Address, ReceivableService, int,
	 *      FailureReason)
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param service
	 * @param flags
	 * @param reason
	 */
	public void eventProcessingFailed(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags, FailureReason reason) {

		if (doTraceLogs) {
			logger.trace("eventProcessingFailed( handle = " + handle
					+ " , eventType = " + eventType + " , event = " + event
					+ " , address = " + address + " , service = " + service
					+ " , flags = " + flags + " , reason = " + reason + " )");
		}

		object.eventProcessingFailed(handle, eventType, event, address,
				service, flags, reason);
	}

	/**
	 * @see ResourceAdaptor#eventProcessingSuccessful(ActivityHandle,
	 *      FireableEventType, Object, Address, ReceivableService, int)
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param service
	 * @param flags
	 */
	public void eventProcessingSuccessful(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags) {

		if (doTraceLogs) {
			logger.trace("eventProcessingSuccessful( handle = " + handle
					+ " , eventType = " + eventType + " , event = " + event
					+ " , address = " + address + " , service = " + service
					+ " , flags = " + flags + " )");
		}

		if (this.state == ResourceAdaptorObjectState.ACTIVE
				|| this.state == ResourceAdaptorObjectState.STOPPING) {
			object.eventProcessingSuccessful(handle, eventType, event, address,
					service, flags);
		}
	}

	/**
	 * @see ResourceAdaptor#eventUnreferenced(ActivityHandle, FireableEventType,
	 *      Object, Address, ReceivableService, int)
	 * @param handle
	 * @param eventType
	 * @param event
	 * @param address
	 * @param service
	 * @param flags
	 */
	public void eventUnreferenced(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags) {

		if (doTraceLogs) {
			logger.trace("eventUnreferenced( handle = " + handle
					+ " , eventType = " + eventType + " , event = " + event
					+ " , address = " + address + " , service = " + service
					+ " , flags = " + flags + " )");
		}

		if (this.state == ResourceAdaptorObjectState.ACTIVE
				|| this.state == ResourceAdaptorObjectState.STOPPING) {
			object.eventUnreferenced(handle, eventType, event, address,
					service, flags);
		}
	}

	public boolean isFaultTolerant() {
		return this.object instanceof FaultTolerantResourceAdaptor;
	}

	public ResourceAdaptor getResourceAdaptorObject() {
		return object;
	}
}
