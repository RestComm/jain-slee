package org.mobicents.slee.container.service;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;

/**
 * Useful service started event version of the {@link DeferredEvent}.
 * 
 *
 * @author martins
 *
 */
public class DeferredServiceStartedEvent extends DeferredEvent {

	public DeferredServiceStartedEvent(ActivityContext ac, ServiceStartedEventImpl event, SleeContainer sleeContainer) {
		super(ServiceStartedEventImpl.EVENT_TYPE_ID,event,ac,null,sleeContainer);
	}
	
}