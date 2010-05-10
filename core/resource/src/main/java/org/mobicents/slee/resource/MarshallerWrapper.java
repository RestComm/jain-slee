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
