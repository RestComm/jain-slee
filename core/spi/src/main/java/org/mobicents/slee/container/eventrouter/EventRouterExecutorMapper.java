/**
 * 
 */
package org.mobicents.slee.container.eventrouter;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * Maps {@link EventRouterExecutor} to Activity Contexts.
 * 
 * @author martins
 * 
 */
public interface EventRouterExecutorMapper {

	/**
	 * 
	 * @param executors
	 */
	public void setExecutors(EventRouterExecutor[] executors);

	/**
	 * Retrieves the executor for the activity context with the specified
	 * handle.
	 * 
	 * @param activityContextHandle
	 * @return
	 */
	public EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle);

}
