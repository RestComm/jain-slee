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
package org.mobicents.slee.container.component;

import java.io.File;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.classloading.ComponentClassLoader;
import org.mobicents.slee.container.component.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.component.du.DeployableUnit;
import org.mobicents.slee.container.component.security.PermissionHolder;

/**
 * @author martins
 *
 */
public interface SleeComponent {
	
	/**
	 * Retrieves the component class loader
	 * 
	 * @return
	 */
	public ComponentClassLoader getClassLoader();

	/**
	 * Retrieves the class loader domain for the component jar this component
	 * belongs, components that depend on this component must add this in its
	 * domain.
	 * 
	 * @return
	 */
	public URLClassLoaderDomain getClassLoaderDomain();
	
	/**
	 * Retrieves the component javassist class pool
	 * 
	 * @return
	 */
	public ClassPool getClassPool();
	
	/**
	 * Retrieves the file pointing to where this component is deployed
	 * 
	 * @return
	 */
	public File getDeploymentDir();

	/**
	 * Sets the the file pointing where this component is deployed
	 * 
	 * @param deploymentDir
	 */
	public void setDeploymentDir(File deploymentDir);

	/**
	 * Retrieves the source for this component (component jar/service
	 * descriptor) in the deployable unit
	 * 
	 * @return
	 */
	public String getDeploymentUnitSource();

	/**
	 * Sets the source for this component (component jar/service descriptor) in
	 * the deployable unit
	 * 
	 * @param deploymentUnitSource
	 */
	public void setDeploymentUnitSource(String deploymentUnitSource);

	/**
	 * Retrieves the Deployable Unit this component belongs
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit();

	/**
	 * Specifies the the Deployable Unit this component belongs. This method
	 * also sets the reverse relation, adding the component to the deployable
	 * unit
	 * 
	 * @param deployableUnit
	 * @throws AlreadyDeployedException
	 *             if a component with same id already exists in the du
	 * @throws IllegalStateException
	 *             if this method is invoked and the deployable unit was already
	 *             set before
	 */
	public void setDeployableUnit(DeployableUnit deployableUnit)
			throws AlreadyDeployedException;

	
	/**
	 * Gets set with permissions. This may be empty list in case of components that dont have them. 
	 * In case of compoennts that can have xml-desc wide permissions - this set contains this permissions,
	 *  as does any other components set defined in that xml-descr. however impl of Policy MUST 
	 *  handle double defined polic
	 * @return
	 */
	public Set<PermissionHolder> getPermissions();

	/**
	 * adds the component to the deployable unit
	 * 
	 * @return true if the component was added
	 */
	public boolean addToDeployableUnit();

	/**
	 * Retrieves the set of components IDs this component depends
	 * 
	 * @return
	 */
	public Set<ComponentID> getDependenciesSet();

	/**
	 * Retrieves the ID of this component
	 * 
	 * @return
	 */
	public ComponentID getComponentID();

	/**
	 * Indicates if the component is new to SLEE 1.1 specs
	 * 
	 * @return
	 */
	public boolean isSlee11();

	/**
	 * Validates the component.
	 * 
	 * @return
	 * @throws DependencyException
	 * @throws DeploymentException
	 */
	public boolean validate() throws DependencyException,
			DeploymentException;

	/**
	 * Retrieves the JAIN SLEE specs component descriptor
	 * 
	 * @return
	 */
	public abstract ComponentDescriptor getComponentDescriptor();
	
	public abstract void processSecurityPermissions() throws DeploymentException;
	
	/**
	 * Indicates that the component was undeployed and thus should clean up any resources 
	 */
	public void undeployed();
}
