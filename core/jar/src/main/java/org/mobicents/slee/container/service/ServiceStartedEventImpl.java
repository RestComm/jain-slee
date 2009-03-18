package org.mobicents.slee.container.service;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.serviceactivity.ServiceStartedEvent;

/**
 * Implementation of the Service Started Event.
 * 
 * @author M. Ranganathan
 * @author martins
 *
 */
public class ServiceStartedEventImpl implements ServiceStartedEvent {
    
	private ServiceID serviceID;
    
    public ServiceStartedEventImpl(ServiceID serviceID) {
        this.serviceID = serviceID;
    }
   
    public ServiceID getService() {
        return this.serviceID;
    }

    /**
	 *	the event type id for this event that is compliant with JAIN SLEE 1.0
	 */
	public static final EventTypeID SLEE_10_EVENT_TYPE_ID = new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
	"1.0");
	
	/**
	 *	the event type id for this event that is compliant with JAIN SLEE 1.1
	 */
	public static final EventTypeID SLEE_11_EVENT_TYPE_ID = new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
	"1.1");
}

