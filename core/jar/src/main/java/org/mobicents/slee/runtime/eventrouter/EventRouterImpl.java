package org.mobicents.slee.runtime.eventrouter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.slee.SLEEException;
import javax.slee.management.SleeState;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.LogMessageFactory;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
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
	
	/**
	 * the object used to manage event references
	 */
	private final DeferredEventReferencesManagement eventReferencesManagement = new DeferredEventReferencesManagement();
	
	public EventRouterImpl(SleeContainer container, int executors, boolean monitorPendingAcAttachments) {
		this.container = container;		
		final long period = 60*60*1000;  
		container.getNonClusteredScheduler().scheduleAtFixedRate(new LocalResourcesGarbageCollectionTimerTask(), period, period, TimeUnit.MILLISECONDS);
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
					+ de.getActivityContextHandle() + " address " + de.getAddress());
		}

		if (container.getSleeState() == SleeState.STOPPED) {
			throw new SLEEException(
					"Mobicents SLEE container is in STOPPED state. Cannot route events.");
		}

		// execute routing of event
		de.getEventRouterActivity().getExecutorService().execute(new EventRoutingTask(de));

	}

	public void activityEnded(ActivityContextHandle ach) {
		activities.remove(ach);
	}
	
	/*
	 * Maps an executor to an activity
	 * 
	 * @return
	 */
	private ExecutorService mapExecutor(ActivityContextHandle ach) {
		return executors[activities.size() % executors.length];
	}
	
	@Override
	public String toString() {
		return "EventRouter: "
		+ "\n+-- Executors: " + executors.length
		+ "\n+-- Activities: " + activities.keySet();
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
	
	public EventRouterActivity getEventRouterActivity(ActivityContextHandle ach) {		
		EventRouterActivity era = activities.get(ach);
		if (era == null) {
			PendingAttachementsMonitor pendingAttachementsMonitor = null;
			if (monitorPendingACAttachements) {
				pendingAttachementsMonitor = new PendingAttachementsMonitor();
			}
			final EventRouterActivity newEra = new EventRouterActivity(ach,pendingAttachementsMonitor,container);
			era = activities.putIfAbsent(ach,newEra);
			if (era == null) {
				era = newEra;
				era.setExecutorService(mapExecutor(ach));
			}
		}
		return era;
	}

	public void resumeEventContext(EventContextImpl eventContextImpl) {
		new EventRoutingTask(eventContextImpl.getDeferredEvent()).run();		
	}
	
	public DeferredEventReferencesManagement getDeferredEventReferencesManagement() {
		return eventReferencesManagement;
	}
		
	/**
	 * Runnable to remove event router local resources for activities that are already gone
	 */
	private class LocalResourcesGarbageCollectionTimerTask implements Runnable {
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (logger.isDebugEnabled()) {
				logger.debug("Running Event Router's activities local resources garbage collection task");
			}
			try {
				final Set<ActivityContextHandle> set = new HashSet<ActivityContextHandle>(activities.keySet());
				if (logger.isDebugEnabled()) {
					logger.debug("Current Event Router's activities local resources: "+set);
				}
				set.removeAll(container.getActivityContextFactory().getAllActivityContextsHandles());
				for (ActivityContextHandle ach : set) {
					if (logger.isDebugEnabled()) {
						logger.debug(LogMessageFactory.newLogMessage(ach, "Removing the event router local resources for the activity"));
					}
					activities.remove(ach);
				}	
			}
			catch (Throwable e) {
				logger.error("Failure in event router activity resources garbage collection",e);
			}
		}
	}
}