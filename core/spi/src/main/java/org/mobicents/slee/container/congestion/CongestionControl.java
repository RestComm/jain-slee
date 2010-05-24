package org.mobicents.slee.container.congestion;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * Congestion Control module for the Mobicents SLEE Container.
 * 
 * @author martins
 *
 */
public interface CongestionControl extends SleeContainerModule {

	/**
	 * 
	 * @return true if the start of activity should be refused, due to
	 *         congestion control being active.
	 */
	public boolean refuseStartActivity();

	/**
	 * 
	 * @return true if the firing of an event should be refused, due to
	 *         congestion control being active.
	 */
	public boolean refuseFireEvent();
}
