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

package org.mobicents.slee.resource;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

public class EndAllActivitiesRAEntityTimerTask implements Runnable {

	private static final Logger logger = Logger.getLogger(EndAllActivitiesRAEntityTimerTask.class);
	
	private static final long delay = 45;
	
	private final ResourceAdaptorEntity raEntity;
	private final SleeContainer sleeContainer;
	
	private final ScheduledFuture<?> scheduledFuture;
	
	public EndAllActivitiesRAEntityTimerTask(ResourceAdaptorEntity raEntity,SleeContainer sleeContainer) {
		this.raEntity = raEntity;
		this.sleeContainer = sleeContainer;
		this.scheduledFuture = sleeContainer.getNonClusteredScheduler().schedule(this, delay,TimeUnit.SECONDS);
	}
	
	public boolean cancel() {
		return scheduledFuture.cancel(false);		
	}

	@Override
	public void run() {
		logger.info("Forcing the end of all activities for ra entity "+ raEntity.getName());
		// first round, end all activities gracefully
		boolean noActivitiesFound = true;
		for (ActivityContextHandle handle : sleeContainer
				.getActivityContextFactory()
				.getAllActivityContextsHandles()) {
			if (handle.getActivityType() == ActivityType.RA) {
				final ResourceAdaptorActivityContextHandle raHandle = (ResourceAdaptorActivityContextHandle) handle;
				if (raHandle.getResourceAdaptorEntity().equals(raEntity)) {
					noActivitiesFound = false;
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("Forcing the end of activity " + handle+" Pt.1");
						}
						ActivityContext ac = sleeContainer
						.getActivityContextFactory()
						.getActivityContext(handle);
						if (ac != null) {
							// if it has a suspended event context then resume it
							EventRoutingTask routingTask = ac.getLocalActivityContext().getCurrentEventRoutingTask();
							EventContext eventContext = routingTask != null ? routingTask.getEventContext() : null;
							if (eventContext != null && eventContext.isSuspended()) {
								eventContext.resumeDelivery();
							}
							// end activity
							ac.endActivity();							
						}
					} catch (Exception e) {
						if (logger.isDebugEnabled()) {
							logger.debug("Failed to end activity " + handle+" Pt.1", e);
						}
					}
				}
			}
		}
		if (noActivitiesFound) {
			raEntity.allActivitiesEnded();
		}
		else {
			// second round, enforcing the removal of all stuck activities
			// sleep 15s
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
			noActivitiesFound = true; 
			for (ActivityContextHandle handle : sleeContainer
					.getActivityContextFactory()
					.getAllActivityContextsHandles()) {
				if (handle.getActivityType() == ActivityType.RA) {
					final ResourceAdaptorActivityContextHandle raHandle = (ResourceAdaptorActivityContextHandle) handle;
					if (raHandle.getResourceAdaptorEntity().equals(raEntity)) {
						noActivitiesFound = false;
						try {
							if (logger.isDebugEnabled()) {
								logger.debug("Forcing the end of activity " + handle+" Pt.2");
							}
							ActivityContext ac = sleeContainer
							.getActivityContextFactory()
							.getActivityContext(handle);
							if (ac != null) {
								for(SbbEntityID sbbEntityId : ac.getSbbAttachmentSet()) {
									ac.detachSbbEntity(sbbEntityId);
								}
								ac.activityEnded();							
							}
						} catch (Exception e) {
							if (logger.isDebugEnabled()) {
								logger.debug("Failed to end activity " + handle+" Pt.2", e);
							}
						}
					}
				}
			}
			if (noActivitiesFound) {
				if (logger.isDebugEnabled()) {
					logger.debug("Found no activities for the ra entity but the entity didn't stop, forcing...");
				}
				// concurrent ending of activities may fail to notify the ra entity
				raEntity.allActivitiesEnded();
			}
		}
	}
}
