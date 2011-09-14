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

package org.mobicents.slee.resource.jdbc;

import org.mobicents.slee.resource.jdbc.task.JdbcTask;

/**
 * Interface for a JDBC Resource Adaptor Activity Object. It executes jdbc tasks
 * asynchronously.
 * 
 * @author martins
 * 
 */
public interface JdbcActivity {

	/**
	 * Executes the specified task. An exception event will be fired if the task
	 * execution throws an exception.
	 * 
	 * @param task
	 */
	void execute(JdbcTask task);

	/**
	 * Requests the activity end.
	 */
	public void endActivity();

    /**
     * Get the name of the entity that owns this activity.
     */
    public String getRaEntityName();

}
