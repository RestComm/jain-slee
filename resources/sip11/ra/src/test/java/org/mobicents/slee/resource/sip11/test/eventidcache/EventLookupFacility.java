package org.mobicents.slee.resource.sip11.test.eventidcache;

import java.util.HashMap;
import java.util.Map;

import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.FireableEventType;

public class EventLookupFacility implements javax.slee.facilities.EventLookupFacility {
	
	private Map<EventTypeID,FireableEventType> eventIds = new HashMap<EventTypeID, FireableEventType>();
	
	public FireableEventType getFireableEventType(EventTypeID arg0)
			throws NullPointerException, UnrecognizedEventException,
			FacilityException {
		return eventIds.get(arg0);
	}

	public void putEventID(String name, String vendor, String version,
			FireableEventType eventType) {
		eventIds.put(new EventTypeID(name,vendor,version), eventType);
	}

}
