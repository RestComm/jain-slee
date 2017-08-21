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
package org.mobicents.slee.container.management;

import java.util.Set;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SbbID;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LinkNameAlreadyBoundException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.management.UnrecognizedResourceAdaptorException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * @author martins
 * 
 */
public interface ResourceManagement extends SleeContainerModule {

	/**
	 * @see ResourceManagementMBean#activateResourceAdaptorEntity(String)
	 */
	public void activateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException;

	/**
	 * @see ResourceManagementMBean#bindLinkName(String, String)
	 */
	public void bindLinkName(String linkName, String entityName)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorEntityException,
			LinkNameAlreadyBoundException;

	/**
	 * @see ResourceManagementMBean#createResourceAdaptorEntity(ResourceAdaptorID,
	 *      String, ConfigProperties)
	 */
	public void createResourceAdaptorEntity(ResourceAdaptorID id,
			String entityName, ConfigProperties properties)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedResourceAdaptorException,
			ResourceAdaptorEntityAlreadyExistsException,
			InvalidConfigurationException;

	/**
	 * @see ResourceManagementMBean#deactivateResourceAdaptorEntity(String)
	 */
	public void deactivateResourceAdaptorEntity(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException;

	/**
	 * @see ResourceManagementMBean#getBoundSbbs(String)
	 */
	public SbbID[] getBoundSbbs(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException;

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(ResourceAdaptorID)
	 */
	public ConfigProperties getConfigurationProperties(ResourceAdaptorID id)
			throws NullPointerException, UnrecognizedResourceAdaptorException;

	/**
	 * @see ResourceManagementMBean#getConfigurationProperties(String)
	 */
	public ConfigProperties getConfigurationProperties(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException;

	/**
	 * @see ResourceManagementMBean#getLinkNames()
	 */
	public String[] getLinkNames();

	/**
	 * @see ResourceManagementMBean#getLinkNames(String)
	 */
	public String[] getLinkNames(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException;

	/**
	 * Retrieves a copy of the current set of ra entity links
	 * 
	 * @return
	 */
	public Set<String> getLinkNamesSet();

	/**
	 * @see ResourceManagementMBean#getResourceAdaptor(String)
	 */
	public ResourceAdaptorID getResourceAdaptor(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException;

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities()
	 */
	public String[] getResourceAdaptorEntities();

	/**
	 * 
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorEntityState)
	 */
	public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
			throws NullPointerException;

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(ResourceAdaptorID)
	 */
	public String[] getResourceAdaptorEntities(
			ResourceAdaptorID resourceAdaptorID) throws NullPointerException,
			UnrecognizedResourceAdaptorException;

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntities(String[])
	 */
	public String[] getResourceAdaptorEntities(String[] linkNames)
			throws NullPointerException;

	/**
	 * Retrieves the set of resource adaptor entities aggregated per ra type,
	 * this is a runtime cache for optimal performance on ra type activity
	 * context factories
	 * 
	 * @param resourceAdaptorTypeID
	 * @return
	 */
	public Set<ResourceAdaptorEntity> getResourceAdaptorEntitiesPerType(
			ResourceAdaptorTypeID resourceAdaptorTypeID);

	/**
	 * Retrieves the {@link ResourceAdaptorEntity} with the specified entity
	 * name.
	 * 
	 */
	public ResourceAdaptorEntity getResourceAdaptorEntity(String entityName);

	/**
	 * @see ResourceManagementMBean#getResourceAdaptorEntity(String linkName)
	 */
	public String getResourceAdaptorEntityName(String linkName)
			throws NullPointerException, UnrecognizedLinkNameException;

	/**
	 * @see ResourceManagementMBean#getResourceUsageMBean(String)
	 */
	public ObjectName getResourceUsageMBean(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException,
			InvalidArgumentException;

	/**
	 * @see ResourceManagementMBean#getState(String)
	 */
	public ResourceAdaptorEntityState getState(String entityName)
			throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException;

	/**
	 * Installs the specified {@link ResourceAdaptorComponent} in the container
	 * 
	 * @param component
	 * @throws DeploymentException
	 */
	public void installResourceAdaptor(ResourceAdaptorComponent component)
			throws DeploymentException;

	/**
	 * Installs the specified {@link ResourceAdaptorTypeComponent} in the
	 * container
	 * 
	 * @param component
	 * @throws DeploymentException
	 */
	public void installResourceAdaptorType(
			ResourceAdaptorTypeComponent component) throws DeploymentException;

	/**
	 * @see ResourceManagementMBean#removeResourceAdaptorEntity(String)
	 */
	public void removeResourceAdaptorEntity(String entityName)
			throws java.lang.NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			DependencyException;

	/**
	 * @see ResourceManagementMBean#unbindLinkName(String)
	 */
	public void unbindLinkName(String linkName) throws NullPointerException,
			UnrecognizedLinkNameException, DependencyException;

	/**
	 * Uninstalls the specified {@link ResourceAdaptorComponent} from the
	 * container
	 * 
	 * @param component
	 * @throws DependencyException
	 */
	public void uninstallResourceAdaptor(ResourceAdaptorComponent component)
			throws DependencyException;

	/**
	 * Uninstalls the specified {@link ResourceAdaptorTypeComponent} from the
	 * container
	 * 
	 * @param component
	 */
	public void uninstallResourceAdaptorType(
			ResourceAdaptorTypeComponent component);

	/**
	 * 
	 * @see ResourceManagementMBean#updateConfigurationProperties(String,
	 *      ConfigProperties)
	 */
	public void updateConfigurationProperties(String entityName,
			ConfigProperties properties) throws NullPointerException,
			UnrecognizedResourceAdaptorEntityException, InvalidStateException,
			InvalidConfigurationException;

}
