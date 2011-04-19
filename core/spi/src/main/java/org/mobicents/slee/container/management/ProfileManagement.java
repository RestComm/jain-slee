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

import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.profile.ProfileTable;

/**
 * @author martins
 * 
 */
public interface ProfileManagement extends SleeContainerModule {

	/**
	 * 
	 * @return
	 */
	public ProfileFacility getProfileFacility();

	/**
	 * 
	 * @param profileTableName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 */
	public ProfileTable getProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException;

	/**
	 * 
	 * @return
	 */
	public ProfileTableActivityContextInterfaceFactory getProfileTableActivityContextInterfaceFactory();
	
	/**
	 * Installs profile into container, creates default profile and reads
	 * backend storage to search for other profiles - it creates MBeans for all
	 * 
	 * @param component
	 * @throws DeploymentException
	 *             - this exception is thrown in case of error FIXME: on check
	 *             to profile - if we have one profile table active and we
	 *             encounter another, what shoudl happen? is there auto init for
	 *             all in back end memory?
	 */
	public void installProfileSpecification(
			ProfileSpecificationComponent component) throws DeploymentException;

	/**
	 * 
	 * @param component
	 * @throws UnrecognizedProfileSpecificationException
	 */
	public void uninstallProfileSpecification(
			ProfileSpecificationComponent component)
			throws UnrecognizedProfileSpecificationException;

}
