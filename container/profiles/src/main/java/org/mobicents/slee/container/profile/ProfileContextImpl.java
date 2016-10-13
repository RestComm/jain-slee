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

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.Tracer;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.ProfileContextExt;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.facilities.TracerImpl;

/**
 * Start time:17:11:23 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This class represents ProfileContext. Note that profile context object MUST
 * belong to one profile table during its life time, profile(object) can change. <br>
 * The ProfileContext interface provides a Profile object with access to
 * SLEE-managed state that is dependent on the Profile objects's currently
 * executing context.
 * 
 * A ProfileContext object is given to a Profile object after the Profile object
 * is created via the setProfileContext method. The ProfileContext object
 * remains associated with the Profile object for the lifetime of that Profile
 * object. Note that the information that the Profile object obtains from the
 * ProfileContext object may change as the SLEE assigns the Profile object to
 * different profiles during the Profile object's lifecycle.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileContextImpl implements ProfileContextExt {

	private ProfileTableImpl profileTable = null;
	private ProfileObjectImpl profileObject = null;

	public ProfileContextImpl(ProfileTableImpl profileTable) {
		if (profileTable == null) {
			throw new NullPointerException("Parameters must not be null");
		}

		this.profileTable = profileTable;

	}

	public void setProfileObject(ProfileObjectImpl profileObject) {
		this.profileObject = profileObject;
	}


	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileContext#getProfileLocalObject()
	 */
	public ProfileLocalObject getProfileLocalObject()
			throws IllegalStateException, SLEEException {
		// check state
		if (profileObject == null
				|| profileObject.getState() == ProfileObjectState.PROFILE_INITIALIZATION
				|| profileObject.getProfileEntity() == null) {
			throw new IllegalStateException();
		}
		// check if it is default profile
		if (profileObject.getProfileEntity().getProfileName() == null) {
			throw new IllegalStateException();
		}
		return profileObject.getProfileLocalObject();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileContext#getProfileName()
	 */
	public String getProfileName() throws IllegalStateException, SLEEException {
		doGeneralChecks();

		if (profileObject == null
				|| profileObject.getState() == ProfileObjectState.PROFILE_INITIALIZATION
				|| profileObject.getProfileEntity() == null) {
			throw new IllegalStateException();
		}

		return this.profileObject.getProfileEntity().getProfileName();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileContext#getProfileTable()
	 */
	public ProfileTable getProfileTable() throws SLEEException {
		doGeneralChecks();
		return this.profileTable;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.profile.ProfileContext#getProfileTable(java.lang.String)
	 */
	public ProfileTable getProfileTable(String profileTableName)
			throws NullPointerException, UnrecognizedProfileTableNameException,
			SLEEException {

		return this.profileTable.getSleeContainer()
				.getSleeProfileTableManager().getProfileTable(profileTableName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileContext#getProfileTableName()
	 */
	public String getProfileTableName() throws SLEEException {
		doGeneralChecks();
		try {
			return this.profileTable.getProfileTableName();
		} catch (Exception e) {
			throw new SLEEException("Operaion failed.", e);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileContext#getRollbackOnly()
	 */
	public boolean getRollbackOnly() throws TransactionRequiredLocalException,
			SLEEException {
		doGeneralChecks();
		final SleeTransactionManager txMgr = profileTable.getSleeContainer()
				.getTransactionManager();
		txMgr.mandateTransaction();
		try {
			return txMgr.getRollbackOnly();
		} catch (SystemException e) {
			throw new SLEEException("Problem with the tx manager!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileContext#setRollbackOnly()
	 */
	public void setRollbackOnly() throws TransactionRequiredLocalException,
			SLEEException {
		doGeneralChecks();
		final SleeTransactionManager txMgr = profileTable.getSleeContainer()
				.getTransactionManager();
		txMgr.mandateTransaction();
		try {
			txMgr.setRollbackOnly();
		} catch (SystemException e) {
			throw new SLEEException("Problem with the tx manager!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileContext#getTracer(java.lang.String)
	 */
	public Tracer getTracer(String tracerName) throws NullPointerException,
			IllegalArgumentException, SLEEException {
		doGeneralChecks();
		try {
			TracerImpl.checkTracerName(tracerName, this.profileTable
					.getProfileTableNotification().getNotificationSource());
		} catch (InvalidArgumentException e1) {
			throw new IllegalArgumentException(e1);
		}
		try {
			return profileTable.getSleeContainer().getTraceManagement()
					.createTracer(
							this.profileTable.getProfileTableNotification()
									.getNotificationSource(), tracerName, true);
		} catch (Exception e) {
			throw new SLEEException("Failed to obtain tracer", e);
		}
	}

	private void doGeneralChecks() {
		if (this.profileTable == null)
			throw new SLEEException("Profile table has not been set.");
		if (this.profileTable.getProfileTableNotification() == null)
			throw new SLEEException("Profile table has no notification source.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.ProfileContextExt#getAlarmFacility()
	 */
	public AlarmFacility getAlarmFacility() {
		return profileTable.getProfileSpecificationComponent().getAlarmFacility();
	}

}
