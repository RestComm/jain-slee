/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform       *
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
import org.mobicents.slee.container.management.ProfileManagementImpl;
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
