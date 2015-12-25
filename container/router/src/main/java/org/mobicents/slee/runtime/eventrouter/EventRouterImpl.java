/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */

package org.mobicents.slee.runtime.eventrouter;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.eventrouter.EventRouter;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRouterExecutorMapper;
import org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsImpl;
import org.mobicents.slee.util.concurrent.SleeThreadFactory;

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
	
	@Override
	public void sleeInitialization() {
		logger
		.info("Mobicents JAIN SLEE Event Router started.");
	}
	
	@Override
	public void sleeStarting() {
		// get ridden of old executors, if any
		if (this.executors != null) {
			for (EventRouterExecutor executor : this.executors) {
				executor.shutdown();
			}
		}
		// create new ones
		this.executors = new EventRouterExecutor[configuration.getEventRouterThreads()];
		for (int i = 0; i < configuration.getEventRouterThreads(); i++) {
			this.executors[i] = new EventRouterExecutorImpl(configuration.isCollectStats(), new SleeThreadFactory("SLEE-EventRouterExecutor-"+i), sleeContainer);
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

	public EventRouterConfiguration getConfiguration() {
		return configuration;
	}	

}