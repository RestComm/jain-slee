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

/**
 * 
 */
package org.mobicents.slee.resource.sip11;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;

/**
 * @author martins
 * 
 */
public class SipMarshaler implements javax.slee.resource.Marshaler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.Marshaler#getEstimatedEventSize(javax.slee.resource
	 * .FireableEventType, java.lang.Object)
	 */
	public int getEstimatedEventSize(FireableEventType arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.Marshaler#getEstimatedHandleSize(javax.slee.resource
	 * .ActivityHandle)
	 */
	public int getEstimatedHandleSize(ActivityHandle arg0) {
		return ((MarshableSipActivityHandle) arg0).getEstimatedHandleSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.Marshaler#getEventBuffer(javax.slee.resource.
	 * FireableEventType, java.lang.Object)
	 */
	public ByteBuffer getEventBuffer(FireableEventType arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.Marshaler#marshalEvent(javax.slee.resource.
	 * FireableEventType, java.lang.Object, java.io.DataOutput)
	 */
	public void marshalEvent(FireableEventType arg0, Object arg1,
			DataOutput arg2) throws IOException {
		throw new UnsupportedOperationException();
	}

	private static final Class<?> DialogWithIdActivityHandle_TYPE = DialogWithIdActivityHandle.class;
	private static final Class<?> DialogWithoutIdActivityHandle_TYPE = DialogWithoutIdActivityHandle.class;

	private static final byte dialogWithIdActivityHandle = 0;
	private static final byte dialogWithoutIdActivityHandle = 1;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.Marshaler#marshalHandle(javax.slee.resource.
	 * ActivityHandle, java.io.DataOutput)
	 */
	public void marshalHandle(ActivityHandle arg0, DataOutput arg1)
			throws IOException {

		final Class<?> handleType = arg0.getClass();

		if (handleType == DialogWithIdActivityHandle_TYPE) {
			arg1.writeByte(dialogWithIdActivityHandle);
			final DialogWithIdActivityHandle handle = (DialogWithIdActivityHandle) arg0;
			arg1.writeUTF(handle.getDialogId());
		} else if (handleType == DialogWithoutIdActivityHandle_TYPE) {
			arg1.writeByte(dialogWithoutIdActivityHandle);
			final DialogWithoutIdActivityHandle handle = (DialogWithoutIdActivityHandle) arg0;
			arg1.writeUTF(handle.getCallId());
			arg1.writeUTF(handle.getLocalTag());
		} else {
			throw new IOException("unknown activity handle type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.resource.Marshaler#releaseEventBuffer(javax.slee.resource.
	 * FireableEventType, java.lang.Object, java.nio.ByteBuffer)
	 */
	public void releaseEventBuffer(FireableEventType arg0, Object arg1,
			ByteBuffer arg2) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.slee.resource.Marshaler#unmarshalEvent(javax.slee.resource.
	 * FireableEventType, java.io.DataInput)
	 */
	public Object unmarshalEvent(FireableEventType arg0, DataInput arg1)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.resource.Marshaler#unmarshalHandle(java.io.DataInput)
	 */
	public ActivityHandle unmarshalHandle(DataInput arg0) throws IOException {

		final byte handleType = arg0.readByte();

		if (handleType == dialogWithIdActivityHandle) {
			final String dialogId = arg0.readUTF();
			return new DialogWithIdActivityHandle(dialogId);
		} 
		else {
			final String callId = arg0.readUTF();
			final String localTag = arg0.readUTF();
			return new DialogWithoutIdActivityHandle(callId, localTag);
		}
	}

}
