package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.sbbentity.SbbEntity;

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
	 * @param sleeEvent
	 * @param sbbEntitiesThatHandledCurrentEvent
	 * @return Result that indicates the next sbb entity to handle the event,
	 *         note that sbb entities that are not entitled to *deliver* the
	 *         event (service id is set or the event is not defined in sbb
	 *         descriptor) will only be returned in case event is activity end
	 *         event.
	 */
	public Result next(ActivityContext ac,
			EventContext sleeEvent, Set<String> sbbEntitiesThatHandledCurrentEvent, SleeContainer sleeContainer) {
		
		String sbbEntityId = null;
		SbbEntity sbbEntity = null;
		EventEntryDescriptor mEventEntry = null;
				
		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator<?> iter = ac.getSortedSbbAttachmentSet(sbbEntitiesThatHandledCurrentEvent).iterator(); iter
				.hasNext();) {
			sbbEntityId = (String) iter.next();
			sbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity(sbbEntityId,true);
			if (sbbEntity == null) {
				// ignore, sbb entity has been removed
				continue;
			}
			if (sleeEvent.getService() != null && !sleeEvent.getService().equals(sbbEntity.getServiceId())) {
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
