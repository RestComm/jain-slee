package org.mobicents.slee.container.activity;

import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;

/**
 * Local view and resources of the activity context.
 * 
 * @author martins
 *
 */
public interface LocalActivityContext {
	
	public ActivityContextHandle getActivityContextHandle();
		
	public EventRoutingTask getCurrentEventRoutingTask();
	
	public ActivityEventQueueManager getEventQueueManager();
	
	public EventRouterExecutor getExecutorService();
		
	public void setCurrentEventRoutingTask(EventRoutingTask eventRoutingTask);
	
	public void setExecutorService(EventRouterExecutor executor);
			
}
