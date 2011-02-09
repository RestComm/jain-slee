/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTaskImpl;
import org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatisticsImpl;

/**
 * 
 * @author martins
 * 
 */
public class EventRouterExecutorImpl implements EventRouterExecutor {

	private final ExecutorService executor;
	private final EventRouterExecutorStatisticsImpl stats;
	private final SleeContainer sleeContainer;
	
	/**
	 * Used to collect executing stats of an {@link EventRoutingTask}.
	 * 
	 * @author martins
	 * 
	 */
	private class EventRoutingTaskStatsCollector implements Runnable {

		private final EventRoutingTask eventRoutingTask;

		public EventRoutingTaskStatsCollector(EventRoutingTask eventRoutingTask) {
			this.eventRoutingTask = eventRoutingTask;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final long startTime = System.nanoTime();
			eventRoutingTask.run();
			stats.eventRouted(eventRoutingTask.getEventContext().getEventTypeId(), System
					.nanoTime()
					- startTime);
		}
	}

	/**
	 * Used to collect executing stats of a misc {@link Runnable} task.
	 * 
	 * @author martins
	 * 
	 */
	private class MiscTaskStatsCollector implements Runnable {

		private final Runnable runnable;

		public MiscTaskStatsCollector(Runnable runnable) {
			this.runnable = runnable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			final long startTime = System.nanoTime();
			runnable.run();
			stats.miscTaskExecuted(System.nanoTime() - startTime);
		}
	}

	/**
	 * 
	 */
	public EventRouterExecutorImpl(boolean collectStats, SleeContainer sleeContainer) {
		this.executor = Executors.newSingleThreadExecutor();
		stats = collectStats ? new EventRouterExecutorStatisticsImpl() : null;
		this.sleeContainer = sleeContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#getStatistics
	 * ()
	 */
	public EventRouterExecutorStatistics getStatistics() {
		return stats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#shutdown()
	 */
	public void shutdown() {
		executor.shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#execute(java
	 * .lang.Runnable)
	 */
	public void execute(Runnable task) {
		if (stats == null) {
			executor.execute(task);
		} else {
			executor.execute(new MiscTaskStatsCollector(task));
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.runtime.eventrouter.EventRouterExecutor#executeNow(java.lang.Runnable)
	 */
	public void executeNow(Runnable task) throws InterruptedException, ExecutionException {
		if (stats == null) {
			executor.submit(task).get();
		} else {
			executor.submit(new MiscTaskStatsCollector(task)).get();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#activityMapped(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public void activityMapped(ActivityContextHandle ach) {
		if (stats != null) {
			stats.activityMapped(ach);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#activityUnmapped(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public void activityUnmapped(ActivityContextHandle ach) {
		if (stats != null) {
			stats.activityUnmapped(ach);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouterExecutor#routeEvent(org.mobicents.slee.core.event.SleeEvent)
	 */
	public void routeEvent(EventContext event) {
		final EventRoutingTaskImpl eventRoutingTask = new EventRoutingTaskImpl(event,sleeContainer);
		if (stats == null) {
			executor.execute(eventRoutingTask);
		} else {
			executor.execute(new EventRoutingTaskStatsCollector(
					eventRoutingTask));
		}
	}

}
