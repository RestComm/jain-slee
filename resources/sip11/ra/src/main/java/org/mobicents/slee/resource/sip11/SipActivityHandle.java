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

package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * Base class for SIP RA activity handles, which provides a link to the related activity object.
 * 
 * @author martins
 *
 */
public abstract class SipActivityHandle implements ActivityHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @return
	 */
	public abstract boolean isReplicated();
	
	/**
	 * the activity related with the handle
	 */
	private transient Wrapper activity;

	/**
	 * Retrieves the activity related with the handle. 
	 * @return
	 */
	public Wrapper getActivity() {
		return activity;
	}

	/**
	 * Sets the activity related with the handle.
	 * @param activity
	 */
	public void setActivity(Wrapper activity) {
		this.activity = activity;
	}
	
}
