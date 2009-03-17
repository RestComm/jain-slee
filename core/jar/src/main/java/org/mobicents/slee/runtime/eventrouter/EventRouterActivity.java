package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ExecutorService;

import org.mobicents.slee.container.SleeContainer;

public class EventRouterActivity {

	/**
	 * 
	 */
	private final String acId;
	
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
	
	public EventRouterActivity(String acId, PendingAttachementsMonitor pendingAttachementsMonitor, SleeContainer sleeContainer) {
		this.acId = acId;
		this.eventQueueManager = new ActivityEventQueueManager(acId,sleeContainer);
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
	
	public String getActivityContextId() {
		return acId;
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
			return ((EventRouterActivity) obj).acId
					.equals(this.acId);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return acId.hashCode();
	}
	
}
