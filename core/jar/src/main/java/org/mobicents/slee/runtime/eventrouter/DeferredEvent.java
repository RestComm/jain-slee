/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.eventrouter;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.resource.EventFlags;
import javax.slee.resource.ReceivableService;

import org.jboss.logging.Logger;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

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
	private final String acId;
	private final ActivityContextHandle ach;
	private final Object event;
	private final Address address;
	private final ReceivableService receivableService;
	private final int eventFlags;
	
	
	/**
	 * the aci loaded for event routing, to be used in event handling rollbacks
	 */
	private ActivityContextInterface loadedAci;

	public DeferredEvent(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address) {
		this(eventTypeId,event,ac,address,null,EventFlags.NO_FLAGS);		
	}
	
	public DeferredEvent(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ReceivableService receivableService, int eventFlags) {
		if (log.isDebugEnabled()) {
			log.debug("DeferredEvent() " + eventTypeId + "\n"
					+ "Activity Context:" + ac.getActivityContextId());
		}

		this.eventTypeId = eventTypeId;
		this.event = event;
		this.acId = ac.getActivityContextId();
		this.ach = ac.getActivityContextHandle();
		this.address = address;
		this.receivableService = receivableService;
		this.eventFlags = eventFlags;		
	}

	/**
	 * @return Returns the activity id.
	 */
	public String getActivityContextId() {
		return acId;
	}

	public ActivityContextHandle getActivityContextHandle() {
		return ach;
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

	public int getEventFlags() {
		return eventFlags;
	}
	
	public ReceivableService getReceivableService() {
		return receivableService;
	}
	
	public ActivityContextInterface getLoadedAci() {
		return loadedAci;
	}
	
	public void setLoadedAci(ActivityContextInterface aci) {
		this.loadedAci = aci;
	}
}
