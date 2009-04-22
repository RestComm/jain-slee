package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.SLEEException;
import javax.slee.management.SleeState;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTask;

/**
 * 
 * @author Eduardo Martins
 */

public class EventRouterImpl implements EventRouter {

	private static Logger logger = Logger.getLogger(EventRouter.class);

	/**
	 * the container's anchor object
	 */
	private final SleeContainer container;
	
	/**
	 * the {@link EventRouterActivity} objects, which hold all runtime structures related to the activity
	 */
	private final ConcurrentHashMap<String, EventRouterActivity> activities = new ConcurrentHashMap<String, EventRouterActivity>();
	
	/**
	 * The array of {@link ExecutorService}s that are used to route events
	 */
	private ExecutorService[] executors;
	
	private boolean monitorPendingACAttachements;
	
	/**
	 * the object used to manage event references
	 */
	private final DeferredEventReferencesManagement eventReferencesManagement = new DeferredEventReferencesManagement();
	
	public EventRouterImpl(SleeContainer container, int executors, boolean monitorPendingAcAttachments) {
		this.container = container;		
		config(executors, monitorPendingAcAttachments);
		logger
				.info("Mobicents JAIN SLEE Event Router started. Event Executors: "
						+ executors
						+ " Monitoring Pending AC Attachments: "
						+ monitorPendingAcAttachments);
	}
	
	public void routeEvent(DeferredEvent de) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Routing event: " + de.getEventTypeId() + " activity context "
					+ de.getActivityContextId() + " address " + de.getAddress());
		}

		if (container.getSleeState() == SleeState.STOPPED) {
			throw new SLEEException(
					"Mobicents SLEE container is in STOPPED state. Cannot route events.");
		}

		// execute routing of event
		de.getEventRouterActivity().getExecutorService().execute(new EventRoutingTask(container,de));

	}

	public void activityEnded(String acId) {
		activities.remove(acId);
	}

	public void activityStarted(String acId) {
		PendingAttachementsMonitor pendingAttachementsMonitor = null;
		if (monitorPendingACAttachements) {
			pendingAttachementsMonitor = new PendingAttachementsMonitor();
		}
		EventRouterActivity era = new EventRouterActivity(acId,pendingAttachementsMonitor,container);
		if (activities.putIfAbsent(acId,era) == null) {
			era.setExecutorService(mapExecutor(acId));
		}
	}
	
	/*
	 * Maps an executor to an activity
	 * 
	 * @return
	 */
	private ExecutorService mapExecutor(String acId) {
		return executors[activities.size() % executors.length];
	}
	
	@Override
	public String toString() {
		return "EventRouter: "
		+ "\n+-- Number of executors: " + executors.length
		+ "\n+-- Number of activities: " + activities.size();
	}

	public void config(int eventExecutorsSize,
			boolean monitoringUncommittedAcAttachs) {
		if (container.getSleeState() != SleeState.RUNNING) {
			// get ridden of old executors, if any
			if (this.executors != null) {
				for (ExecutorService executorService : this.executors) {
					executorService.shutdown();
				}
			}
			// create new ones
			this.executors = new ExecutorService[eventExecutorsSize];
			for (int i = 0; i < eventExecutorsSize; i++) {
				this.executors[i] = Executors.newSingleThreadExecutor();
			}			
			this.monitorPendingACAttachements = monitoringUncommittedAcAttachs;
		}
		else {
			throw new IllegalStateException("can't config event router with current slee state");
		}
	}
	
	public EventRouterActivity getEventRouterActivity(String acId) {		
		return activities.get(acId);
	}

	public void resumeEventContext(EventContextImpl eventContextImpl) {
		new EventRoutingTask(container,eventContextImpl.getDeferredEvent()).run();		
	}
	
	public DeferredEventReferencesManagement getDeferredEventReferencesManagement() {
		return eventReferencesManagement;
	}
}
