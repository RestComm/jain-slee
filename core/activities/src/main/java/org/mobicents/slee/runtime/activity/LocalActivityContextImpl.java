/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.activity;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;

public class LocalActivityContextImpl implements LocalActivityContext {
	
	/**
	 * 
	 */
	private final ActivityContextHandle ach;
	
	/**
	 * 
	 */
	private EventRouterExecutor executor;
	
	/**
	 * 
	 */
	private final ActivityEventQueueManagerImpl eventQueueManager;
	
	/**
	 * 
	 */
	private EventRoutingTask routingTask;
	
	/**
	 * 
	 */
	private final int activityFlags;
	
	/**
	 * 
	 */
	private final ActivityContextFactoryImpl acFactory;

	/**
	 * 
	 */
	private Runnable activityReferencesCheck;

	public LocalActivityContextImpl(ActivityContextHandle ach, int activityFlags, ActivityContextFactoryImpl acFactory) {
		this.ach = ach;
		this.eventQueueManager = new ActivityEventQueueManagerImpl(this);
		this.activityFlags = activityFlags;
		this.acFactory = acFactory;
	}
	
	public ActivityEventQueueManagerImpl getEventQueueManager() {
		return eventQueueManager;
	}
	
	public EventRouterExecutor getExecutorService() {
		return executor;
	}
	
	public void setExecutorService(EventRouterExecutor executor) {
		this.executor = executor;
	}
	
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.LocalActivityContext#getCurrentEventRoutingTask()
	 */
	public EventRoutingTask getCurrentEventRoutingTask() {
		return routingTask;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.LocalActivityContext#setCurrentEventRoutingTask(org.mobicents.slee.container.eventrouter.EventRoutingTask)
	 */
	public void setCurrentEventRoutingTask(EventRoutingTask eventRoutingTask) {
		this.routingTask = eventRoutingTask;
	}
	
	public int getActivityFlags() {
		return activityFlags;
	}
	
	public ActivityContextFactoryImpl getAcFactory() {
		return acFactory;
	}
	
	public ActivityContextImpl getActivityContext() {
		return acFactory.getActivityContext(ach);
	}
	
	public Runnable getActivityReferencesCheck() {
		return activityReferencesCheck;
	}
	
	public void setActivityReferencesCheck(Runnable activityReferencesCheck) {
		this.activityReferencesCheck = activityReferencesCheck;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((LocalActivityContextImpl) obj).ach
					.equals(this.ach);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ach.hashCode();
	}
	
	
}
