package org.mobicents.slee.container.event;

/**
 * Useful activity end event version of the {@link EventContextImpl}.
 * 
 * @author martins
 *
 */
public class ActivityEndEventContextImpl extends EventContextImpl {

	/**
	 * 
	 * @param ac
	 */
	public ActivityEndEventContextImpl(EventContextData data, EventContextFactoryImpl factory) {
		super(data,factory);		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.SleeEventImpl#isActivityEndEvent()
	 */
	@Override
	public boolean isActivityEndEvent() {
		return true;
	}
}
