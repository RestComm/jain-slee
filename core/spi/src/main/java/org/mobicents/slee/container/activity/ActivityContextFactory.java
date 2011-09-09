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

/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.util.Set;

import javax.slee.resource.ActivityAlreadyExistsException;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public interface ActivityContextFactory extends SleeContainerModule {

	/**
	 * 
	 * @param activityContext
	 * @param activityFlags
	 * @throws ActivityAlreadyExistsException
	 */
	public ActivityContext createActivityContext(ActivityContextHandle ach,
			int activityFlags) throws ActivityAlreadyExistsException;

	/**
	 * Retrieves the {@link ActivityContext} for the specified
	 * {@link ActivityContextHandle}.
	 * 
	 * @param ach
	 * @return null if no such activity context exists
	 */
	public ActivityContext getActivityContext(ActivityContextHandle ach);
	
	/**
	 * Retrieves the {@link ActivityContext} for the specified
	 * {@link ActivityContextHandle}.
	 * 
	 * @param ach
	 * @param updateLastAccessTime
	 *            indicates if an update of last access time is needed. Note
	 *            that this doesn't mean an update will be done, it may depend
	 *            if activity idleness checks are on, the activity type or
	 *            simply when last update was done.
	 * @return null if no such activity context exists
	 */
	public ActivityContext getActivityContext(ActivityContextHandle ach,
			boolean updateLastAccessTime);

	/**
	 * @return Set of all registered SLEE activity context handles
	 */
	public Set<ActivityContextHandle> getAllActivityContextsHandles();

	/**
	 * @return
	 */
	public int getActivityContextCount();

	/**
	 * Indicates if the activity context exists in SLEE.
	 * 
	 * @param ach
	 * @return
	 */
	public boolean activityContextExists(ActivityContextHandle ach);

}
