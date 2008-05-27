package org.mobicents.slee.connector.proxy;

import javax.resource.ResourceException;

public interface SleeConnectionProxyInterface {

	
	
	public javax.slee.connection.ExternalActivityHandle createActivityHandle()
	throws ResourceException ;

/**
* Lookups eventTypeID.
* @return EventTypeID of partcilar event.
*/
public javax.slee.EventTypeID getEventTypeID(String eventName,
	String eventVendor, String eventVersion)
	throws javax.slee.UnrecognizedEventException,
	javax.resource.ResourceException ;
/**
* Fires event to slee.
*/
public void fireEvent(
	Object event,
	javax.slee.EventTypeID eventTypeID,
	javax.slee.connection.ExternalActivityHandle externalActivityHandle,
	javax.slee.Address address) throws java.lang.NullPointerException,
	javax.slee.UnrecognizedActivityException,
	javax.slee.UnrecognizedEventException,
	javax.resource.ResourceException ;

/**
* @throws ResourceException
*/
public void close() ;
	
	
}
