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

package org.mobicents.slee.container.profile;

import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;

/**
 * Start time:18:25:55 2009-03-17<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * This class encapsulates logic to set/get cmp field of profile,
 * acting as an interceptor.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
public class ProfileCmpHandler {

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param profileObject
	 * @throws IllegalStateException
	 * @throws ReadOnlyProfileException
	 */
	public static void beforeSetCmpField(ProfileObjectImpl profileObject) throws IllegalStateException, ReadOnlyProfileException {
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		if (profileObject.getState() != ProfileObjectState.READY && profileObject.getState() != ProfileObjectState.PROFILE_INITIALIZATION) {
			throw new IllegalStateException("Profile object must be in ready state");
		}
		
		if (profileObject.getProfileEntity().isReadOnly()) {				
			throw new ReadOnlyProfileException("Profile: " + profileObject.getProfileEntity().getProfileName() + ", table:" + profileObject.getProfileTable().getProfileTableName() + " ,is not writeable.");
		}
		
		ProfileCallRecorderTransactionData.addProfileCall(profileObject);			
	}

	/**
	 * 
	 * @param profileObject
	 */
	public static void afterSetCmpField(ProfileObjectImpl profileObject) {
		profileObject.getProfileEntity().setDirty(true);
		ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}
	
	/**
	 * 
	 * @param profileObject
	 * @throws IllegalStateException
	 */
	public static void beforeGetCmpField(ProfileObjectImpl profileObject) throws IllegalStateException {
		
		sleeContainer.getTransactionManager().mandateTransaction();

		if (profileObject.getState() != ProfileObjectState.READY && profileObject.getState() != ProfileObjectState.PROFILE_INITIALIZATION) {
			throw new IllegalStateException("Profile object must be in ready state");
		}
		
		ProfileCallRecorderTransactionData.addProfileCall(profileObject);			
	}

	/**
	 * 
	 * @param profileObject
	 */
	public static void afterGetCmpField(ProfileObjectImpl profileObject) {

		ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}

}
