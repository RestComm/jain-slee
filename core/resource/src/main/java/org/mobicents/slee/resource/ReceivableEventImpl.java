package org.mobicents.slee.resource;

import javax.slee.EventTypeID;
import javax.slee.resource.ReceivableService.ReceivableEvent;

/**
 * Implementation of the SLEE 1.1 specs {@link ReceivableEvent} class.
 * @author martins
 *
 */
public class ReceivableEventImpl implements ReceivableEvent {

	private final EventTypeID eventType;
	private final String resourceOption;
	private final boolean initialEvent;
		
	public ReceivableEventImpl(EventTypeID eventType, String resourceOption,
			boolean initialEvent) {
		this.eventType = eventType;
		this.resourceOption = resourceOption;
		this.initialEvent = initialEvent;
	}

	public EventTypeID getEventType() {
		return eventType;
	}

	public String getResourceOption() {
		return resourceOption;
	}

	public boolean isInitialEvent() {
		return initialEvent;
	}

	@Override
	public int hashCode() {
		return eventType.hashCode()*31 + (resourceOption != null ? resourceOption.hashCode() : 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ReceivableEventImpl other = (ReceivableEventImpl) obj;
			if (this.resourceOption == null) {
				if (other.resourceOption != null) {
					return false;
				}
			}
			else {
				if (!this.resourceOption.equals(other.resourceOption)) {
					return false;
				}
			}
			return this.eventType.equals(other.eventType);
		}
		else {
			return false;
		}
	}
}
