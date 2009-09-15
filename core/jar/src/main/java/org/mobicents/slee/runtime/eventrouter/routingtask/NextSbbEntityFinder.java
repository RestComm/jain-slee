package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.Iterator;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.eventrouter.ActivityEndEventImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;

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
		
	/**
	 * Retrieves the next sbb entity to handle the event.
	 * 
	 * @param ac
	 * @param eventTypeID
	 * @param service
	 * @param sbbEntitiesThatHandledCurrentEvent
	 * @return Result that indicates the next sbb entity to handle the event,
	 *         note that sbb entities that are not entitled to *deliver* the
	 *         event (service id is set or the event is not defined in sbb
	 *         descriptor) will only be returned in case event is activity end
	 *         event.
	 */
	public Result next(ActivityContext ac,
			EventTypeID eventTypeID, ServiceID service, Set<String> sbbEntitiesThatHandledCurrentEvent) {
		
		String sbbEntityId = null;
		SbbEntity sbbEntity = null;
		MEventEntry mEventEntry = null;
		
		boolean activityEndEvent = eventTypeID == ActivityEndEventImpl.EVENT_TYPE_ID;
		
		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator<?> iter = ac.getSortedSbbAttachmentSet(sbbEntitiesThatHandledCurrentEvent).iterator(); iter
				.hasNext();) {
			sbbEntityId = (String) iter.next();
			try {
				sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
				if (service != null && !service.equals(sbbEntity.getServiceId())) {
					if (!activityEndEvent) {
						continue;
					}
					else {
						return new Result(sbbEntity, false);						
					}
				}
				// check event is allowed to be handled by the sbb
				mEventEntry = sbbEntity.getSbbComponent().getDescriptor().getEventEntries().get(eventTypeID);
				if (mEventEntry != null && mEventEntry.isReceived()) {
					return new Result(sbbEntity, true);					
				} else {
					if (!activityEndEvent) {
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
			} catch (IllegalStateException e) {
				// ignore, sbb entity has been removed
				continue;
			}
		}

		return null;

	}

}
