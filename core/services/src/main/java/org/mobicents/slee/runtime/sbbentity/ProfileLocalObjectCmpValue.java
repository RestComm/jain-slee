package org.mobicents.slee.runtime.sbbentity;

import java.io.Serializable;

import javax.slee.profile.ProfileLocalObject;

/**
 * The cmp value stored in cache for a {@link ProfileLocalObject}
 * 
 * @author martins
 *
 */
public class ProfileLocalObjectCmpValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String profileTableName;
	
	private final String profileName;

	public ProfileLocalObjectCmpValue(String profileTableName,
			String profileName) {
		this.profileTableName = profileTableName;
		this.profileName = profileName;
	}

	public String getProfileTableName() {
		return profileTableName;
	}

	public String getProfileName() {
		return profileName;
	}

	@Override
	public int hashCode() {
		return profileTableName.hashCode() * 31 + ((profileName == null) ? 0 : profileName.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final ProfileLocalObjectCmpValue other = (ProfileLocalObjectCmpValue) obj;
		
		if (profileName == null) {
			if (other.profileName != null)
				return false;
		} else if (!profileName.equals(other.profileName))
			return false;
		
		return profileTableName.equals(other.profileTableName);
	}
	
	
}
