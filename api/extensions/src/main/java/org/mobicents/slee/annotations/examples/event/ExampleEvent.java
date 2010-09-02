package org.mobicents.slee.annotations.examples.event;

import java.util.UUID;

import org.mobicents.slee.annotations.EventType;
import org.mobicents.slee.annotations.LibraryRef;

@EventType(name=ExampleEvent.EVENT_TYPE_NAME,
		vendor=ExampleEvent.EVENT_TYPE_VENDOR,
		version=ExampleEvent.EVENT_TYPE_VERSION,
		libraryRefs={@LibraryRef(name="ExampleLibrary",vendor="javax.slee",version="1.0")})
public class ExampleEvent {

	public static final String EVENT_TYPE_NAME = "ExampleEvent";
	public static final String EVENT_TYPE_VENDOR = "javax.slee";
	public static final String EVENT_TYPE_VERSION = "1.0";
	
	private final String id = UUID.randomUUID().toString();

	private final Object eventData;
	
	public ExampleEvent(Object eventData) {
		this.eventData = eventData;
	}
	
	public Object getEventData() {
		return eventData;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExampleEvent other = (ExampleEvent) obj;
		return this.id.equals(other.id);
	}
	
}
