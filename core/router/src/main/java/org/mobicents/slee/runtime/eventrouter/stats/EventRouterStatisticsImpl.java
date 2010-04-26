/**
 * 
 */
package org.mobicents.slee.runtime.eventrouter.stats;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics;

/**
 * @author martins
 *
 */
public class EventRouterStatisticsImpl implements EventRouterStatistics {

	private EventRouter eventRouter;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.eventrouter.stats.EventRouterStatisticsMBean#getActivitiesMapped()
	 */
	public int getActivitiesMapped() {
		int result = 0;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getActivitiesMapped(i);			
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.eventrouter.stats.EventRouterStatisticsMBean#getActivitiesMapped(int)
	 */
	public int getActivitiesMapped(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getActivitiesMapped();		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatistics#getEventRouter()
	 */
	public EventRouter getEventRouter() {
		return eventRouter;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatistics#setEventRouter(org.mobicents.slee.runtime.eventrouter.EventRouter)
	 */
	public void setEventRouter(EventRouter eventRouter) {
		this.eventRouter = eventRouter;		
	}
	
	private EventRouterExecutor[] getExecutors() throws IllegalStateException {
		if (eventRouter == null) {
			throw new IllegalStateException("event router not set");
		}
		return eventRouter.getExecutors();
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatistics#getEventRouterExecutorStatistics(int)
	 */
	public EventRouterExecutorStatistics getEventRouterExecutorStatistics(
			int executor) {
		return getExecutors()[executor].getStatistics();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getEventsRouted(javax.slee.EventTypeID)
	 */
	public long getEventsRouted(EventTypeID eventTypeID) {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getEventsRouted(i,eventTypeID);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getEventsRouted(int, javax.slee.EventTypeID)
	 */
	public long getEventsRouted(int executor, EventTypeID eventTypeID) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getEventsRouted(eventTypeID);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutedTasks()
	 */
	public long getExecutedTasks() {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getExecutedTasks(i);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutedTasks(int)
	 */
	public long getExecutedTasks(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getExecutedTasks();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutingTime(int)
	 */
	public long getExecutingTime(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getExecutingTime();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getIdleTime(int)
	 */
	public long getIdleTime(int executor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecuted()
	 */
	public long getMiscTasksExecuted() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecuted(int)
	 */
	public long getMiscTasksExecuted(int executor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecutingTime(int)
	 */
	public long getMiscTasksExecutingTime(int executor) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getRoutingTime(javax.slee.EventTypeID)
	 */
	public long getRoutingTime(EventTypeID eventTypeID) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getRoutingTime(int, javax.slee.EventTypeID)
	 */
	public long getRoutingTime(int executor, EventTypeID eventTypeID) {
		// TODO Auto-generated method stub
		return 0;
	}

}
