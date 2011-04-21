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
