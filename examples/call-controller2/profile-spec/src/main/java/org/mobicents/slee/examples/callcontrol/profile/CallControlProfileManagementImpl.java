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

package org.mobicents.slee.examples.callcontrol.profile;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.CreateException;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileVerificationException;

/**
 * Profile Management implementation class.
 */
public abstract class CallControlProfileManagementImpl implements Profile,
		CallControlProfileCMP {

	@SuppressWarnings("unused")
	private ProfileContext profileCtx;

	/**
	 * Initialize the profile with its default values.
	 */
	public void profileInitialize() {
		setUserAddress(null);
		setBlockedAddresses(null);
		setBackupAddress(null);
		setVoicemailState(false);
	}

	public void profileLoad() {
	}

	public void profileStore() {
	}

	/**
	 * Verify the profile's CMP field settings.
	 * 
	 * @throws ProfileVerificationException
	 *             if any CMP field contains an invalid value
	 */
	public void profileVerify() throws ProfileVerificationException {
		// Verify Called User Address
		Address address = getUserAddress();
		if (address != null)
			verifyAddress(address);
		// Verify Blocked Addresses
		Address[] blockedAddresses = getBlockedAddresses();
		if (blockedAddresses != null) {
			for (int i = 0; i < blockedAddresses.length; i++) {
				if (blockedAddresses[i] != null)
					verifyAddress(blockedAddresses[i]);
			}
		}
		// Verify Backup Address
		Address backupAddress = getBackupAddress();
		if (backupAddress != null)
			verifyAddress(backupAddress);
	}

	public void verifyAddress(Address address)
			throws ProfileVerificationException {
		// Check address plan
		if (address.getAddressPlan() != AddressPlan.SIP)
			throw new ProfileVerificationException("Address \"" + address
					+ "\" is not a SIP address");
		// Check URI scheme - must be sip: or sips:
		String uri = address.getAddressString().toLowerCase();
		if (!(uri.startsWith("sip:") || uri.startsWith("sips:")))
			throw new ProfileVerificationException("Address \"" + address
					+ "\" is not a SIP address");
	}

	public void profileActivate() {
		// TODO Auto-generated method stub

	}

	public void profilePassivate() {
		// TODO Auto-generated method stub

	}

	public void profilePostCreate() throws CreateException {
		// TODO Auto-generated method stub

	}

	public void profileRemove() {
		// TODO Auto-generated method stub

	}

	public void setProfileContext(ProfileContext ctx) {
		this.profileCtx = ctx;

	}

	public void unsetProfileContext() {
		// TODO Auto-generated method stub
		this.profileCtx = null;
	}

}