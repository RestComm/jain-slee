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

package org.mobicents.slee.resource;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;

public class MarshallerWrapper implements Marshaler {

	private final Marshaler marshaler;
	private final ResourceAdaptorEntityImpl raEntity;
	
	public MarshallerWrapper(Marshaler marshaler,ResourceAdaptorEntityImpl raEntity) {
		this.marshaler = marshaler;
		this.raEntity = raEntity;
	}

	public int getEstimatedEventSize(FireableEventType eventType, Object event) {
		return marshaler.getEstimatedEventSize(eventType, event);
	}

	public int getEstimatedHandleSize(ActivityHandle handle) {
		return marshaler.getEstimatedHandleSize(raEntity.derreferActivityHandle(handle));
	}

	public ByteBuffer getEventBuffer(FireableEventType eventType, Object event) {
		return marshaler.getEventBuffer(eventType, event);
	}

	public void marshalEvent(FireableEventType eventType, Object event,
			DataOutput out) throws IOException {
		marshaler.marshalEvent(eventType, event, out);		
	}

	public void marshalHandle(ActivityHandle handle, DataOutput out)
			throws IOException {
		marshaler.marshalHandle(raEntity.derreferActivityHandle(handle), out);
	}

	public void releaseEventBuffer(FireableEventType eventType, Object event,
			ByteBuffer buffer) {
		marshaler.releaseEventBuffer(eventType, event, buffer);
	}

	public Object unmarshalEvent(FireableEventType eventType, DataInput in)
			throws IOException {
		return marshaler.unmarshalEvent(eventType, in);
	}

	public ActivityHandle unmarshalHandle(DataInput in) throws IOException {
		final ActivityHandle ah = marshaler.unmarshalHandle(in);
		if (raEntity.getHandleReferenceFactory() == null)  {
			return ah;
		}
		else {
			return raEntity.getHandleReferenceFactory().getReferenceTransacted(ah);
		}
	}
	
}
