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

import java.util.concurrent.atomic.AtomicInteger;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;

/**
 * Round Robin {@link EventRouterExecutor} to {@link ActivityContextHandle} mapping.
 * @author martins
 *
 */
public class RoundRobinEventRouterExecutorMapper extends AbstractEventRouterExecutorMapper {

	/**
	 * index use to iterate the executor's array
	 */
	protected AtomicInteger index = null;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.mapping.AbstractEventRouterExecutorMapper#setExecutors(org.mobicents.slee.runtime.eventrouter.EventRouterExecutor[])
	 */
	@Override
	public void setExecutors(EventRouterExecutor[] executors) {
		super.setExecutors(executors);
		//reset index
		index = new AtomicInteger(0);
	}
	
	/**
	 * Computes the index of the next executor to retrieve. Adaptation of the {@link AtomicInteger} incrementAndGet() code.
	 *  
	 * @return
	 */
	private int getNextIndex() {
		for (;;) {
            int current = index.get();
            int next = (current == executors.length ? 1 : current + 1);
            if (index.compareAndSet(current, next))
                return next-1;
        }
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.mapping.AbstractEventRouterExecutorMapper#getExecutor(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	@Override
	public EventRouterExecutor getExecutor(
			ActivityContextHandle activityContextHandle) {
		return executors[getNextIndex()];
	}

}
