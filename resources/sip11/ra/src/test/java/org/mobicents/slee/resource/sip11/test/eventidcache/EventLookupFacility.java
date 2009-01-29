package org.mobicents.slee.resource.sip11.test.eventidcache;

import java.util.HashMap;
import java.util.Map;

import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;

public class EventLookupFacility implements javax.slee.facilities.EventLookupFacility {
	
	private Map<EventIDKey,Integer> eventIds = new HashMap<EventIDKey, Integer>();
	
	public void putEventID(String arg0, String arg1, String arg2, int eventID) {
		eventIds.put(new EventIDKey(arg0,arg1,arg2), Integer.valueOf(eventID));
	}
	
	public void removeEventID(String arg0, String arg1, String arg2) {
		eventIds.remove(new EventIDKey(arg0,arg1,arg2));
	}
	
	public ClassLoader getEventClassLoader(int arg0) {
		throw new UnsupportedOperationException();
	}

	public String getEventClassName(int arg0)
			throws UnrecognizedEventException, FacilityException {
		throw new UnsupportedOperationException();
	}

	public int getEventID(String arg0, String arg1, String arg2)
			throws NullPointerException, UnrecognizedEventException,
			FacilityException {
		return eventIds.get(new EventIDKey(arg0,arg1,arg2));
	}

	public String[] getEventType(int arg0) throws UnrecognizedEventException,
			FacilityException {
		throw new UnsupportedOperationException();
	}

}
