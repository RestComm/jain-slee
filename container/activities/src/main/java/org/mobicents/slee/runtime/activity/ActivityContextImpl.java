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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.TimerID;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityIsEndingException;

import org.apache.log4j.Logger;
import org.infinispan.tree.Node;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.runtime.event.ActivityEndEventUnreferencedCallback;
import org.mobicents.slee.runtime.event.CommitEventContextAction;
import org.mobicents.slee.runtime.event.RollbackEventContextAction;

/**
 * Create one of these when a new SipTransaction is seen by the stack. Call the
 * Slee Endpoint. This is a cached object so it is created from a factory
 * interface.
 * 
 * @author M. Ranganathan
 * @author F.Moggia
 * @author Tim - tx stuff
 * @author Ivelin Ivanov
 * @author eduardomartins
 * 
 */

public class ActivityContextImpl implements ActivityContext {

	static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

	private static final Logger logger = Logger
			.getLogger(ActivityContext.class);

	// --- map keys for attributes cached
	private static final String NODE_MAP_KEY_ACTIVITY_FLAGS = "flags";

	private static final String NODE_MAP_KEY_LAST_ACCESS = "time";

	/**
	 * the handle for this ac
	 */
	private final ActivityContextHandle activityContextHandle;

	/**
	 * the data stored in cache for this ac
	 */
	protected final ActivityContextCacheData cacheData;

	private final ActivityContextFactoryImpl factory;

	private Integer flags;

	private final ActivityContextReferencesHandler acReferencesHandler;

	public ActivityContextImpl(
			final ActivityContextHandle activityContextHandle,
			ActivityContextCacheData cacheData, boolean updateAccessTime,
			Integer activityFlags, ActivityContextFactoryImpl factory) {
		this.activityContextHandle = activityContextHandle;
		this.factory = factory;
		this.cacheData = cacheData;
		// ac creation, create cache data and set activity flags
		this.cacheData.create();
		this.cacheData.putObject(NODE_MAP_KEY_ACTIVITY_FLAGS, activityFlags);
		this.flags = activityFlags;
		// set access time if needed
		if (updateAccessTime) {
			updateLastAccessTime(true);
		}
		// setup references handler if needed
		if (ActivityFlags.hasRequestSleeActivityGCCallback(activityFlags)
				&& !isEnding()) {
			acReferencesHandler = new ActivityContextReferencesHandler(this);
		} else {
			acReferencesHandler = null;
		}
	}

	public ActivityContextImpl(ActivityContextHandle activityContextHandle,
			ActivityContextCacheData cacheData, boolean updateAccessTime,
			ActivityContextFactoryImpl factory) {
		this.activityContextHandle = activityContextHandle;
		this.factory = factory;
		this.cacheData = cacheData;
		// set access time if needed
		if (cacheData.exists() && updateAccessTime) {
			updateLastAccessTime(false);
		}
		// setup references handler if needed
		if (ActivityFlags.hasRequestSleeActivityGCCallback(getActivityFlags())
				&& !isEnding()) {
			acReferencesHandler = new ActivityContextReferencesHandler(this);
		} else {
			acReferencesHandler = null;
		}
	}

	/**
	 * Retrieves the handle of this ac
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return activityContextHandle;
	}

	/**
	 * Retrieve the {@link ActivityFlags} for this activity context
	 * 
	 * @return
	 */
	public int getActivityFlags() {
		if (flags == null) {
			// instance has no flags stored, check local ac
			if (localActivityContext != null) {
				flags = localActivityContext.getActivityFlags();
			} else {
				// local ac does not exists, get from cache
				flags = (Integer) cacheData
						.getObject(NODE_MAP_KEY_ACTIVITY_FLAGS);
			}
		}
		return flags != null ? flags : ActivityFlags.NO_FLAGS;
	}

	/**
	 * @return the factory
	 */
	public ActivityContextFactoryImpl getFactory() {
		return factory;
	}

	/**
	 * test if the activity context is ending.
	 * 
	 * @return true if ending.
	 */
	public boolean isEnding() {
		return cacheData.isEnding();
	}

	/**
	 * Set a shared data item for the ACI
	 * 
	 * @param key
	 *            -- name of the shared data item.
	 * @param newValue
	 *            -- value of the shared data item.
	 */
	public void setDataAttribute(String key, Object newValue) {
		cacheData.setCmpAttribute(key, newValue);
		if (logger.isDebugEnabled()) {
			logger.debug("Activity context with handle "
					+ getActivityContextHandle() + " set cmp attribute named "
					+ key + " to value " + newValue);
		}
	}

