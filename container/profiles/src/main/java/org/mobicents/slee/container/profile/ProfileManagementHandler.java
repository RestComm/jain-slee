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

import javax.slee.SLEEException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * 
 * ProfileManagementHandler.java
 * 
 * <br>
 * Project: restcomm <br>
 * 3:26:16 PM Apr 3, 2009 <br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileManagementHandler {

	private static Logger logger = Logger
			.getLogger(ProfileManagementHandler.class);

	public static boolean isProfileDirty(ProfileObjectImpl profileObject) {
		if (logger.isDebugEnabled()) {
			logger.info("[isProfileDirty] @ " + profileObject);
		}

		return profileObject.getProfileEntity().isDirty();
	}

	public static boolean isProfileValid(ProfileObject profileObject,
			ProfileID profileId) throws NullPointerException, SLEEException {
		if (logger.isDebugEnabled()) {
			logger.info("[isProfileValid(" + profileId + ")] @ "
					+ profileObject);
		}

		if (profileId == null) {
			throw new NullPointerException("null profile id");
		}
		
		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		
		boolean terminateTx = txManager.requireTransaction(); 
		try {
			ProfileTableImpl profileTable = ((ProfileManagementImpl)sleeContainer.getSleeProfileTableManager()).getProfileTable(profileId.getProfileTableName());
			return profileTable.profileExists(profileId.getProfileName());
		}
		catch (UnrecognizedProfileTableNameException e) {
			return false;
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally {
			try {
				txManager.requireTransactionEnd(terminateTx, false);
			} catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	public static void markProfileDirty(ProfileObjectImpl profileObject) {
		if (logger.isDebugEnabled()) {
			logger.info("[markProfileDirty] @ " + profileObject);
		}

		profileObject.getProfileEntity().setDirty(true);
	}

	// Usage methods. Here we can be static for sure. Rest must be tested.
	public static Object getUsageParameterSet(ProfileObjectImpl profileObject,
			String name) throws UnrecognizedUsageParameterSetNameException {
		if (logger.isDebugEnabled()) {
			logger.info("[getUsageParameterSet(" + name + ")] @ "
					+ profileObject);
		}

		if (name == null) {
			throw new NullPointerException(
					"UsageParameterSet name must not be null.");
		}
		ProfileTableImpl profileTable = profileObject
				.getProfileTable();
		Object result = profileTable.getProfileTableUsageMBean()
				.getInstalledUsageParameterSet(name);
		if (result == null) {
			throw new UnrecognizedUsageParameterSetNameException();			
		}
		else {
			return result;
		}
	}

	public static Object getDefaultUsageParameterSet(
			ProfileObjectImpl profileObject) {
	
		if (logger.isDebugEnabled()) {
			logger.info("[getDefaultUsageParameterSet] @ "
					+ profileObject);
		}
		ProfileTableImpl profileTable = profileObject
				.getProfileTable();
		return profileTable.getProfileTableUsageMBean().getDefaultInstalledUsageParameterSet();
	}

	private static ClassLoader switchContextClassLoader(
			ClassLoader newClassLoader) {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(newClassLoader);

		return oldClassLoader;
	}

}
