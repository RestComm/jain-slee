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

/**
 * 
 */
package org.mobicents.slee.container.eventrouter;

import java.util.concurrent.ExecutionException;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;

/**
 * An {@link EventRouter} executor, used to route events and execute misc tasks
 * for multiple {@link ActivityContextHandle} at the same time, but in a
 * serialized way.
 * 
 * @author martins
 * 
 */
public interface EventRouterExecutor {

	/**
	 * Indicates to the executor that it was mapped to the activity with the
	 * specified handle.
	 * 
	 * @param ach
	 */
	public void activityMapped(ActivityContextHandle ach);
	
	/**
	 * Indicates to the executor that it was unmapped to the activity with the
	 * specified handle.
	 * 
	 * @param ach
	 */
	public void activityUnmapped(ActivityContextHandle ach);
	
	/**
	 * Executes a misc {@link Runnable} task.
	 * 
	 * @param task
	 */
	public void execute(Runnable task);

	/**
	 * Executes a misc {@link Runnable} task, blocking till execution ends.
	 * 
	 * @param task
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void executeNow(Runnable task) throws InterruptedException, ExecutionException;
	
	/**
	 * Retrieves the performance and load statistics for the executor.
	 * 
	 * @return null if the executor is not collecting stats.
	 */
	public EventRouterExecutorStatistics getStatistics();

	/**
	 * Routes the specified event.
	 * @param event
	 */
	public void routeEvent(EventContext event);

	/**
	 * Shuts down the executor.
	 */
	public void shutdown();

}