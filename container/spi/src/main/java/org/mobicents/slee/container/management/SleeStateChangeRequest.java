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
