/**
 * 
 */
package org.mobicents.slee.container.profile;

import org.mobicents.slee.container.profile.entity.ProfileEntity;

/**
 * @author martins
 * 
 */
public interface ProfileObject {

	/**
	 * @return
	 */
	public Object getProfileCmpSlee10Wrapper();

	/**
	 * 
	 * @return
	 */
	public ProfileEntity getProfileEntity();

	/**
	 * 
	 * @return
	 */
	public ProfileTable getProfileTable();

	/**
	 * 
	 * @return
	 */
	public ProfileObjectState getState();

}
