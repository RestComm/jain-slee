/**
 * 
 */
package org.mobicents.slee.container.eventrouter.stats;

import org.mobicents.slee.container.eventrouter.EventRouter;

/**
 * Performance and load statistics for the SLEE {@link EventRouter}.
 * 
 * @author martins
 * 
 */
public interface EventRouterStatistics extends EventRouterStatisticsMBean {

	/**
	 * Retrieves the event router.
	 * @return
	 */
	public EventRouter getEventRouter();
	
	/**
	 * Retrieves the statistics for the specified event router executor.
	 * 
	 * @param executor
	 *            the executor number
	 * @return
	 */
	public EventRouterExecutorStatistics getEventRouterExecutorStatistics(
			int executor);

}
