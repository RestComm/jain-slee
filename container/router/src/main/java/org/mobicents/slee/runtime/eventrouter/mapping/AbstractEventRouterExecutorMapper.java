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
package org.mobicents.slee.runtime.eventrouter.mapping;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRouterExecutorMapper;

/**
 * 
 * @author martins
 * 
 */
public abstract class AbstractEventRouterExecutorMapper implements
		EventRouterExecutorMapper {

	protected EventRouterExecutor[] executors;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.mapping.EventRouterExecutorMapper
	 * #getExecutor(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public abstract EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.mapping.EventRouterExecutorMapper
	 * #setExecutors
	 * (org.mobicents.slee.runtime.eventrouter.EventRouterExecutor[])
	 */
	public void setExecutors(EventRouterExecutor[] executors) {
		this.executors = executors;
	}

}
