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
