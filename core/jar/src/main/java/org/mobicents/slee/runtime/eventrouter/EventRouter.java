package org.mobicents.slee.runtime.eventrouter;

import javax.slee.resource.FailureReason;

import org.mobicents.slee.runtime.activity.ActivityContextHandle;

/**
 * Interface for the container's Event Router
 * 
 * @author Eduardo Martins
 * 
 */
public interface EventRouter {

	/**
	 * Retrieves the event router activity object for the specified {@link ActivityContextHandle}
	 * 
	 * @return
	 */
	public EventRouterActivity getEventRouterActivity(ActivityContextHandle ach);

	/**
	 * Requests the routing of a {@link DeferredEvent}
	 * 
	 * @param dE
	 */
	public void routeEvent(DeferredEvent dE);

	/**
	 * The procedure of an event routing completed with success.
	 * @param dE
	 */
	public void processSucessfulEventRouting(DeferredEvent dE);

	/**
	 * The procedure of an event routing failed with the specified reason.
	 * @param dE
	 * @param reason
	 */
	public void processEventRoutingFailure(DeferredEvent dE,FailureReason failureReason);

	/**
	 * The activity has started so the event router may init related runtime resources
	 * @param ach
	 */
	public void activityStarted(ActivityContextHandle ach);
	
	/**
	 * The activity has ended so the event router may close related runtime resources 
	 * @param ach
	 */
	public void activityEnded(ActivityContextHandle ach);

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
