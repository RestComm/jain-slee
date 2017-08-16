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
import org.restcomm.timers.TimerTaskData;
import org.restcomm.timers.TimerTaskFactory;

/**
 * Wrapps the FT RA Timer task factory into the factory used by Mobicents
 * Cluster framework.
 * 
 * @author martins
 * 
 */
public class FaultTolerantTimerTaskFactoryWrapper implements TimerTaskFactory {

	/**
	 * 
	 */
	private final FaultTolerantTimerTaskFactory taskFactory;

	/**
	 * 
	 * @param taskFactory
	 */
	public FaultTolerantTimerTaskFactoryWrapper(
			FaultTolerantTimerTaskFactory taskFactory) {
		this.taskFactory = taskFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.restcomm.timers.TimerTaskFactory#newTimerTask(org.restcomm.timers
	 * .TimerTaskData)
	 */
	@Override
	public TimerTask newTimerTask(TimerTaskData data) {
		FaultTolerantTimerTaskDataWrapper dataWrapper = (FaultTolerantTimerTaskDataWrapper) data;
		FaultTolerantTimerTask task = taskFactory.getTask(dataWrapper
				.getWrappedData());
		return new FaultTolerantTimerTaskWrapper(task, dataWrapper);
	}

}
