/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */
package org.mobicents.slee.runtime.eventrouter;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTaskImpl;
import org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatisticsImpl;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	public EventRouterExecutorImpl(boolean collectStats, ThreadFactory threadFactory, SleeContainer sleeContainer) {
		final LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<Runnable>();
		this.executor = new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        executorQueue, threadFactory);
		stats = collectStats ? new EventRouterExecutorStatisticsImpl(Collections.unmodifiableCollection(executorQueue)) : null;
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
