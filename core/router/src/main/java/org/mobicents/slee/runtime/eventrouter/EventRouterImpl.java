package org.mobicents.slee.runtime.eventrouter;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRouterExecutorMapper;
import org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsImpl;

/**
 * 
 * @author Eduardo Martins
 */

public class EventRouterImpl extends AbstractSleeContainerModule implements EventRouter {

	private static Logger logger = Logger.getLogger(EventRouter.class);
	
	/**
	 * The array of {@link EventRouterExecutor}s that are used to route events
	 */
	private EventRouterExecutor[] executors;
		
	/**
	 * Maps executors to activities.
	 */
	private EventRouterExecutorMapper executorMapper;
	
	/**
	 * Provides performance and load statistics of the event router.
	 */
	private EventRouterStatistics statistics;
	
	private final EventRouterConfiguration configuration;
	
	/**
	 * 
	 */
	public EventRouterImpl(EventRouterConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeInit()
	 */
	@Override
	public void sleeStarting() {
		logger
		.info("Mobicents JAIN SLEE Event Router started.");
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStart()
	 */
	@Override
	public void beforeSleeRunning() {
		// get ridden of old executors, if any
		if (this.executors != null) {
			for (EventRouterExecutor executor : this.executors) {
				executor.shutdown();
			}
		}
		// create new ones
		this.executors = new EventRouterExecutor[configuration.getEventRouterThreads()];
		for (int i = 0; i < configuration.getEventRouterThreads(); i++) {
			this.executors[i] = new EventRouterExecutorImpl(configuration.isCollectStats(),sleeContainer);
		}	
		// create mapper
		try {
			Class<?> executorMapperClass = Class.forName(configuration.getExecutorMapperClassName());
			executorMapper = (EventRouterExecutorMapper) executorMapperClass.newInstance();
			executorMapper.setExecutors(executors);
		} catch (Throwable e) {
			throw new IllegalStateException("Unable to create event router executor mapper class instance",e);
		}		
		// create stats
		statistics = new EventRouterStatisticsImpl(this);
	}
	
	@Override
	public String toString() {
		return "EventRouter: "
		+ "\n+-- Executors: " + executors.length;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouter#getEventRouterStatistics()
	 */
	public EventRouterStatistics getEventRouterStatistics() {
		return statistics;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouter#getExecutors()
	 */
	public EventRouterExecutor[] getExecutors() {
		return executors;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.EventRouter#getEventRouterExecutorMapper()
	 */
	public EventRouterExecutorMapper getEventRouterExecutorMapper() {
		return executorMapper;
	}

}