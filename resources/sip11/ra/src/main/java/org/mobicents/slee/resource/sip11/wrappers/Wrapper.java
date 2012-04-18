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

package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Dialog;
import javax.slee.Address;

import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public abstract class Wrapper {
	
	protected SipActivityHandle activityHandle;
	protected SipResourceAdaptor ra;
	
	protected boolean ending;
	
	/**
	 * 
	 * @param activityHandle
	 */
	public Wrapper(SipActivityHandle activityHandle, SipResourceAdaptor ra) {
		this.activityHandle = activityHandle;
		this.activityHandle.setActivity(this);
		this.ra = ra;
	}
	
	/**
	 * Indicates if the wrapper is a {@link Dialog}
	 * @return
	 */
	public abstract boolean isDialog();

	/**
	 * Indicates if the wrapper is an ack dummy transaction.
	 * @return
	 */
	public abstract boolean isAckTransaction();
	
	/**
	 * Retrieves the handle associated with the activity.
	 * @return
	 */
	public SipActivityHandle getActivityHandle() {
		return activityHandle;
	}
	
	/**
	 * Retrieves the slee {@link Address} where events on this resource are fired.
	 * @return
	 */
	public abstract Address getEventFiringAddress();
	
	/**
	 * In case the wrapper is an activity, it indicates if it is ending, otherwise it doesn't have a meaning.
	 * @return
	 */
	public boolean isEnding() {
		return ending;
	}
	
	/**
	 * Indicates this activity is ending.
	 */
	public void ending() {
		this.ending = true;
	}	
	
	/**
	 * Operation forbidden in the sip interfaces implemented by concrete wrappers.
	 * @return
	 */
	public Object getApplicationData() {
		throw new SecurityException();
	}

	/**
	 * Operation forbidden in the sip interfaces implemented by concrete wrappers.
	 * @param arg0
	 */
	public void setApplicationData(Object arg0) {
		throw new SecurityException();
	}
	
	public void clear() {
		if (activityHandle != null) {
			activityHandle.setActivity(null);
		}		
	}
	
	public SipResourceAdaptor getRa() {
		return ra;
	}
}
