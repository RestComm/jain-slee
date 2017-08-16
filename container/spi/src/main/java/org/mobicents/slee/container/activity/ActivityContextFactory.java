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
	 * {@link ActivityContextHandle}. Same as getActivityContext(ach,false).
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
	 * Retrieves the {@link ActivityContext} for the specified string id.
	 * Same as getActivityContext(sid,false).
	 * 
	 * @param sid
	 * @return null if no such activity context exists
	 */
	public ActivityContext getActivityContext(String sid);
	
	/**
	 * Retrieves the {@link ActivityContext} for the specified string id.
	 * 
	 * @param sid
	 * @param updateLastAccessTime
	 *            indicates if an update of last access time is needed. Note
	 *            that this doesn't mean an update will be done, it may depend
	 *            if activity idleness checks are on, the activity type or
	 *            simply when last update was done.
	 * @return null if no such activity context exists
	 */
	public ActivityContext getActivityContext(String sid,
			boolean updateLastAccessTime);

	/**
	 * @return Set of all registered SLEE activity context handles
	 */
	public Set<ActivityContextHandle> getAllActivityContextsHandles();
	
	/**
	 * @return Set of all registered SLEE activity context handles by activity type
	 */
	public Set<ActivityContextHandle> getAllActivityContextsHandles(ActivityType type);

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
