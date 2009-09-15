package org.mobicents.slee.runtime.facilities.profile;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

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

	public ProfileTableActivityHandle(String profileTable) {
		this.profileTable = profileTable;
	}
    
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return this.profileTable.equals(((ProfileTableActivityHandle) obj).profileTable);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return profileTable.hashCode();
	}
    
	public String getProfileTable() {
		return profileTable;
	}
	
	@Override
	public String toString() {
		return profileTable;
	}
}
