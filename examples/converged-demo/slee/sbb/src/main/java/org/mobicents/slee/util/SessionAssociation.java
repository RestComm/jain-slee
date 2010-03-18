 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.util;

import java.io.Serializable;

public class SessionAssociation implements Serializable {

	private static final long serialVersionUID = 3618984494431088949L;

	private String state;

	private Session callerSession;

	private Session calleeSession;
	// This member is used to communicate state changes to components external to JSLEE
	private StateCallback stateCallback;
	
	public SessionAssociation(String state) {
		this.state = state;
	}

	public Session getSession(String callId) {
		Session retVal = null;
		if ( callId == null ) {
			throw new IllegalArgumentException("Argument callId must not be null");
		}
		if (callId.equals(callerSession.getCallId()))
			retVal = callerSession;
		else
			retVal = calleeSession;
		return retVal;
	}

	public Session getPeerSession(String callId) {
		Session retVal = null;
		if (calleeSession.getCallId().equals(callId))
			retVal = callerSession;
		else
			retVal = calleeSession;
		return retVal;
	}

	public Session getCalleeSession() {
		return calleeSession;
	}

	public void setCalleeSession(Session calleeSession) {
		this.calleeSession = calleeSession;
	}

	public Session getCallerSession() {
		return callerSession;
	}

	public void setCallerSession(Session callerSession) {
		this.callerSession = callerSession;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String toString() {
		return "SessionAssociation: [state:" + state + ", callerSession:"
				+ callerSession + ", calleeSession:" + calleeSession + "]";
	}

	public StateCallback getStateCallback() {
		return stateCallback;
	}

	public void setStateCallback(StateCallback stateCallback) {
		this.stateCallback = stateCallback;
	}

}
