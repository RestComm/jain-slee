/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.io.Externalizable;

import javax.slee.resource.ActivityHandle;

/**
 * The handle for an {@link ActivityContext}.
 * 
 * @author martins
 * 
 */
public interface ActivityContextHandle extends Externalizable {

	/**
	 * 
	 * @return
	 */
	public ActivityHandle getActivityHandle();

	/**
	 * 
	 * @return
	 */
	public Object getActivityObject();
	
	/**
	 * 
	 * @return
	 */
	public ActivityType getActivityType();
	
}
