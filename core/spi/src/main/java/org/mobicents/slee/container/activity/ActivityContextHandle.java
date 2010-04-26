/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

/**
 * The handle for an {@link ActivityContext}.
 * 
 * @author martins
 * 
 */
public interface ActivityContextHandle extends Serializable {

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
