package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.Iterator;
import java.util.Set;

import javax.slee.EventTypeID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
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

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * Finds the next sbb entity to possibly deliver an event of the specified
	 * event type {@link EventTypeID}, in the specified {@link ActivityContext}.
	 * 
	 * The next sbb entity is the one attached with highest priority, didn't
	 * handle the event yet, and of course, the related sbb descriptor declares
	 * receiving the event type.
	 * 
	 * @param ac
	 * @param eventTypeID
	 * @return
	 */
	public SbbEntity next(ActivityContext ac,
			EventTypeID eventTypeID) {

		String sbbEntityId = null;
		SbbEntity sbbEntity = null;

		Set<String> sbbEntitiesThatHandledCurrentEvent = sleeContainer.getEventRouter().getEventRouterActivity(ac.getActivityContextId()).getSbbEntitiesThatHandledCurrentEvent();
		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator iter = ac.getSortedCopyOfSbbAttachmentSet().iterator(); iter
				.hasNext();) {
			sbbEntityId = (String) iter.next();
			// check sbb entity is not on the delivery set
			if (sbbEntitiesThatHandledCurrentEvent.contains(sbbEntityId)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Already delivered event to sbbEntityId "
							+ sbbEntityId + ", skipping...");
				}
				continue;
			}
			try {
				sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
				// check event is allowed to be handled by the sbb
				if (sbbEntity.getSbbDescriptor().getReceivedEvents().contains(
						eventTypeID)) {
					return sbbEntity;
				} else {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Event is not received by sbb descriptor of entity "
										+ sbbEntityId + ", skipping...");
					}
					continue;
				}
			} catch (IllegalStateException e) {
				// ignore, sbb entity has been removed
				continue;
			}
		}

		return null;

	}
}
