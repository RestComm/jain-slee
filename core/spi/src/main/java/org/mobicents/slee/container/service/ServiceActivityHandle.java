/**
 * 
 */
package org.mobicents.slee.container.service;

import javax.slee.ServiceID;
import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public interface ServiceActivityHandle extends ActivityHandle {

	/**
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle();
	
	public ServiceID getServiceID();
}
