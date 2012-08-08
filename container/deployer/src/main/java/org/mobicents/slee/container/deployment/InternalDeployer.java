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

package org.mobicents.slee.container.deployment;

import java.net.URL;

import org.jboss.deployment.DeploymentException;

/**
 * The interface for the deployer inside the SLEE Container.
 * 
 * @author martins
 * 
 */
@SuppressWarnings("deprecation")
public interface InternalDeployer {

	/**
	 * Sets the external deployer.
	 * 
	 * @param deployer
	 */
	public void setExternalDeployer(ExternalDeployer deployer);

	/**
	 * 
	 * @param deployableUnitURL
	 * @return
	 */
	public boolean accepts(URL deployableUnitURL);

	/**
	 * 
	 * @param componentURL
	 * @throws DeploymentException
	 */
	public void init(URL componentURL) throws DeploymentException;

	/**
	 * 
	 * @param componentURL
	 * @throws DeploymentException
	 */
	public void start(URL componentURL) throws DeploymentException;

	/**
	 * 
	 * @param deployableUnitURL
	 * @throws DeploymentException
	 */
	public void stop(URL deployableUnitURL) throws DeploymentException;

}
