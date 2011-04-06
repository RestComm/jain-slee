package org.mobicents.slee.container.management;

import javax.slee.management.SleeState;

/**
 * A request to change SLEE state.
 * 
 * @author martins
 *
 */
public interface SleeStateChangeRequest {

	/**
	 * The state to set
	 * @return
	 */
	public SleeState getNewState();
	
	/**
	 * Indicates if the container operations, which result from state change, should be completed before returning from state change request.
	 * @return
	 */
	public boolean isBlockingRequest();
	
	/**
	 * Indicates that the state has now changed.
	 * @param oldState
	 */
	public void stateChanged(SleeState oldState);
	
	/**
	 * Indicates that the whole process of state change completed.
	 */
	public void requestCompleted();
	
}
