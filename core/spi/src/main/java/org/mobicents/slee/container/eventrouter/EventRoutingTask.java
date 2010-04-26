/**
 * 
 */
package org.mobicents.slee.container.eventrouter;

import org.mobicents.slee.container.event.EventContext;

/**
 * @author martins
 *
 */
public interface EventRoutingTask extends Runnable {

	public EventContext getEventContext();

}
