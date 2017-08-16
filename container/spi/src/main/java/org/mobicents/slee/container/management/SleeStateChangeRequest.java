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

package org.mobicents.slee.container.management;

import javax.slee.management.SleeState;

/**
 * A request to change SLEE state.
 * 
 * @author martins
 *
 */
public interface SleeStateChangeRequest {

	/**
	 * The state to set
	 * @return
	 */
	public SleeState getNewState();
	
	/**
	 * Indicates if the container operations, which result from state change, should be completed before returning from state change request.
	 * @return
	 */
	public boolean isBlockingRequest();
	
	/**
	 * Indicates that the state has now changed.
	 * @param oldState
	 */
	public void stateChanged(SleeState oldState);
	
	/**
	 * Indicates that the whole process of state change completed.
	 */
	public void requestCompleted();
	
}
