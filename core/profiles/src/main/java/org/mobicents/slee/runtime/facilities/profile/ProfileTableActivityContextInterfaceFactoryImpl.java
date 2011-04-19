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

/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.profile.ProfileTableActivityContextHandle;
import org.mobicents.slee.container.profile.ProfileTableImpl;

/**
 * Implmenetation of profile table activity context interface factory.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 */
public class ProfileTableActivityContextInterfaceFactoryImpl implements
		ProfileTableActivityContextInterfaceFactory {

	public static String JNDI_NAME = "profiletableactivitycontextinterfacefactory";

	private final SleeContainer serviceContainer;
	private ProfileManagementImpl profileManagementImpl;
	
	/**
	 * 
	 */
	public ProfileTableActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer, ProfileManagementImpl profileManagementImpl) {
		this.serviceContainer = serviceContainer;
		this.profileManagementImpl = profileManagementImpl; 
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileTableActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.profile.ProfileTableActivity)
	 */
	public ActivityContextInterface getActivityContextInterface(
			ProfileTableActivity profileTableActivity)
			throws NullPointerException, TransactionRequiredLocalException,
			UnrecognizedActivityException, FactoryException {
		if (profileTableActivity == null
				|| profileTableActivity.getProfileTableName() == null) {

			throw new NullPointerException("null profile table activity");

		}

		serviceContainer.getTransactionManager().mandateTransaction();
		ProfileTableImpl profileTableImpl = null;
		try {
			// check if this is an assigned profile table
			// name.
			profileTableImpl = profileManagementImpl.getProfileTable(profileTableActivity.getProfileTableName());
		} catch (UnrecognizedProfileTableNameException e) {
			throw new UnrecognizedActivityException(profileTableActivity.getProfileTableName(),e);
		}
		
        ActivityContext ac = profileTableImpl.getActivityContext();
        if (ac == null) {
        	throw new UnrecognizedActivityException("No resource for: "+profileTableActivity.getProfileTableName(),profileTableActivity);
        }
        
		return ac.getActivityContextInterface();

	}
}
