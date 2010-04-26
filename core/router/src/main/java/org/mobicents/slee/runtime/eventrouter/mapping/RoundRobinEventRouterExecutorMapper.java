/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter.mapping;

import java.util.concurrent.atomic.AtomicInteger;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;

/**
 * Round Robin {@link EventRouterExecutor} to {@link ActivityContextHandle} mapping.
 * @author martins
 *
 */
public class RoundRobinEventRouterExecutorMapper extends AbstractEventRouterExecutorMapper {

	/**
	 * index use to iterate the executor's array
	 */
	protected AtomicInteger index = null;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.mapping.AbstractEventRouterExecutorMapper#setExecutors(org.mobicents.slee.runtime.eventrouter.EventRouterExecutor[])
	 */
	@Override
	public void setExecutors(EventRouterExecutor[] executors) {
		super.setExecutors(executors);
		//reset index
		index = new AtomicInteger(0);
	}
	
	/**
	 * Computes the index of the next executor to retrieve. Adaptation of the {@link AtomicInteger} incrementAndGet() code.
	 *  
	 * @return
	 */
	private int getNextIndex() {
		for (;;) {
            int current = index.get();
            int next = (current == executors.length ? 1 : current + 1);
            if (index.compareAndSet(current, next))
                return next-1;
        }
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.mapping.AbstractEventRouterExecutorMapper#getExecutor(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	@Override
	public EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle) {
		return executors[getNextIndex()];
	}

}
