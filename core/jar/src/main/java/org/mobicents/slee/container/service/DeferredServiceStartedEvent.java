package org.mobicents.slee.container.service;

import javax.transaction.SystemException;

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

	public DeferredServiceStartedEvent(ActivityContext ac, ServiceStartedEventImpl event) throws SystemException {
		super(ServiceStartedEventImpl.getEventTypeID(),event,ac,null);
	}
	
}