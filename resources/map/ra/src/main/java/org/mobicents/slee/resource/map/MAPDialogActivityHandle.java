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

package org.mobicents.slee.resource.map;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPDialogActivityHandle implements Serializable, ActivityHandle {

	private long dialogId;
	private transient MAPDialogWrapper activity;
	

	/**
	 * @param dialogId
	 */
	public MAPDialogActivityHandle(long dialogId) {
		super();
		this.dialogId = dialogId;
	}

	public long getDialogId() {
		return this.dialogId;
	}


	public MAPDialogWrapper getActivity() {
		return activity;
	}

	public void setActivity(MAPDialogWrapper activity) {
		this.activity = activity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (dialogId ^ (dialogId >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MAPDialogActivityHandle other = (MAPDialogActivityHandle) obj;
		if (dialogId != other.dialogId)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "MAPDialogActivityHandle(id=" + this.dialogId + ")";
	}
}
