package org.mobicents.slee.runtime.activity;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;

public class LocalActivityContextImpl implements LocalActivityContext {
	
	/**
	 * 
	 */
	private final ActivityContextHandle ach;
	
	/**
	 * 
	 */
	private EventRouterExecutor executor;
	
	/**
	 * 
	 */
	private final ActivityEventQueueManagerImpl eventQueueManager;
	
	/**
	 * 
	 */
	private EventRoutingTask routingTask;
		
	public LocalActivityContextImpl(ActivityContextHandle ach, SleeContainer sleeContainer) {
		this.ach = ach;
		this.eventQueueManager = new ActivityEventQueueManagerImpl(this);
	}
	
	public ActivityEventQueueManagerImpl getEventQueueManager() {
		return eventQueueManager;
	}
	
	public EventRouterExecutor getExecutorService() {
		return executor;
	}
	
	public void setExecutorService(EventRouterExecutor executor) {
		this.executor = executor;
	}
	
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.LocalActivityContext#getCurrentEventRoutingTask()
	 */
	public EventRoutingTask getCurrentEventRoutingTask() {
		return routingTask;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.LocalActivityContext#setCurrentEventRoutingTask(org.mobicents.slee.container.eventrouter.EventRoutingTask)
	 */
	public void setCurrentEventRoutingTask(EventRoutingTask eventRoutingTask) {
		this.routingTask = eventRoutingTask;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((LocalActivityContextImpl) obj).ach
					.equals(this.ach);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ach.hashCode();
	}
	
	
}
