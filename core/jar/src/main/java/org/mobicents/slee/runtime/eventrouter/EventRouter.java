package org.mobicents.slee.runtime.eventrouter;

import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

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
	public EventRouterActivity getEventRouterActivity(ActivityContextHandle ach);

	/**
	 * Requests the routing of a {@link DeferredEvent}
	 * 
	 * @param dE
	 */
	public void routeEvent(DeferredEvent dE);
	
	/**
	 * Requests the routing of a {@link DeferredEvent} to be resumed, after it's
	 * suspension finished. This operation will be run in the same thread, which
	 * means it must be invoked from the activity executor service
	 * 
	 * @param eventContextImpl
	 */
	public void resumeEventContext(EventContextImpl eventContextImpl);

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

	/**
	 * Retrieves the object used to manage event references
	 * @return
	 */
	public DeferredEventReferencesManagement getDeferredEventReferencesManagement();

}
