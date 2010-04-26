/**
 * 
 */
package org.mobicents.slee.container.component.common;

import javax.slee.profile.ProfileSpecificationID;

/**
 * @author martins
 *
 */
public interface ProfileSpecRefDescriptor {

	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationID getComponentID();

	/**
	 * 
	 * @return
	 */
	public String getProfileSpecAlias();
	
}
