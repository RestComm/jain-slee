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
	 *	the event type id for this event
	 */
	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
	"1.0");
		
}

