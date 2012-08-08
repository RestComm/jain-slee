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

package org.mobicents.slee.container.profile;

import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;

/**
 * Start time:18:25:55 2009-03-17<br>
 * Project: mobicents-jainslee-server-core<br>
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
