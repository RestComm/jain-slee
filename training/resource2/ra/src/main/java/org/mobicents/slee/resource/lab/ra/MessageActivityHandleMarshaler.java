package org.mobicents.slee.resource.lab.ra;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;

public class MessageActivityHandleMarshaler implements Marshaler {

	@Override
	public int getEstimatedEventSize(FireableEventType arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEstimatedHandleSize(ActivityHandle arg0) {
		MessageActivityHandle mah = (MessageActivityHandle) arg0;

		return mah.getHandle().getBytes().length;
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
	public void marshalHandle(ActivityHandle arg0, DataOutput arg1)
			throws IOException {
		MessageActivityHandle mah = (MessageActivityHandle) arg0;
		arg1.writeUTF(mah.getHandle());

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
	public ActivityHandle unmarshalHandle(DataInput arg0) throws IOException {
		MessageActivityHandle mah = new MessageActivityHandle(arg0.readUTF());
		return mah;

	}

}
