package org.mobicents.slee.runtime.facilities.profile;

import java.io.Serializable;

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
public class ProfileTableActivityHandleImpl implements ProfileTableActivityHandle, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String profileTable;

	private final Address clusterLocalAddress; 
	
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
    
	public String getProfileTable() {
		return profileTable;
	}
	
	@Override
	public String toString() {
		return profileTable;
	}
}
