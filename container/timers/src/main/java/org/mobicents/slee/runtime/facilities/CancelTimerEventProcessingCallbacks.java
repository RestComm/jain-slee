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
package org.mobicents.slee.runtime.facilities;

import org.mobicents.slee.container.event.EventUnreferencedCallback;

/**
 * Cancels the timer after the event is routed.
 * @author martins
 *
 */
public class CancelTimerEventProcessingCallbacks implements EventUnreferencedCallback {

	final TimerFacilityTimerTask  task;
		
	/**
	 * @param timerFacilityImpl
	 * @param timerID
	 */
	public CancelTimerEventProcessingCallbacks(TimerFacilityTimerTask  task) {
		this.task = task;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#acceptsTransaction()
	 */
	public boolean requiresTransaction() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#eventUnreferenced()
	 */
	public void eventUnreferenced() {
		task.remove();			
	}
}
