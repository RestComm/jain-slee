/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter.mapping;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRouterExecutorMapper;

/**
 * 
 * @author martins
 * 
 */
public abstract class AbstractEventRouterExecutorMapper implements
		EventRouterExecutorMapper {

	protected EventRouterExecutor[] executors;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.mapping.EventRouterExecutorMapper
	 * #getExecutor(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public abstract EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.mapping.EventRouterExecutorMapper
	 * #setExecutors
	 * (org.mobicents.slee.runtime.eventrouter.EventRouterExecutor[])
	 */
	public void setExecutors(EventRouterExecutor[] executors) {
		this.executors = executors;
	}

}
