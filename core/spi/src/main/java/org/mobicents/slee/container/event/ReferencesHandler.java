/**
 * 
 */
package org.mobicents.slee.container.event;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public interface ReferencesHandler {

	/**
	 * 
	 * @param ach
	 */
	public void add(ActivityContextHandle ach);

	/**
	 * 
	 * @param ach
	 */
	public void remove(ActivityContextHandle ach);

}
