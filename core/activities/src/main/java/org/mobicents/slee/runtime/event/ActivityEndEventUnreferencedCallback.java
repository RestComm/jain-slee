/**
 * 
 */
package org.mobicents.slee.runtime.event;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;

/**
 * @author martins
 *
 */
public class ActivityEndEventUnreferencedCallback implements EventUnreferencedCallback {

	private final ActivityContextHandle ach;
	private final ActivityContextFactoryImpl factory;
	
	/**
	 * @param ach
	 * @param factory
	 */
	public ActivityEndEventUnreferencedCallback(ActivityContextHandle ach,
			ActivityContextFactoryImpl factory) {
		this.ach = ach;
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#eventUnreferenced()
	 */
	public void eventUnreferenced() {
		factory.getActivityContext(ach).activityEnded();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#requiresTransaction()
	 */
	public boolean requiresTransaction() {
		return true;
	}

}
