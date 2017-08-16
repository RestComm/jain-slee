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

package org.mobicents.slee;

import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerID;

/**
 * JAIN SLEE 1.1 {@link ActivityContextInterface} Extension.
 * 
 * @author martins
 * 
 */
public interface ActivityContextInterfaceExt extends ActivityContextInterface {

	/**
	 * Retrieves the IDs of timers currently set in the
	 * {@link ActivityContextInterfaceExt}.
	 * 
	 * @return an array with the {@link TimerID}s.
	 */
	public TimerID[] getTimers();

	/**
	 * Retrieves the names currently bound to the
	 * {@link ActivityContextInterfaceExt}.
	 * 
	 * @return an array with the names bound.
	 */
	public String[] getNamesBound();

	/**
	 * Suspends the routing of events on the activity context, till the current
	 * transaction ends.
	 * 
	 * @throws javax.slee.TransactionRequiredLocalException
	 *             if it is invoked without a valid transaction context.
	 * @throws javax.slee.SLEEException
	 *             if the requested operation cannot be performed due to a
	 *             system-level failure.
	 */
	public void suspend() throws javax.slee.TransactionRequiredLocalException,
			javax.slee.SLEEException;

}
