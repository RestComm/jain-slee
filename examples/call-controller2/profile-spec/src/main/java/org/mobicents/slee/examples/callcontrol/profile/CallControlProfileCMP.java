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

/**
 * 
 * User Subscription Profile 
 * 
 * @author torosvi
 * @author Ivelin Ivanov
 *
 */
public interface CallControlProfileCMP {
	// 'userAddress' CMP field setter
	public abstract void setUserAddress(Address value);
	// 'userAddress' CMP field getter
	public abstract Address getUserAddress();

	// 'blockedAddresses' CMP field setter
	public abstract void setBlockedAddresses(Address[] value);
	// 'blockedAddresses' CMP field getter
	public abstract Address[] getBlockedAddresses();

	// 'backupAddress' CMP field setter
	public abstract void setBackupAddress(Address value);
	// 'backupAddress' CMP field getter
	public abstract Address getBackupAddress();

	// 'voicemailState' CMP field setter
	public abstract void setVoicemailState(boolean value);
	// 'voicemailState' CMP field getter
	public abstract boolean getVoicemailState();
}