/**
 * 
 */
package org.mobicents.slee.container.event;

/**
 * Callback used to indicate that a {@link EventContext} processing succeed.
 * 
 * @author martins
 * 
 */
public interface EventProcessingSucceedCallback {

	/**
	 * Indicates that a {@link EventContext} processing succeed.
	 */
	public void eventProcessingSucceed(boolean sbbProcessedEvent);

}
