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

package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.management.jmx.EventRouterConfiguration;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;

/**
 * 
 * Class that can be used to find, while delivering an event, the next sbb
 * entity to be processed.
 * 
 * @author martins
 * 
 */
public class NextSbbEntityFinder {

	private static final Logger logger = Logger
			.getLogger(NextSbbEntityFinder.class);
		
	static class Result {
		
		final SbbEntity sbbEntity;
		final boolean deliverEvent;
		
		Result(SbbEntity sbbEntity, boolean deliverEvent) {
			super();
			this.sbbEntity = sbbEntity;
			this.deliverEvent = deliverEvent;
		}
	}

	private final EventRouterConfiguration eventRouterConfiguration = ((EventRouterImpl) SleeContainer
			.lookupFromJndi().getEventRouter()).getConfiguration();

	/**
	 * Retrieves the next sbb entity to handle the event.
	 * 
	 * @param ac
	 * @param sleeEvent
	 * @param sbbEntitiesThatHandledCurrentEvent
	 * @return Result that indicates the next sbb entity to handle the event,
	 *         note that sbb entities that are not entitled to *deliver* the
	 *         event (service id is set or the event is not defined in sbb
	 *         descriptor) will only be returned in case event is activity end
	 *         event.
	 */
	public Result next(ActivityContext ac,
			EventContext sleeEvent, Set<SbbEntityID> sbbEntitiesThatHandledCurrentEvent, SleeContainer sleeContainer) {
		
		SbbEntityID sbbEntityId = null;
		SbbEntity sbbEntity = null;
		EventEntryDescriptor mEventEntry = null;
				
		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator<SbbEntityID> iter = ac.getSortedSbbAttachmentSet(sbbEntitiesThatHandledCurrentEvent).iterator(); iter
				.hasNext();) {
			sbbEntityId = iter.next();
			sbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity(sbbEntityId,true);
			if (sbbEntity == null) {
				// ignore, sbb entity has been removed
				continue;
			}
			if (eventRouterConfiguration.isConfirmSbbEntityAttachement() && !sbbEntity.isAttached(ac.getActivityContextHandle())) {
				// detached by a concurrent tx, see Issue 2313 				
				continue;
			}
			if (sleeEvent.getService() != null && !sleeEvent.getService().equals(sbbEntityId.getServiceID())) {
				if (!sleeEvent.isActivityEndEvent()) {
					continue;
				}
				else {
					return new Result(sbbEntity, false);						
				}
			}
			// check event is allowed to be handled by the sbb
			mEventEntry = sbbEntity.getSbbComponent().getDescriptor().getEventEntries().get(sleeEvent.getEventTypeId());
			if (mEventEntry != null && mEventEntry.isReceived()) {
				return new Result(sbbEntity, true);					
			} else {
				if (!sleeEvent.isActivityEndEvent()) {
					if (logger.isDebugEnabled()) {
						logger
						.debug("Event is not received by sbb descriptor of entity "
								+ sbbEntityId + ", will not deliver event to sbb entity ...");
					}
					continue;
				}
				else {
					return new Result(sbbEntity, false);						
				}
			}			
		}

		return null;

	}

}
