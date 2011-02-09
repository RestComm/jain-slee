package org.mobicents.slee.resource;

import javax.slee.EventTypeID;
import javax.slee.resource.FireableEventType;

/**
 * Implementation of the SLEE 1.1 specs {@link FireableEventType} class.
 * @author martins
 *
 */
public class FireableEventTypeImpl implements FireableEventType {

	/**
	 * the event's class loader, which can be used to load the class returned by {@link FireableEventType#getEventClassName()}
	 */
	private final ClassLoader eventClassLoader;
	
	/**
	 * the event's class name
	 */
	private final String eventClassName;
	
	/**
	 * the event type ID
	 */
	private final EventTypeID eventTypeID;
	
	
	public FireableEventTypeImpl(ClassLoader eventClassLoader,
			String eventClassName, EventTypeID eventTypeID) {
		this.eventClassLoader = eventClassLoader;
		this.eventClassName = eventClassName;
		this.eventTypeID = eventTypeID;
	}

	public ClassLoader getEventClassLoader() {
		return eventClassLoader;
	}

	public String getEventClassName() {
		return eventClassName;
	}

	public EventTypeID getEventType() {
		return eventTypeID;
	}

	@Override
	public int hashCode() {
		return eventTypeID.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((FireableEventTypeImpl)obj).eventTypeID.equals(this.eventTypeID);
		}
		else {
			return false;
		}
	}
}
