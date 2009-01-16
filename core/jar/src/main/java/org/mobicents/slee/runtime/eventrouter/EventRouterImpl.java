package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.SLEEException;
import javax.slee.management.SleeState;
import javax.slee.resource.FailureReason;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;
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
	private final ConcurrentHashMap<ActivityContextHandle, EventRouterActivity> activities = new ConcurrentHashMap<ActivityContextHandle, EventRouterActivity>();
	
	/**
	 * The array of {@link ExecutorService}s that are used to route events
	 */
	private ExecutorService[] executors;
	
	private boolean monitorPendingACAttachements;
	
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
			logger.debug("Routing event: " + de.getEventTypeId() + " activity "
					+ de.getActivityContextHandle() + " address " + de.getAddress());
		}

		if (container.getSleeState() == SleeState.STOPPED) {
			throw new SLEEException(
					"Mobicents SLEE container is in STOPPED state. Cannot route events.");
		}

		// execute routing of event
		final EventRouterActivity era = activities.get(de.getActivityContextHandle());
		era.getExecutorService().execute(new EventRoutingTask(container,de,era.getPendingAttachementsMonitor()));

	}

	public void processEventRoutingFailure(DeferredEvent de,
		FailureReason failureReason) {
		if (container.getSleeState() != SleeState.STOPPED) {
			if (de.getActivityContextHandle().getActivityType() == ActivityType.externalActivity) {
				ResourceAdaptorEntity raEntity;
				try {
					raEntity = container
							.getResourceManagement().getResourceAdaptorEntity(
									de.getActivityContextHandle()
											.getActivitySource());
					raEntity.getResourceAdaptor().eventProcessingFailed(
							de.getActivityContextHandle().getActivityHandle(),
							de.getEvent(), ((EventTypeIDImpl)de.getEventTypeId()).getEventID(), de.getAddress(), 0,
							failureReason);
				} catch (Exception e) {
					logger.error("failed to notify ra of event routing failure", e);
				}
				
			}			
		}
	}

	public void processSucessfulEventRouting(DeferredEvent de) {
		if (container.getSleeState() != SleeState.STOPPED) {
			if (de.getActivityContextHandle().getActivityType() == ActivityType.externalActivity) {
				ResourceAdaptorEntity raEntity;
				try {
					raEntity = container.getResourceManagement()
							.getResourceAdaptorEntity(
									de.getActivityContextHandle()
											.getActivitySource());
					raEntity.getResourceAdaptor().eventProcessingSuccessful(
							de.getActivityContextHandle().getActivityHandle(),
							de.getEvent(),
							((EventTypeIDImpl) de.getEventTypeId())
									.getEventID(), de.getAddress(), 0);
				} catch (Exception e) {
					logger.error(
							"failed to notify ra of event routing failure", e);
				}				
			}			
		}
	}

	public void activityEnded(ActivityContextHandle ach) {
		activities.remove(ach);
	}

	public void activityStarted(ActivityContextHandle ach) {
		PendingAttachementsMonitor pendingAttachementsMonitor = null;
		if (monitorPendingACAttachements) {
			pendingAttachementsMonitor = new PendingAttachementsMonitor();
		}
		EventRouterActivity era = new EventRouterActivity(ach,pendingAttachementsMonitor,container);
		if (activities.putIfAbsent(ach,era) == null) {
			era.setExecutorService(mapExecutor(ach));
		}
	}
	
	/*
	 * Maps an executor to an activity
	 * 
	 * @return
	 */
	private ExecutorService mapExecutor(ActivityContextHandle activityHandle) {
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
					executorService.shutdownNow();
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
	
	public EventRouterActivity getEventRouterActivity(ActivityContextHandle ach) {		
		return activities.get(ach);
	}
}
