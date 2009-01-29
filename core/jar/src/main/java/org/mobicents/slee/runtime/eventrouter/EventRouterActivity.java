package org.mobicents.slee.runtime.eventrouter;

import java.util.HashSet;
import java.util.Set;
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
	 * the set containing all sbb entities that handled the current event being routed
	 */
	private final Set<String> sbbEntitiesThatHandledCurrentEvent;
	
	public EventRouterActivity(String acId, PendingAttachementsMonitor pendingAttachementsMonitor, SleeContainer sleeContainer) {
		this.acId = acId;
		this.eventQueueManager = new ActivityEventQueueManager(acId,sleeContainer);
		this.pendingAttachementsMonitor = pendingAttachementsMonitor;
		this.sbbEntitiesThatHandledCurrentEvent = new HashSet<String>();
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
	
	public Set<String> getSbbEntitiesThatHandledCurrentEvent() {
		return sbbEntitiesThatHandledCurrentEvent;
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
