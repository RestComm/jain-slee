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

import org.restcomm.timers.TimerTask;

/**
 * Wrapps the FT RA timer task into the task used by Restcomm Cluster
 * framework.
 * 
 * @author martins
 * 
 */
public class FaultTolerantTimerTaskWrapper extends TimerTask {

	/**
	 * 
	 */
	private final FaultTolerantTimerTask wrappedTask;

	/**
	 * 
	 * @param wrappedTask
	 * @param data
	 */
	public FaultTolerantTimerTaskWrapper(FaultTolerantTimerTask wrappedTask,
			FaultTolerantTimerTaskDataWrapper data) {
		super(data);
		this.wrappedTask = wrappedTask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restcomm.timers.TimerTask#runTask()
	 */
	@Override
	public void runTask() {
		wrappedTask.run();
	}

}
