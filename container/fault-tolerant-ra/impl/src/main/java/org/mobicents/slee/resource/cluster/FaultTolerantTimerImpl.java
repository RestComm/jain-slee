/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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
 */

package org.mobicents.slee.resource.cluster;

import org.mobicents.slee.container.SleeContainer;
import org.restcomm.timers.FaultTolerantScheduler;
import org.restcomm.timers.PeriodicScheduleStrategy;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the FT RA Timer.
 * 
 * @author martins
 * 
 */
public class FaultTolerantTimerImpl implements FaultTolerantTimer {

	private final SleeContainer sleeContainer;
	private final String raEntity;

	private FaultTolerantScheduler scheduler;

	/**
	 * 
	 * @param sleeContainer
	 * @param raEntity
	 */
	public FaultTolerantTimerImpl(SleeContainer sleeContainer, String raEntity) {
		this.sleeContainer = sleeContainer;
		this.raEntity = raEntity;
	}

	@Override
	public void configure(FaultTolerantTimerTaskFactory taskFactory, int threads)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException {
		if (isConfigured()) {
			throw new IllegalStateException();
		}
		if (threads < 0) {
			throw new IllegalArgumentException();
		}
		this.scheduler = new FaultTolerantScheduler("fts-raentity-" + raEntity,
				threads, sleeContainer.getClusterFactory(), (byte) 0, sleeContainer
						.getTransactionManager().getRealTransactionManager(),
				new FaultTolerantTimerTaskFactoryWrapper(taskFactory));
	}

	@Override
	public boolean isConfigured() {
		return scheduler != null;
	}

	@Override
	public void schedule(FaultTolerantTimerTask task, long delay, TimeUnit unit) {
		if (!isConfigured()) {
			throw new IllegalStateException();
		}
		if (task == null) {
			throw new NullPointerException("null task");
		}
		long delayMs = unit.toMillis(delay);
		long startTime = System.currentTimeMillis() + delayMs;
		FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(
				task.getTaskData(), startTime, -1, null);
		FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(
				task, data);
		scheduler.schedule(taskWrapper);
	}

	@Override
	public void scheduleAtFixedRate(FaultTolerantTimerTask task,
			long initialDelay, long period, TimeUnit unit) {
		if (!isConfigured()) {
			throw new IllegalStateException();
		}
		if (task == null) {
			throw new NullPointerException("null task");
		}
		if (period < 0) {
			throw new IllegalArgumentException();
		}
		long initialDelayMs = unit.toMillis(initialDelay);
		long periodMs = unit.toMillis(period);
		long startTime = System.currentTimeMillis() + initialDelayMs;
		FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(
				task.getTaskData(), startTime, periodMs,
				PeriodicScheduleStrategy.atFixedRate);
		FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(
				task, data);
		scheduler.schedule(taskWrapper);

	}

	@Override
	public void scheduleWithFixedDelay(FaultTolerantTimerTask task,
			long initialDelay, long delay, TimeUnit unit) {
		if (!isConfigured()) {
			throw new IllegalStateException();
		}
		if (task == null) {
			throw new NullPointerException("null task");
		}
		if (delay < 0) {
			throw new IllegalArgumentException();
		}
		long initialDelayMs = unit.toMillis(initialDelay);
		long delayMs = unit.toMillis(delay);
		long startTime = System.currentTimeMillis() + initialDelayMs;
		FaultTolerantTimerTaskDataWrapper data = new FaultTolerantTimerTaskDataWrapper(
				task.getTaskData(), startTime, delayMs,
				PeriodicScheduleStrategy.withFixedDelay);
		FaultTolerantTimerTaskWrapper taskWrapper = new FaultTolerantTimerTaskWrapper(
				task, data);
		scheduler.schedule(taskWrapper);
	}

	@Override
	public void cancel(Serializable taskID) {
		if (!isConfigured()) {
			throw new IllegalStateException();
		}
		if (taskID == null) {
			throw new NullPointerException("null task id");
		}
		scheduler.cancel(taskID);
	}

	public void shutdown() {
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
	}

}
