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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.sip.Dialog;

import net.java.slee.resource.sip.DialogActivity;

/**
 * The {@link SipActivityHandle} for {@link DialogActivity} related with a
 * {@link Dialog} that does not exist yet.
 * 
 * @author martins
 * 
 */
public class DialogWithoutIdActivityHandle extends MarshableSipActivityHandle
		implements Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the dialog's call id
	 */
	private String callId;

	/**
	 * the dialog's local tag
	 */
	private String localTag;

	public DialogWithoutIdActivityHandle() {

	}

	/**
	 * 
	 * @param callID
	 * @param localTag
	 * @param remoteTag
	 */
	public DialogWithoutIdActivityHandle(String callId, String localTag) {
		if (callId == null) {
			throw new NullPointerException("null call id");
		}
		if (localTag == null) {
			throw new NullPointerException("null local tag");
		}
		this.callId = callId;
		this.localTag = localTag;
	}

	/**
	 * Retrieves the dialog's call id.
	 * 
	 * @return
	 */
	public String getCallId() {
		return callId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.sip11.MarshableSipActivityHandle#
	 * getEstimatedHandleSize()
	 */
	@Override
	public int getEstimatedHandleSize() {
		return callId.length() + localTag.length() + 7;
	}

	@Override
	public boolean isReplicated() {
		return true;
	}

	/**
	 * Retrieves the dialog's local tag
	 * 
	 * @return
	 */
	public String getLocalTag() {
		return localTag;
	}

	@Override
	public int hashCode() {
		return callId.hashCode() * 31 + localTag.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DialogWithoutIdActivityHandle other = (DialogWithoutIdActivityHandle) obj;
		if (!callId.equals(other.callId)) {
			return false;
		}
		if (!localTag.equals(other.localTag)) {
			return false;
		}
		return true;
	}

	public static final char DIALOG_ID_SEPARATOR = ':';
	private static final String REMOTE_TAG = ":null";

	public static final Class<? extends SipActivityHandle> TYPE = DialogWithoutIdActivityHandle.class;

	@Override
	public String toString() {
		return new StringBuilder(callId).append(DIALOG_ID_SEPARATOR)
				.append(localTag).append(REMOTE_TAG).toString();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(callId);
		out.writeUTF(localTag);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		callId = in.readUTF();
		localTag = in.readUTF();
	}

}
