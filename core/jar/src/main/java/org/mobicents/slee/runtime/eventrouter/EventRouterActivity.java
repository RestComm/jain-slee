package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ExecutorService;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

public class EventRouterActivity {

	/**
	 * 
	 */
	private final ActivityContextHandle ach;
	
	/**
	 * 
	 */
	private ExecutorService executorService;
	
	/**
	 * 
	 */
	private final ActivityEventQueueManager eventQueueManager;
	
	/**
	 * 
	 */
	private final PendingAttachementsMonitor pendingAttachementsMonitor;
	
	/**
	 * the event context for the event currently being routed
	 */
	private EventContextImpl currentEventContext;
	
	public EventRouterActivity(ActivityContextHandle ach, PendingAttachementsMonitor pendingAttachementsMonitor, SleeContainer sleeContainer) {
		this.ach = ach;
		this.eventQueueManager = new ActivityEventQueueManager(ach,sleeContainer);
		this.pendingAttachementsMonitor = pendingAttachementsMonitor;
	}
	
	public ActivityEventQueueManager getEventQueueManager() {
		return eventQueueManager;
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	protected void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
		
	public PendingAttachementsMonitor getPendingAttachementsMonitor() {
		return pendingAttachementsMonitor;
	}
	
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}
	
	public EventContextImpl getCurrentEventContext() {
		return currentEventContext;
	}
	
	public void setCurrentEventContext(EventContextImpl currentEventContext) {
		this.currentEventContext = currentEventContext;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((EventRouterActivity) obj).ach
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
