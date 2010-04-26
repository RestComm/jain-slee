/**
 * 
 */
package org.mobicents.slee.container.event;

import javax.slee.resource.FailureReason;

/**
 * Callback used to indicate that a {@link EventContext} processing failed.
 * 
 * @author martins
 * 
 */
public interface EventProcessingFailedCallback {

	/**
	 * Indicates that a {@link EventContext} processing failed.
	 * @param failureReason
	 */
	public void eventProcessingFailed(FailureReason failureReason);

}
