package org.mobicents.slee.runtime.eventrouter;

import java.io.ObjectStreamException;

import javax.slee.ActivityEndEvent;
import javax.slee.EventTypeID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;

/**
 * 
 * Activity End Event implementation, as a singleton, also useful to retrieve
 * related objects such {@link EventTypeID} or {@link ComponentKey}.
 * 
 * @author Eduardo Martins
 * @author F.Moggia
 */
public class ActivityEndEventImpl implements ActivityEndEvent {
	
	/**
	 *	the component key for this event
	 */
	public static final ComponentKey COMPONENT_KEY = new ComponentKey("javax.slee.ActivityEndEvent",
			"javax.slee", "1.0");
	
	private static EventTypeID activityEndEventTypeID = null;
	/**
	 * 
	 * @return the event type id for the activity end event
	 */
	public static EventTypeID getEventTypeID() {
		if (activityEndEventTypeID == null) {
			activityEndEventTypeID = SleeContainer.lookupFromJndi().getEventManagement().getEventType(ActivityEndEventImpl.COMPONENT_KEY);
		}
		return activityEndEventTypeID;
	}
	
	/**
	 * singleton for this class
	 */
	public static final ActivityEndEventImpl SINGLETON = new ActivityEndEventImpl();
	
	/**
	 * private contsructor to ensure singleton
	 */
	private ActivityEndEventImpl() {
		
	}
		
	// solves serialization of singleton
	private Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }

    // solves cloning of singleton
    protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
    }
}
