package org.mobicents.slee.runtime.eventrouter;

import javax.slee.Address;
import javax.transaction.SystemException;

import org.mobicents.slee.runtime.activity.ActivityContext;

/**
 * Useful activity end event version of the {@link DeferredEvent}.
 * 
 * @author martins
 *
 */
public class DeferredActivityEndEvent extends DeferredEvent {

	public DeferredActivityEndEvent(ActivityContext ac, Address address) throws SystemException {
		super(ActivityEndEventImpl.EVENT_TYPE_ID,ActivityEndEventImpl.SINGLETON,ac,address);
	}
}
