package org.mobicents.slee.runtime.eventrouter;

import org.mobicents.slee.runtime.activity.ActivityContext;

/**
 * Interface for the container's Event Router
 * 
 * @author Eduardo Martins
 * 
 */
public interface EventRouter {

	/**
	 * Retrieves the event router activity object for the {@link ActivityContext} with the specified id
	 * 
	 * @return
	 */
	public EventRouterActivity getEventRouterActivity(String acId);

	/**
	 * Requests the routing of a {@link DeferredEvent}
	 * 
	 * @param dE
	 */
	public void routeEvent(DeferredEvent dE);

	/**
	 * The activity has started so the event router may init related runtime resources
	 * @param ach
	 */
	public void activityStarted(String acId);
	
	/**
	 * The activity has ended so the event router may close related runtime resources 
	 * @param ach
	 */
	public void activityEnded(String acId);

	/**
	 * Configures the event router, defining the number of event executors and
	 * if it should monitor pending AC attachments. This method will throw
	 * {@link IllegalStateException} if the container state is RUNNING.
	 * 
	 * @param eventRouterExecutors
	 * @param monitoringUncommittedAcAttachs
	 */
	public void config(int eventRouterExecutors,
			boolean monitoringUncommittedAcAttachs);

}
