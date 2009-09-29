package org.mobicents.slee.runtime.facilities.profile;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.jgroups.Address;

/**
 * Handle for a profile table activity.
 * 
 * @author martins
 *
 */
public class ProfileTableActivityHandle implements ActivityHandle, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String profileTable;

	private final Address clusterLocalAddress; 
	
	public ProfileTableActivityHandle(String profileTable) {
		this.profileTable = profileTable;
		this.clusterLocalAddress = null;
	}
    
	public ProfileTableActivityHandle(String profileTable, Address clusterLocalAddress) {
		this.profileTable = profileTable;
		this.clusterLocalAddress = clusterLocalAddress;
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ProfileTableActivityHandle other = (ProfileTableActivityHandle) obj;
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
