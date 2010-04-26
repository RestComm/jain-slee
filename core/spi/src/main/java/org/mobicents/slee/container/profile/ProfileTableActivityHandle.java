/**
 * 
 */
package org.mobicents.slee.container.profile;

import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public interface ProfileTableActivityHandle extends ActivityHandle {

	public ActivityContextHandle getActivityContextHandle();
	
}
