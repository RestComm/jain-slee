package org.mobicents.slee.container.component.profile;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;

/**
 * Interface for a {@link ProfileEntity} factory object, which can be used to
 * build specific entity objects for an {@link ProfileSpecificationComponent}.
 * 
 * @author martins
 * 
 */
public interface ProfileEntityFactory {

	/**
	 * Creates a new {@link ProfileEntity} instance with the specified profile
	 * name, for the specified profile table name.
	 * 
	 * @param profileTableName
	 * @param profileName
	 * @return
	 */
	public ProfileEntity newInstance(String profileTableName, String profileName);
	
	/**
	 * Copies the attributes between two instances of {@link ProfileEntity}.
	 * 
	 * @param from
	 * @param to
	 */
	public void copyAttributes(ProfileEntity from, ProfileEntity to);
	
}