	/**
	 * Get the shared data for the ACI.
	 * 
	 * @param key
	 *            -- name we want to look up
	 * @return the shared data for the ACI
	 * 
	 */
	public Object getDataAttribute(String key) {
		return cacheData.getCmpAttribute(key);
	}

	@SuppressWarnings("rawtypes")
	public Map getDataAttributes() {
		return cacheData.getCmpAttributesCopy();
	}

	/**
	 * add a naming binding to this activity context.
	 * 
	 * @param aciName
	 *            - new name binding to be added.
	 * 
	 */
	public void addNameBinding(String aciName) {
		cacheData.nameBound(aciName);
		if (acReferencesHandler != null) {
			acReferencesHandler.nameReferenceCreated();
		}
	}

	/**
	 * This is called to release all the name bindings after the activity end
	 * event is delivered to the sbb.
	 * 
	 */
	private void removeNamingBindings() {
		ActivityContextNamingFacility acf = sleeContainer
				.getActivityContextNamingFacility();
		for (Object obj : cacheData.getNamesBoundCopy()) {
			String aciName = (String) obj;
			try {
				acf.removeName(aciName);
			} catch (Exception e) {
				logger.warn("failed to unbind name: " + aciName + " from ac:"
						+ getActivityContextHandle(), e);
			}
		}
	}

