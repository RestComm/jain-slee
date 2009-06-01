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
	 * Creates a new {@link ProfileEntity} instance with the specified profile
	 * name, and copies the profile table name and all the fields from the specified profile entity.
	 * 
	 * @param profileName
	 * @param profileEntity
	 * @return
	 */
	public ProfileEntity cloneInstance(String profileName, ProfileEntity profileEntity);
	
}
