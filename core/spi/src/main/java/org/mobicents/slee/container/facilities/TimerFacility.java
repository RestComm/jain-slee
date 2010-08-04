/**
 * 
 */
package org.mobicents.slee.container.facilities;

import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public interface TimerFacility extends javax.slee.facilities.TimerFacility,
		SleeContainerModule {

	/**
	 * Allows timer cancellation without detaching from {@link ActivityContext}.
	 * There is no validation of the timerID and transaction context.
	 * 
	 * @param timerID
	 * @param detachAC
	 */
	public void cancelTimer(TimerID timerID, boolean detachAC);
	
}
