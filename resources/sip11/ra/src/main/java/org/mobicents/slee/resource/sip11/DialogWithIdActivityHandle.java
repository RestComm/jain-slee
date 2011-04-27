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

import net.java.slee.resource.sip.DialogActivity;

/**
 * The {@link SipActivityHandle} for {@link DialogActivity}
 * 
 * @author martins
 * 
 */
public class DialogWithIdActivityHandle extends MarshableSipActivityHandle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Class<? extends SipActivityHandle> TYPE = DialogWithIdActivityHandle.class;
	
	/**
	 * the dialog's id
	 */
	private String dialogId;

	/**
	 * 
	 * @param dialogId
	 */
	public DialogWithIdActivityHandle(String dialogId) {
		if (dialogId == null) {
			throw new NullPointerException("null dialogId");
		}		
		this.dialogId = dialogId;		
	}

	/**
	 * Retrieves the dialog's id.
	 * 
	 * @return
	 */
	public String getDialogId() {
		return dialogId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.MarshableSipActivityHandle#getEstimatedHandleSize()
	 */
	@Override
	public int getEstimatedHandleSize() {
		return dialogId.length() + 3;
	}
	
	@Override
	public boolean isReplicated() {
		return true;
	}
	
	@Override
	public int hashCode() {
		return dialogId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DialogWithIdActivityHandle other = (DialogWithIdActivityHandle) obj;
		if (!dialogId.equals(other.dialogId)) {
			return false;
		}		
		return true;
	}

	@Override
	public String toString() {
		return dialogId;
	}
		
}
