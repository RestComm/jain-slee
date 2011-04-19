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

package org.mobicents.slee.runtime.facilities.profile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.jgroups.Address;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.profile.ProfileTableActivityContextHandle;
import org.mobicents.slee.container.profile.ProfileTableActivityHandle;

/**
 * Handle for a profile table activity.
 * 
 * @author martins
 *
 */
public class ProfileTableActivityHandleImpl implements ProfileTableActivityHandle, Externalizable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient String profileTable;

	private transient Address clusterLocalAddress; 
	
	public ProfileTableActivityHandleImpl() {
		// for externalizable only
	}
	
	public ProfileTableActivityHandleImpl(String profileTable, Address clusterLocalAddress) {
		this.profileTable = profileTable;
		this.clusterLocalAddress = clusterLocalAddress;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.profile.ProfileTableActivityHandle#getActivityContextHandle()
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return new ProfileTableActivityContextHandle(this);
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ProfileTableActivityHandleImpl other = (ProfileTableActivityHandleImpl) obj;
			if (this.clusterLocalAddress == null) {
				if (other.clusterLocalAddress != null) {
					return false;
				}				
			}
			else {
				if (!this.clusterLocalAddress.equals(other.clusterLocalAddress)) {
					return false;
				}
			}
			return this.profileTable.equals(other.profileTable);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return profileTable.hashCode() * 31 + (clusterLocalAddress == null ? 0 : clusterLocalAddress.hashCode()) ;
	}
    
	public void setClusterLocalAddress(Address clusterLocalAddress) {
		this.clusterLocalAddress = clusterLocalAddress;
	}
	
	public void setProfileTable(String profileTable) {
		this.profileTable = profileTable;
	}
	
	public String getProfileTable() {
		return profileTable;
	}
	
	public Address getClusterLocalAddress() {
		return clusterLocalAddress;
	}
	
	@Override
	public String toString() {
		return profileTable;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		profileTable = in.readUTF();
		clusterLocalAddress = (Address) in.readObject();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(profileTable);
		out.writeObject(clusterLocalAddress);
	}
}
