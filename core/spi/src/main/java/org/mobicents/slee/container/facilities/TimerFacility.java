/**
 * 
 */
package org.mobicents.slee.container.facilities;

import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 *
 */
public interface TimerFacility extends javax.slee.facilities.TimerFacility, SleeContainerModule {

	public void cancelTimerWithoutValidation(TimerID timerID);
	
}
