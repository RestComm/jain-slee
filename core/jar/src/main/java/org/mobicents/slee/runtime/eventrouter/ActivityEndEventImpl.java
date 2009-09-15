package org.mobicents.slee.runtime.eventrouter;

import java.io.ObjectStreamException;

import javax.slee.ActivityEndEvent;
import javax.slee.EventTypeID;

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
	 *	the event type id for this event
	 *  NOTE: do not build other instances of this event type id since 
	 *  the event router depends on that, i.e., uses == in some conditional choices
	 */
	public static final EventTypeID EVENT_TYPE_ID = new EventTypeID("javax.slee.ActivityEndEvent",
			"javax.slee", "1.0");
		
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
