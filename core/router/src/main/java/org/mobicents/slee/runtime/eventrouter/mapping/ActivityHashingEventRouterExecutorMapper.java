/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter.mapping;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;

/**
 * Simple {@link EventRouterExecutor} to {@link ActivityContextHandle} mapping
 * using the hashcode of the latter.
 * 
 * @author martins
 * 
 */
public class ActivityHashingEventRouterExecutorMapper extends
		AbstractEventRouterExecutorMapper {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.runtime.eventrouter.mapping.
	 * AbstractEventRouterExecutorMapper
	 * #getExecutor(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	@Override
	public EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle) {
		return executors[Math.abs(activityContextHandle.hashCode())
				% executors.length];
	}

}
