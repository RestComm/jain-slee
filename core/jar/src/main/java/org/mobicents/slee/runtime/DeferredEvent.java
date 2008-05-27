/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/


package org.mobicents.slee.runtime;

//import java.io.Serializable;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;

/**
 * A differed event. When an SBB posts an event, it winds up as one of these. When the tx commits, it actually makes it
 * into the event queue.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov (refactoring)
 * @author eduardomartins
 *
 */
public class DeferredEvent {
    
	private static Logger log = Logger.getLogger(DeferredEvent.class);
    
    private final EventTypeID eventTypeId;
    private final Object activity;
    private final Object event;
    private final Address address;
    private final String activityContextId;
    //private final int hashCode;
    
    public DeferredEvent (int eventId, Object event, ActivityContext ac, 
            Address address	) throws SystemException{
        this(SleeContainer.lookupFromJndi().getEventTypeID(eventId), event, ac, address);
    }

    public DeferredEvent (EventTypeID eventTypeId, Object event, ActivityContext ac, 
            Address address	) throws SystemException{
        if ( log.isDebugEnabled()) {
            log.debug("DeferredEvent() " + eventTypeId +"\n"
                    + "Activity:" + ac.getActivity() +"\n"+
                    "Activity Context:" + ac);
        }
		this.eventTypeId = eventTypeId;
		this.event = event;
		this.activity = ac.getActivity();
		this.address = address;
		this.activityContextId = ac.getActivityContextId();
		/*
		this.hashCode = ((activity.hashCode() * 31 + (address == null ? 0
				: address.hashCode())) * 31 + event.hashCode())
				* 31 + eventTypeId.hashCode();
		*/
		// put on ac outstanding events
		ac.putOutstandingEvent(this);
		// add tx actions to commit or rollback
		SleeContainer.getTransactionManager().addAfterCommitAction(
				new CommitDeferredEventAction(this));
		SleeContainer.getTransactionManager().addAfterRollbackAction(
				new RollbackDeferredEventAction(this, ac));
	}

	/**
	 * @return Returns the activity.
	 */
    public Object getActivity() {
        return activity;
    }

    /**
     * Retreives the activity context id.
     * @return
     */
    public String getActivityContextId() {
		return activityContextId;
	}
    
    /**
     * @return Returns the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @return Returns the event.
     */
    public Object getEvent() {
        return event;
    }

    /**
     * @return Returns the eventTypeId.
     */
    public EventTypeID getEventTypeId() {
        return eventTypeId;
    }

    // uncomment if this class becomes serializable
    /*
    public boolean equals(Object arg0) {
		if (arg0 != null && arg0.getClass() == this.getClass()) {
			DeferredEvent other = (DeferredEvent) arg0;
			if (this.address == null) {
				if (other.address != null) {
					return false;
				} else {
					return this.activity.equals(other.activity)
							&& this.event.equals(other.event)
							&& this.eventTypeId.equals(other.eventTypeId);
				}
			} else {
				if (other.address == null) {
					return false;
				} else {
					return this.activity.equals(other.activity)
							&& this.event.equals(other.event)
							&& this.address.equals(other.address)
							&& this.eventTypeId.equals(other.eventTypeId);
				}
			}

		} else {
			return false;
		}
	}

	public int hashCode() {
		return hashCode;
    }
    */
    
}

