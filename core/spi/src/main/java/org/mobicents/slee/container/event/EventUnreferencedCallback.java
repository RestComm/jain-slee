/**
 * 
 */
package org.mobicents.slee.container.event;

/**
 * Callback used to indicate that a {@link EventContext} is unreferenced.
 * 
 * @author martins
 * 
 */
public interface EventUnreferencedCallback {

	public void eventUnreferenced();

	public boolean requiresTransaction();
	
}
