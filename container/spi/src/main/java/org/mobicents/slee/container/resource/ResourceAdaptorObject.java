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

/**
 * 
 */
package org.mobicents.slee.container.resource;

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
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;

/**
 * @author martins
 *
 */
public interface ResourceAdaptorObject {

	/**
	 * Retrieves the current ra object configuration
	 * 
	 * @return
	 */
	public ConfigProperties getConfigProperties();

	/**
	 * Retrieves the current ra object state
	 * 
	 * @return
	 */
	public ResourceAdaptorObjectState getState();

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
			throws InvalidStateException;
	
	/**
	 * Sets the ft ra context. 
	 * 
	 * @param context
	 *            the context to provide to the ra object
	 * @throws IllegalArgumentException
	 *             if the ra object is not fault tolerant
	 */
	public void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext<Serializable, Serializable> context)
			throws IllegalArgumentException;
	
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
			throws InvalidConfigurationException;

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
			throws InvalidConfigurationException;

	/**
	 * Requests the activation of the ra object. If the operation succeeds the
	 * ra will transition to ACTIVE state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in INACTIVE state
	 */
	public void raActive() throws InvalidStateException;

	/**
	 * Requests the stopping of the ra object. If the operation succeeds the ra
	 * will transition to STOPPING state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in ACTIVE state
	 */
	public void raStopping() throws InvalidStateException;

	/**
	 * Requests the deactivation of the ra object. If the operation succeeds the
	 * ra will transition to INACTIVE state.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in STOPPING state
	 */
	public void raInactive() throws InvalidStateException;

	/**
	 * Unconfigures the ra object
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in INACTIVE state
	 */
	public void raUnconfigure() throws InvalidStateException;

	/**
	 * Unsets the context of the ra object.
	 * 
	 * @throws InvalidStateException
	 *             if the ra object is not in UNCONFIGURED state
	 */
	public void unsetResourceAdaptorContext() throws InvalidStateException;
	
	/**
	 * Unsets the ft context of the ra object.
	 * 
	 * @throws IllegalArgumentException
	 *             if the ra object is not in fault tolerant
	 */
	public void unsetFaultTolerantResourceAdaptorContext() throws IllegalArgumentException;
	
	/**
	 * @see ResourceAdaptor#getResourceAdaptorInterface(ResourceAdaptorTypeID)
	 */
	public Object getResourceAdaptorInterface(String className);

	/**
	 * @see ResourceAdaptor#getMarshaller()
	 */
	public Marshaler getMarshaler();

	/**
	 * @see ResourceAdaptor#serviceActive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceActive(ReceivableService serviceInfo);

	/**
	 * @see ResourceAdaptor#serviceStopping(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceStopping(ReceivableService serviceInfo);

	/**
	 * @see ResourceAdaptor#serviceInactive(ReceivableService)
	 * @param serviceInfo
	 */
	public void serviceInactive(ReceivableService serviceInfo);

	/**
	 * @see ResourceAdaptor#getActivityHandle(Object)
	 * @param activity
	 * @return null if the activity does not belongs to this ra object
	 */
	public ActivityHandle getActivityHandle(Object activity);
	
	/**
	 * @see ResourceAdaptor#getActivity(ActivityHandle)
	 * @param handle
	 */
	public Object getActivity(ActivityHandle handle);
	
	/**
	 * @see ResourceAdaptor#activityEnded(ActivityHandle)
	 * @param handle
	 */
	public void activityEnded(ActivityHandle handle);

	/**
	 * @see ResourceAdaptor#administrativeRemove(ActivityHandle)
	 * @param handle
	 */
    public void administrativeRemove(ActivityHandle handle);
    
    /**
     * @see ResourceAdaptor#activityUnreferenced(ActivityHandle)
     * @param handle
     */
	public void activityUnreferenced(ActivityHandle handle);
	
	/**
	 * @see ResourceAdaptor#queryLiveness(ActivityHandle)
	 * @param activityHandle
	 */
	public void queryLiveness(ActivityHandle activityHandle);
	
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
    public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason);
    
    /**
     * @see ResourceAdaptor#eventProcessingSuccessful(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     * @param handle
     * @param eventType
     * @param event
     * @param address
     * @param service
     * @param flags
     */
    public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags);

    /**
     * @see ResourceAdaptor#eventUnreferenced(ActivityHandle, FireableEventType, Object, Address, ReceivableService, int)
     * @param handle
     * @param eventType
     * @param event
     * @param address
     * @param service
     * @param flags
     */
    public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags);    

    public boolean isFaultTolerant();
    
    ResourceAdaptor getResourceAdaptorObject();
    
}
