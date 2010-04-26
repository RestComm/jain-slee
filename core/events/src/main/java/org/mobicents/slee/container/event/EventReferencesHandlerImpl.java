/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * 
 * @author martins
 *
 */
public class EventReferencesHandlerImpl implements ReferencesHandler {

	private final AtomicInteger references = new AtomicInteger(0);
	private EventContextImpl eventContext;
	
	/**
	 * @param eventContext the eventContext to set
	 */
	protected void setEventContext(EventContextImpl eventContext) {
		this.eventContext = eventContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventReferencesHandler#add(org.mobicents.slee.container.activity.ActivityContextHandle)
	 */
	public void add(ActivityContextHandle ach) {
		references.incrementAndGet();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventReferencesHandler#remove(org.mobicents.slee.container.activity.ActivityContextHandle)
	 */
	public void remove(ActivityContextHandle ach) {
		if (references.decrementAndGet() == 0) {
			eventContext.eventUnreferenced();
		}
	}

}
