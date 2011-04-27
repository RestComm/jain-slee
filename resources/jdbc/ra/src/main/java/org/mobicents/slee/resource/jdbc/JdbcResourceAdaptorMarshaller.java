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

package org.mobicents.slee.resource.jdbc;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;

public class JdbcResourceAdaptorMarshaller implements Marshaler {

	private final JdbcResourceAdaptor ra;
	
	public JdbcResourceAdaptorMarshaller(JdbcResourceAdaptor ra) {
		this.ra = ra;
	}
	
	@Override
	public int getEstimatedEventSize(FireableEventType arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEstimatedHandleSize(ActivityHandle handle) {
		return ((JdbcActivityImpl) handle).getId().length();
	}

	@Override
	public ByteBuffer getEventBuffer(FireableEventType arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void marshalEvent(FireableEventType arg0, Object arg1,
			DataOutput arg2) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void marshalHandle(ActivityHandle handle, DataOutput out)
			throws IOException {
		out.writeUTF(((JdbcActivityImpl) handle).getId());
	}

	@Override
	public void releaseEventBuffer(FireableEventType arg0, Object arg1,
			ByteBuffer arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object unmarshalEvent(FireableEventType arg0, DataInput arg1)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ActivityHandle unmarshalHandle(DataInput in) throws IOException {
		return new JdbcActivityImpl(ra,in.readUTF());
	}

}
