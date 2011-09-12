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

package org.mobicents.slee.resource.jdbc.event;

import javax.slee.EventTypeID;

import org.mobicents.slee.resource.jdbc.task.JdbcTask;

/**
 * An event which provides the {@link Throwable} which occurred when executing a
 * {@link JdbcTask}.
 * 
 * @author martins
 * 
 */
public interface JdbcTaskExecutionThrowableEvent {

	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID(
			JdbcTaskExecutionThrowableEvent.class.getSimpleName(),
			"org.mobicents", "1.0");

	/**
	 * Retrieves the {@link Throwable} thrown when executing.
	 * 
	 * @return
	 */
	public Throwable getThrowable();

	/**
	 * Retrieves the task which execution thrown an exception.
	 * 
	 * @return
	 */
	public JdbcTask getTask();

}
