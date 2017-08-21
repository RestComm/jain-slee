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