	/**
	 * Fetches set of names given to this ac
	 * 
	 * @return Set containing String objects that are names of this ac
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getNamingBindings() {
		return cacheData.getNamesBoundCopy();
	}

	/**
	 * Add the given name to the set of activity context names that we are bound
	 * to. The AC Naming facility implicitly ends the activity after all names
	 * are unbound.
	 * 
	 * @param aciName
	 *            -- name to which we are bound.
	 * @return true if name bind was removed; false otherwise
	 * 
	 */
	public boolean removeNameBinding(String aciName) {
		boolean removed = cacheData.nameUnbound(aciName);
		if (removed && acReferencesHandler != null) {
			acReferencesHandler.nameReferenceRemoved();
		}
		return removed;
	}

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID
	 *            -- timer id to attach.
	 * 
	 */
	public boolean attachTimer(TimerID timerID) {
		if (cacheData.attachTimer(timerID)) {
			if (acReferencesHandler != null) {
				acReferencesHandler.timerReferenceCreated();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Detach timer
	 * 
	 * @param timerID
	 * 
	 */
	public boolean detachTimer(TimerID timerID) {
		boolean detached = cacheData.detachTimer(timerID);
		if (detached && acReferencesHandler != null) {
			acReferencesHandler.timerReferenceRemoved();
		}
		return detached;
	}

	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	@SuppressWarnings("unchecked")
	public Set<TimerID> getAttachedTimers() {
		return cacheData.getAttachedTimers();
	}

	// Spec Sec 7.3.4.1 Step 10. "The SLEE notifies the SLEE Facilities that
	// have references to the Activity Context that the Activ-ity
	// End Event has been delivered on the Activity Context.
	private void removeFromTimers() {
		TimerFacility timerFacility = sleeContainer.getTimerFacility();
		// Iterate through the attached timers, telling the timer facility to
		// remove them
		for (Object obj : cacheData.getAttachedTimers()) {
			timerFacility.cancelTimer((TimerID) obj, false);
		}
	}

	/**
	 * Mark this AC for garbage collection. It can no longer be used past this
	 * point.
	 * 
	 */
	private void removeFromCache(TransactionContext txContext) {
		cacheData.remove();
	}

	/**
	 * attach an sbb entity to this AC.
	 * 
	 * @param sbbEntityId
	 *            -- sbb entity to attach.
	 * @return true if the SBB Entity is attached successfully, otherwise when
	 *         the SBB Entitiy has already been attached before, return false
	 */

	public boolean attachSbbEntity(SbbEntityID sbbEntityId) {

		boolean attached = cacheData.attachSbbEntity(sbbEntityId);
		if (attached) {
			if (acReferencesHandler != null) {
				acReferencesHandler.sbbeReferenceCreated(false);
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Attachement from sbb entity " + sbbEntityId
					+ " to AC " + getActivityContextHandle() + " result: "
					+ attached);
		}
		return attached;
	}

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(SbbEntityID sbbEntityId)
			throws javax.slee.TransactionRequiredLocalException {

		boolean detached = cacheData.detachSbbEntity(sbbEntityId);

		if (detached && acReferencesHandler != null && !isEnding()) {
			acReferencesHandler.sbbeReferenceRemoved();
			if (logger.isTraceEnabled()) {
				logger.trace("Detached sbb entity " + sbbEntityId
						+ " from AC with handle " + getActivityContextHandle());
			}
		}
	}

	/**
	 * get an ordered copy of the set of SBBs attached to this ac. The ordering
	 * is by SBB priority.
	 * 
	 * @return list of SbbEIDs
	 * 
	 */
	public Set<SbbEntityID> getSortedSbbAttachmentSet(
			Set<SbbEntityID> excludeSet) {
		final Set<SbbEntityID> sbbAttachementSet = cacheData
				.getSbbEntitiesAttached();
		Set<SbbEntityID> result = new HashSet<SbbEntityID>();
		for (SbbEntityID sbbEntityId : sbbAttachementSet) {
			if (!excludeSet.contains(sbbEntityId)) {
				result.add(sbbEntityId);
			}
		}
		if (result.size() > 1) {
			result = sleeContainer.getSbbEntityFactory().sortByPriority(result);
		}
		return result;
	}

	public Set<SbbEntityID> getSbbAttachmentSet() {
		return cacheData.getSbbEntitiesAttached();
	}

	/**
	 * 
	 * @return
	 */
	public long getLastAccessTime() {
		final Long time = (Long) cacheData.getObject(NODE_MAP_KEY_LAST_ACCESS);
		return time == null ? System.currentTimeMillis() : time.longValue();
	}

	// --- private helpers

	private void updateLastAccessTime(boolean creation) {
		if (creation) {
			cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS,
					Long.valueOf(System.currentTimeMillis()));
		} else {
			ActivityManagementConfiguration configuration = factory
					.getConfiguration();
			Long lastUpdate = (Long) cacheData
					.getObject(NODE_MAP_KEY_LAST_ACCESS);
			if (lastUpdate != null) {
				final long now = System.currentTimeMillis();
				if ((now - configuration.getMinTimeBetweenUpdatesInMs()) > lastUpdate
						.longValue()) {
					// last update
					if (logger.isTraceEnabled()) {
						logger.trace("Updating access time for AC with handle "
								+ getActivityContextHandle());
					}
					cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS,
							Long.valueOf(now));
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipping update of access time for AC with handle "
								+ getActivityContextHandle());
					}
				}
			} else {
				cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS,
						Long.valueOf(System.currentTimeMillis()));
				if (logger.isTraceEnabled()) {
					logger.trace("Updating access time for AC with handle "
							+ getActivityContextHandle());
				}
			}
		}
	}

	public String toString() {
		return new StringBuilder("ActivityContext{ handle = ")
				.append(activityContextHandle).append(" }").toString();
	}

	// emmartins: added to split null activity end related logic

	public boolean isSbbAttachmentSetEmpty() {
		return cacheData.noSbbEntitiesAttached();
	}

	public boolean isAttachedTimersEmpty() {
		return cacheData.noTimersAttached();
	}

	public boolean isNamingBindingEmpty() {
		return cacheData.noNamesBound();
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ActivityContextImpl) obj).activityContextHandle
					.equals(this.activityContextHandle);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return activityContextHandle.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.activity.ActivityContext#fireEvent(javax
	 * .slee.EventTypeID, java.lang.Object, javax.slee.Address,
	 * javax.slee.ServiceID,
	 * org.mobicents.slee.container.event.EventProcessingSucceedCallback,
	 * org.mobicents.slee.container.event.EventProcessingFailedCallback,
	 * org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback)
			throws ActivityIsEndingException, SLEEException {

		if (isEnding()) {
			throw new ActivityIsEndingException(getActivityContextHandle()
					.toString());
		}

		if (acReferencesHandler != null) {
			acReferencesHandler.eventReferenceCreated();
		}

		fireEvent(
				sleeContainer.getEventContextFactory().createEventContext(
						eventTypeId, event, this, address, serviceID,
						succeedCallback, failedCallback, unreferencedCallback),
				sleeContainer.getTransactionManager().getTransactionContext());
	}

	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID,
			EventContext reference)
			throws ActivityIsEndingException, SLEEException {

		if (isEnding()) {
			throw new ActivityIsEndingException(getActivityContextHandle()
					.toString());
		}

		if (acReferencesHandler != null) {
			acReferencesHandler.eventReferenceCreated();
		}

		fireEvent(
				sleeContainer.getEventContextFactory().createEventContext(
						eventTypeId, event, this, address, serviceID,
						reference), sleeContainer
						.getTransactionManager().getTransactionContext());
	}

	/**
	 * Ends the activity context.
	 */
	public void endActivity() {
		if (logger.isDebugEnabled()) {
			logger.debug("Ending activity context with handle "
					+ getActivityContextHandle());
		}
		if (cacheData.setEnding(true)) {
			fireEvent(
					sleeContainer
							.getEventContextFactory()
							.createActivityEndEventContext(
									this,
									new ActivityEndEventUnreferencedCallback(
											getActivityContextHandle(), factory)),
					sleeContainer.getTransactionManager()
							.getTransactionContext());
		}
	}

	public void activityEnded() {

		// remove references to this AC in timer and ac naming facility
		removeNamingBindings();
		removeFromTimers(); // Spec 7.3.4.1 Step 10
		final int activityFlags = activityContextHandle.getActivityType() == ActivityType.RA ? getActivityFlags()
				: -1;

		TransactionContext txContext = sleeContainer.getTransactionManager()
				.getTransactionContext();
		removeFromCache(txContext);
		factory.removeActivityContext(this);

		if (activityContextHandle.getActivityType() == ActivityType.RA) {
			// external activity, notify RA that the activity has ended
			((ResourceAdaptorActivityContextHandle) activityContextHandle)
					.getResourceAdaptorEntity().activityEnded(
							activityContextHandle.getActivityHandle(),
							activityFlags);
		} else if (activityContextHandle.getActivityType() == ActivityType.SERVICE) {
			sleeContainer.getServiceManagement().activityEnded(
					(ServiceActivityHandle) activityContextHandle
							.getActivityHandle());
		}

	}

	private void fireEvent(EventContext event, TransactionContext txContext) {

		if (logger.isDebugEnabled()) {
			logger.debug("Firing " + event);
		}

		final ActivityEventQueueManager aeqm = event.getLocalActivityContext()
				.getEventQueueManager();
		if (aeqm != null) {
			if (txContext != null) {
				// put event as pending in ac event queue manager
				aeqm.pending(event);
				// add tx actions to commit or rollback
				txContext.getAfterCommitPriorityActions().add(
						new CommitEventContextAction(event, aeqm));
				txContext.getAfterRollbackActions().add(
						new RollbackEventContextAction(event, aeqm));
			} else {
				// commit event, there is no tx
				aeqm.fireNotTransacted(event);
			}
		} else {
			throw new SLEEException("unable to find ACs event queue manager");
		}
	}

	private LocalActivityContextImpl localActivityContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContext#getLocalActivityContext
	 * ()
	 */
	public LocalActivityContextImpl getLocalActivityContext() {
		if (localActivityContext == null) {
			localActivityContext = factory.getLocalActivityContext(this);
		}
		return localActivityContext;
	}

	public void activityUnreferenced() {
		if (logger.isDebugEnabled()) {
			logger.debug("Activity Context with handle "
					+ activityContextHandle + " is now unreferenced");
		}
		switch (activityContextHandle.getActivityType()) {
		case RA:
			// external activity, notify RA that the activity is unreferenced
			((ResourceAdaptorActivityContextHandle) activityContextHandle)
					.getResourceAdaptorEntity()
					.getResourceAdaptorObject()
					.activityUnreferenced(
							activityContextHandle.getActivityHandle());
			break;
		case NULL:
			// null activity unreferenced, end it
			ActivityContext ac = sleeContainer.getActivityContextFactory()
					.getActivityContext(activityContextHandle);
			if (ac != null)
				ac.endActivity();
			break;
		default:
			// do nothing
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.runtime.activity.ActivityContext#
	 * getActivityContextInterface()
	 */
	public ActivityContextInterfaceImpl getActivityContextInterface() {
		return new ActivityContextInterfaceImpl(this);
	}

	public ActivityContextReferencesHandler getAcReferencesHandler() {
		return acReferencesHandler;
	}

	@Override
	public void beforeDeliveringEvent(EventContext ec) {
		if (acReferencesHandler != null) {
			// this means the ac is attached, thus we can indicate there a sbb
			// ref
			// to the refs handler, which means that more detachs must exist
			// than
			// attachs, for a unref state must be possible
			// BUT only if there is no sbb refs yet, cause otherwise it means
			// there was an
			// attachment before delivering the event, yep the sbb entity
			// receiving the event
			// the flag is to signal if this is special sbb reference due to
			// event delivery
			acReferencesHandler.sbbeReferenceCreated(true);
		}
	}
	
	@Override
	public String getStringID() {
		return getStringID(true);
	}
	
	public String getStringID(boolean createIfNull) {
		String sid = cacheData.getStringID();
		if (sid == null && createIfNull) {
			sid = sleeContainer.getUuidGenerator().createUUID();
			cacheData.setStringID(sid);
		}
		return sid;
	}
	
}
