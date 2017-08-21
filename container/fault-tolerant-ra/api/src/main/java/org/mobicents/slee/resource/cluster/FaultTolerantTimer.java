/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A Fault Tolerant timer that resembles the {@link ScheduledExecutorService}.
 * There are two fundamental changes in the interfaces:
 * 
 * 1) Tasks must follow a specific interface, to ensure that the scheduler is
 * able to replicate its data, and fail over the task if needed.
 * 
 * 2) Cancellation of task is done through this interface, not through
 * {@link ScheduledFuture} objects, this allows the operation to be easily done
 * in any cluster node.
 * 
 * @author martins
 * 
 */
public interface FaultTolerantTimer {

	/**
	 * Requests the cancellation of the FT Timer Task with the specified ID.
	 * 
	 * @param taskID
	 *            the ID of the timer task to cancel
	 * @throws NullPointerException
	 *             if task is null
	 * @throws IllegalStateException
	 *             if the timer is not configured.
	 * 
	 */
	public void cancel(Serializable taskID) throws IllegalStateException,
			NullPointerException;

	/**
	 * Configures the fault tolerant timer, specifying the timer task factory
	 * and the number of threads the timer uses to execute tasks.
	 * 
	 * This method may be invoked only once, and before any task is scheduled,
	 * 
	 * @param taskFactory
	 *            the timer task factory
	 * @param threads
	 *            the number of threads used by the timer, to execute tasks.
	 * @throws IllegalStateException
	 *             if the timer is already configured.
	 * @throws IllegalArgumentException
	 *             if the threads parameter is a negative number.
	 * @throws NullPointerException
	 *             if the task factory is null.
	 */
	public void configure(FaultTolerantTimerTaskFactory taskFactory, int threads)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException;

	/**
	 * Indicates if the timer is configured.
	 * 
	 * @return
	 */
	public boolean isConfigured();

	/**
	 * Creates and executes a one-shot action that becomes enabled after the
	 * given delay.
	 * 
	 * @param task
	 *            the task to execute
	 * @param delay
	 *            the time from now to delay execution
	 * @param unit
	 *            the time unit of the delay parameter
	 * @throws NullPointerException
	 *             if task is null
	 * @throws IllegalStateException
	 *             if the timer is not configured.
	 */
	public void schedule(FaultTolerantTimerTask task, long delay, TimeUnit unit)
			throws IllegalStateException, NullPointerException;

	/**
	 * Creates and executes a periodic action that becomes enabled first after
	 * the given initial delay, and subsequently with the given period; that is
	 * executions will commence after <tt>initialDelay</tt> then
	 * <tt>initialDelay+period</tt>, then <tt>initialDelay + 2 * period</tt>,
	 * and so on. If any execution of the task encounters an exception,
	 * subsequent executions are suppressed. Otherwise, the task will only
	 * terminate via cancellation or termination of the executor. If any
	 * execution of this task takes longer than its period, then subsequent
	 * executions may start late, but will not concurrently execute.
	 * 
	 * @param task
	 *            the task to execute
	 * @param initialDelay
	 *            the time to delay first execution
	 * @param period
	 *            the period between successive executions
	 * @param unit
	 *            the time unit of the initialDelay and period parameters
	 * @throws NullPointerException
	 *             if task is null
	 * @throws IllegalArgumentException
	 *             if period less than or equal to zero
	 * @throws IllegalStateException
	 *             if the timer is not configured.
	 */
	public void scheduleAtFixedRate(FaultTolerantTimerTask task,
			long initialDelay, long period, TimeUnit unit)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException;

	/**
	 * Creates and executes a periodic action that becomes enabled first after
	 * the given initial delay, and subsequently with the given delay between
	 * the termination of one execution and the commencement of the next. If any
	 * execution of the task encounters an exception, subsequent executions are
	 * suppressed. Otherwise, the task will only terminate via cancellation or
	 * termination of the executor.
	 * 
	 * @param task
	 *            the task to execute
	 * @param initialDelay
	 *            the time to delay first execution
	 * @param delay
	 *            the delay between the termination of one execution and the
	 *            commencement of the next
	 * @param unit
	 *            the time unit of the initialDelay and delay parameters
	 * @throws NullPointerException
	 *             if task is null
	 * @throws IllegalArgumentException
	 *             if delay less than or equal to zero
	 * @throws IllegalStateException
	 *             if the timer is not configured.
	 */
	public void scheduleWithFixedDelay(FaultTolerantTimerTask task,
			long initialDelay, long delay, TimeUnit unit)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException;

}
