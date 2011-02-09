/**
 * 
 */
package org.mobicents.slee.container.component.profile.cmp;

import java.util.List;

/**
 * @author martins
 *
 */
public interface ProfileCMPInterfaceDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileCMPFieldDescriptor> getCmpFields();
	
	/**
	 * 
	 * @return
	 */
	public String getProfileCmpInterfaceName();
	
}
