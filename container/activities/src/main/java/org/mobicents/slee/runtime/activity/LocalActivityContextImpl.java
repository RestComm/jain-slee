/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2015, Telestax Inc and individual contributors
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

    /**
     *  
     */
    private final String stringId;

	public LocalActivityContextImpl(ActivityContextHandle ach, int activityFlags, String stringId, ActivityContextFactoryImpl acFactory) {
        this.ach = ach;
		this.eventQueueManager = new ActivityEventQueueManagerImpl(this);
		this.activityFlags = activityFlags;
		this.acFactory = acFactory;
        this.stringId = stringId;
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
    public String getStringId() {
        return stringId;
    }

    @Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((LocalActivityContextImpl) obj).stringId
					.equals(this.stringId);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return stringId.hashCode();
	}
	
	
}
