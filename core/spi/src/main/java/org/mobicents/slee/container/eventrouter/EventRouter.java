package org.mobicents.slee.container.eventrouter;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics;

/**
 * Interface for the container's Event Router
 * 
 * @author Eduardo Martins
 * 
 */
public interface EventRouter extends SleeContainerModule {

	/**
	 * Returns the event router executor's mapper.
	 * 
	 * @return
	 */
	public EventRouterExecutorMapper getEventRouterExecutorMapper();

	/**
	 * Retrieves the event router stats.
	 * 
	 * @return
	 */
	public EventRouterStatistics getEventRouterStatistics();

	/**
	 * Retrieves the event router executors.
	 * 
	 * @return
	 */
	public EventRouterExecutor[] getExecutors();

}
