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

package org.mobicents.slee.container.management.jmx;

import javax.slee.EventTypeID;
import javax.slee.management.ManagementException;

public interface EventRouterStatisticsMBean {

    public static final String OBJECT_NAME = "org.mobicents.slee:name=EventRouterStatistics";
    /**
     * Return number of all activities mapped to executors.
     */
	public int getActivitiesMapped() throws ManagementException;
	/**
	 * Return number of activities mapped to executor with passed index.
	 */
	public int getActivitiesMapped(int executor) throws ManagementException;
	/**
	 * Return avg routing time within container. This stat includes all executors.
	 */
	public long getAverageEventRoutingTime() throws ManagementException;
	/**
	 * Retrun avg routing time of particular event type. This includes routing in all executors.
	 */
	public long getAverageEventRoutingTime(EventTypeID eventTypeID) throws ManagementException;
	/**
	 * Return avg routing time for executor with passed index.
	 */
	public long getAverageEventRoutingTime(int executor) throws ManagementException;
	/**
	 * Return avg routing time for particular event type in specific executor.
	 */
	public long getAverageEventRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;
	/**
	 * Return number of events routed of this event type.
	 */
	public long getEventsRouted(EventTypeID eventTypeID) throws ManagementException;
	/**
	 * Return number of events routed of this event type in particular executor..
	 */
	public long getEventsRouted(int executor, EventTypeID eventTypeID) throws ManagementException;
	/**
	 * Return number of all tasks executed in executors, this includes event routing and misc tasks.
	 */
	public long getExecutedTasks() throws ManagementException;
	/**
	 * Return number of all tasks executed in particular executor, this includes event routing and misc tasks .
	 */
	public long getExecutedTasks(int executor) throws ManagementException;
	/**
	 * Return time spent on executing tasks in executor.
	 */
	public long getExecutingTime(int executor) throws ManagementException;

	public long getIdleTime(int executor) throws ManagementException;
	/**
	 * Return number of misc tasks executed, that is all tasks not related to event routing.
	 */
	public long getMiscTasksExecuted() throws ManagementException;
	/**
	 * Return number of misc tasks executed in particular executor, that is all tasks not related to event routing.
	 */
	public long getMiscTasksExecuted(int executor) throws ManagementException;
	/**
	 * Return time spent for execution of misc tasks in executor(total time).
	 */
	public long getMiscTasksExecutingTime(int executor) throws ManagementException;
	/**
	 * Return time spent on routing particular eventType in executor(total time).
	 */
	public long getRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;

	public String printAllStats() throws ManagementException;
}
