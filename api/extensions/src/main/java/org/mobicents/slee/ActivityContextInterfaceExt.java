package org.mobicents.slee;

import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerID;

public interface ActivityContextInterfaceExt extends ActivityContextInterface {

	/**
	 * Retrieves the IDs of timers currently set in the {@link ActivityContextInterfaceExt}.
	 * @return an array with the {@link TimerID}s.
	 */
	public TimerID[] getTimers();
	
	/**
	 * Retrieves the names currently bound to the {@link ActivityContextInterfaceExt}.
	 * @return an array with the names bound.
	 */
	public String[] getNamesBound();
	
}
