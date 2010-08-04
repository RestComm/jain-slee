package org.mobicents.slee.runtime.activity;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.facilities.TimerID;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityIsEndingException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.event.ReferencesHandler;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.facilities.TimerFacility;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
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

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private static final Logger logger = Logger.getLogger(ActivityContext.class);

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
	
	private static final SbbEntityComparator sbbEntityComparator = new SbbEntityComparator(sleeContainer.getSbbEntityFactory());

	private final ActivityContextFactoryImpl factory;
		
	public ActivityContextImpl(final ActivityContextHandle activityContextHandle, ActivityContextCacheData cacheData, boolean updateAccessTime, Integer activityFlags,ActivityContextFactoryImpl factory) {
		this(activityContextHandle,cacheData,false,factory);
		// ac creation, create cache data and set activity flags
		this.cacheData.create();
		this.cacheData.putObject(NODE_MAP_KEY_ACTIVITY_FLAGS, activityFlags);
		if (updateAccessTime) {
			updateLastAccessTime(true);
		}
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (txContext != null) {
			if (ActivityFlags.hasRequestSleeActivityGCCallback(activityFlags) || activityContextHandle.getActivityType() == ActivityType.NULL) {
				// we need to schedule check for an unreferenced activity
				scheduleCheckForUnreferencedActivity(txContext);
			}
		}
	}
	
	public ActivityContextImpl(ActivityContextHandle activityContextHandle, ActivityContextCacheData cacheData, boolean updateAccessTime, ActivityContextFactoryImpl factory) {
		this.activityContextHandle = activityContextHandle;
		this.factory = factory;
		this.cacheData = cacheData;
		// update last acess time if needed	
		if (cacheData.exists() && updateAccessTime) {
			updateLastAccessTime(false);
		}
	}

	/**
	 * Retrieves the handle of this ac
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return activityContextHandle;
	}

	/**
	 * Retrieve the {@link ActivityFlags} for this activity context
	 * @return
	 */
	public int getActivityFlags() {
		Integer flags = (Integer) cacheData.getObject(NODE_MAP_KEY_ACTIVITY_FLAGS);
		if (flags != null) {
			return flags.intValue();
		}
		else {
			return ActivityFlags.NO_FLAGS;
		}
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
	 * @param key --
	 *            name of the shared data item.
	 * @param newValue --
	 *            value of the shared data item.
	 */
	public void setDataAttribute(String key, Object newValue) {
		cacheData.setCmpAttribute(key,newValue);
		if (logger.isDebugEnabled()) {
			logger.debug("Activity context with handle "+getActivityContextHandle()+" set cmp attribute named " + key
					+ " to value " + newValue);
		}
	}

	/**
	 * Get the shared data for the ACI.
	 * 
	 * @param name --
	 *            name we want to look up
	 * @return the shared data for the ACI
	 * 
	 */
	public Object getDataAttribute(String key) {
		return cacheData.getCmpAttribute(key);
	}
	
	@SuppressWarnings("unchecked")
	public Map getDataAttributes() {
		return cacheData.getCmpAttributesCopy();
	}

	/**
	 * add a naming binding to this activity context.
	 * 
	 * @param aciName -
	 *            new name binding to be added.
	 * 
	 */
	public void addNameBinding(String aciName) {
		cacheData.nameBound(aciName);
		// cancel a possible check for unreferenced activity, no need to
		// waste time in checkingif the flags requested such process 
		cacheData.setCheckingReferences(false);
	}

	/**
	 * This is called to release all the name bindings after
	 * the activity end event is delivered to the sbb.
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
				logger.warn("failed to unbind name: " + aciName
						+ " from ac:" + getActivityContextHandle(), e);
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
	 * @param aciName -- name to which we are bound.
	 * @return true if name bind was removed; false otherwise
	 * 
	 */
	public boolean removeNameBinding(String aciName) {
		boolean removed = cacheData.nameUnbound(aciName);
		if (removed && ActivityFlags.hasRequestSleeActivityGCCallback(getActivityFlags())) {
			scheduleCheckForUnreferencedActivity(sleeContainer.getTransactionManager().getTransactionContext());			
		}
		return removed;
	}

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID --
	 *            timer id to attach.
	 * 
	 */
	public boolean attachTimer(TimerID timerID) {
		if (cacheData.attachTimer(timerID)) {		
			// cancel a possible check for unreferenced activity, no need to
			// waste time in checkingif the flags requested such process 
			cacheData.setCheckingReferences(false);
			return true;
		}
		else {
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
		if (detached && ActivityFlags.hasRequestSleeActivityGCCallback(getActivityFlags())) {
			scheduleCheckForUnreferencedActivity(sleeContainer.getTransactionManager().getTransactionContext());			
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
			timerFacility.cancelTimer((TimerID)obj,false);
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
	 * @param sbbEntity --
	 *            sbb entity to attach.
	 * @return true if the SBB Entity is attached successfully, otherwise when
	 *         the SBB Entitiy has already been attached before, return false
	 */

	public boolean attachSbbEntity(String sbbEntityId) {

		boolean attached = cacheData.attachSbbEntity(sbbEntityId);
		if (attached) {
			// cancel a possible check for unreferenced activity, no need to
			// waste time in checking if the flags requested such process 
			cacheData.setCheckingReferences(false);
		}
		if (logger.isTraceEnabled()) {
			logger
					.trace("Attachement from sbb entity "+sbbEntityId+" to AC "+getActivityContextHandle()+" result: "+attached);
		}
		return attached;
	}

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(String sbbEntityId) throws javax.slee.TransactionRequiredLocalException {

		boolean detached = cacheData.detachSbbEntity(sbbEntityId);
		
		if (detached) {
			if (ActivityFlags.hasRequestSleeActivityGCCallback(getActivityFlags())) {
				scheduleCheckForUnreferencedActivity(sleeContainer.getTransactionManager().getTransactionContext());				
			}
			if (logger.isTraceEnabled()) {
				logger
				.trace("Detached sbb entity "+sbbEntityId+" from AC with handle "+getActivityContextHandle());
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
	@SuppressWarnings("unchecked")
	public Set<String> getSortedSbbAttachmentSet(Set<String> excludeSet) {
		final Set<String> sbbAttachementSet = cacheData.getSbbEntitiesAttached();
		final SortedSet orderSbbSet = new TreeSet(sbbEntityComparator);
		for (String sbbEntityId : sbbAttachementSet) {
			if (!excludeSet.contains(sbbEntityId)) {
				orderSbbSet.add(sbbEntityId);
			}
		}
		return orderSbbSet;
	}	
	
	@SuppressWarnings("unchecked")
	public Set<String> getSbbAttachmentSet() {
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
		if (factory.getConfiguration().getTimeBetweenLivenessQueries() > 0) {
			if (creation) {
				cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS, Long.valueOf(System.currentTimeMillis()));			
			}
			else {
				ActivityManagementConfiguration configuration = factory.getConfiguration();
				Long lastUpdate = (Long) cacheData.getObject(NODE_MAP_KEY_LAST_ACCESS);
				if (lastUpdate != null) {
					final long now = System.currentTimeMillis();
					if ((now - configuration.getMinTimeBetweenUpdatesInMs()) > lastUpdate.longValue()) {
						// last update
						if (logger.isTraceEnabled()) {
							logger.trace("Updating access time for AC with handle "+getActivityContextHandle());
						}
						cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS, Long.valueOf(now));
					}
					else {
						if (logger.isDebugEnabled()) {
							logger.debug("Skipping update of access time for AC with handle "+getActivityContextHandle());
						}
					}
				}
				else {
					cacheData.putObject(NODE_MAP_KEY_LAST_ACCESS, Long.valueOf(System.currentTimeMillis()));
					if (logger.isTraceEnabled()) {
						logger.trace("Updating access time for AC with handle "+getActivityContextHandle());
					}
				}
			}
		}
	}
	
	private static final Object MAP_VALUE = new Object();
	

	public String toString() {
		return new StringBuilder("ActivityContext{ handle = ").append(activityContextHandle).append(" }").toString();
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
			return ((ActivityContextImpl)obj).activityContextHandle.equals(this.activityContextHandle);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return activityContextHandle.hashCode();
	}

	// FIXME add logic to remove refs in facilities when activity is explicitely
	// ended. See example 7.3.4.1 in specs.
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContext#fireEvent(javax.slee.EventTypeID, java.lang.Object, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.EventProcessingSucceedCallback, org.mobicents.slee.container.event.EventProcessingFailedCallback, org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID, EventProcessingSucceedCallback succeedCallback, EventProcessingFailedCallback failedCallback, EventUnreferencedCallback unreferencedCallback) throws ActivityIsEndingException,
			SLEEException {
		
		if (isEnding()) {
			throw new ActivityIsEndingException(getActivityContextHandle().toString());
		}
		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (cacheData.setCheckingReferences(false) && txContext != null) {
			scheduleCheckForUnreferencedActivity(txContext);					
		}
		
		fireEvent(sleeContainer.getEventContextFactory().createEventContext(eventTypeId,event,this,address,serviceID,succeedCallback,failedCallback,unreferencedCallback),txContext);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContext#fireEvent(javax.slee.EventTypeID, java.lang.Object, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.ReferencesHandler)
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event,
			Address address, ServiceID serviceID, ReferencesHandler referencesHandler) throws ActivityIsEndingException,
			SLEEException {
		
		if (isEnding()) {
			throw new ActivityIsEndingException(getActivityContextHandle().toString());
		}
		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (cacheData.setCheckingReferences(false) && txContext != null) {
			scheduleCheckForUnreferencedActivity(txContext);					
		}
		
		fireEvent(sleeContainer.getEventContextFactory().createEventContext(eventTypeId,event,this,address,serviceID,referencesHandler),txContext);
	}
	
	/**
	 * Ends the activity context.
	 */
	public void endActivity() {
		if (logger.isDebugEnabled()) {
			logger.debug("Ending activity context with handle "+getActivityContextHandle());
		}
		if (cacheData.setEnding(true)) {
			fireEvent(sleeContainer.getEventContextFactory().createActivityEndEventContext(this, new ActivityEndEventUnreferencedCallback(getActivityContextHandle(),factory)),sleeContainer.getTransactionManager().getTransactionContext());	
		}	
	}
	
	public void activityEnded() {
		
		// remove references to this AC in timer and ac naming facility
		removeNamingBindings();
		removeFromTimers(); // Spec 7.3.4.1 Step 10
		
		TransactionContext txContext = null;
		// check activity type
		switch (activityContextHandle.getActivityType()) {
		
		case RA:
			// external activity, notify RA that the activity has ended
			final int activityFlags = getActivityFlags();
			((ResourceAdaptorActivityContextHandle)activityContextHandle).getResourceAdaptorEntity().activityEnded(activityContextHandle.getActivityHandle(),activityFlags);
			break;
		
		case NULL:
			// do nothing
			break;
			
		case PTABLE:
			// do nothing
			break;
			
		case SERVICE:
			sleeContainer.getServiceManagement().activityEnded((ServiceActivityHandle)this.getActivityContextHandle().getActivityHandle());
			
			break;

		default:
			throw new SLEEException("Unknown activity type " + activityContextHandle.getActivityType());
		}
		
		if (txContext == null) {
			txContext = sleeContainer.getTransactionManager().getTransactionContext();
		}
		removeFromCache(txContext);
		
		factory.removeActivityContext(this);
	}
	
	private void fireEvent(EventContext event,TransactionContext txContext) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Firing "+event);
		}
		
		final ActivityEventQueueManager aeqm = event.getLocalActivityContext().getEventQueueManager();
		if (aeqm != null) {
			if (txContext != null) {
				// put event as pending in ac event queue manager
				aeqm.pending(event);
				// add tx actions to commit or rollback
				txContext.getAfterCommitPriorityActions().add(new CommitEventContextAction(event, aeqm));
				txContext.getAfterRollbackActions().add(
						new RollbackEventContextAction(event,aeqm));
			}
			else {
				// commit event, there is no tx
				aeqm.fireNotTransacted(event);
			}
		} else {
			throw new SLEEException("unable to find ACs event queue manager");
		}		
	}
	
	private LocalActivityContextImpl localActivityContext;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContext#getLocalActivityContext()
	 */
	public LocalActivityContextImpl getLocalActivityContext() {
		if (localActivityContext == null) {
			localActivityContext = factory.getLocalActivityContext(activityContextHandle, true);
		}
		return localActivityContext;
	}
	
	// UNREF CHECK PROCESS
	
	/*
	 * There are 3 &quot;rounds&quot; to check if an activity is unreferenced:
	 * 
	 * 1st) in event handling if there is a sbb detach, cancel timer or name
	 * unbound and the activity flags require callback to activityUnreferenced then we add a tx action to check if the rules (except outstanding
	 * events) for an activity being unreferenced are satisfied.
	 * 
	 * 2nd) When the action is executed and if the rules are satisfied a
	 * runnable task to do another rules check is enqueued in the activity event
	 * executor service, thus serialized with events
	 * 
	 * 3rd a) If an event handling for this activity fires an event on it, attaches an sbb, sets a
	 * timer or binds a name then the whole process is canceled
	 * 
	 * 3rd b) When the runnable is executed, if the process was not
	 * canceled then the activity unreferenced callback is invoked
	 */
	
	// keys related with the procedure to check of the ac is unreferenced
	private static final String NODE_MAP_KEY_ActivityUnreferenced1stCheck = "unref-check-1";
	
	private String activityUnreferenced1stCheckKey = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContext#scheduleCheckForUnreferencedActivity()
	 */
	public void scheduleCheckForUnreferencedActivity() {
		TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (txContext != null) {
			scheduleCheckForUnreferencedActivity(txContext);
		}
	}
		
	@SuppressWarnings({ "unchecked" })
	private void scheduleCheckForUnreferencedActivity(final TransactionContext txContext) {
		
		boolean doTraceLog = logger.isTraceEnabled();
		
		if (doTraceLog) {
			logger.trace("scheduleCheckForUnreferencedActivity() ac = "+this.getActivityContextHandle());
		}
		if (!isEnding()) {
			
			if (activityUnreferenced1stCheckKey == null) {
				activityUnreferenced1stCheckKey = new StringBuilder(String.valueOf(activityContextHandle)).append(NODE_MAP_KEY_ActivityUnreferenced1stCheck).toString();
			}
			final Map txLocalData = txContext.getData();
			// schedule check only once at time
			if (txLocalData.containsKey(activityUnreferenced1stCheckKey)) {
				return;
			}
			else {
				// raise the 1st check flag to ensure that the check is not scheduled more than once
				txLocalData.put(activityUnreferenced1stCheckKey, MAP_VALUE);		
			}
			
			if (doTraceLog) {
				logger.trace("Schedule checking for unreferenced activity on ac "+this.getActivityContextHandle());
			}
			
			TransactionalAction implicitEndCheck = new TransactionalAction() {
				public void execute() {
					unreferencedActivity1stCheck(txContext);
				}
			};
			
			txContext.getBeforeCommitActions().add(implicitEndCheck);
		}	
	}
	
	private void unreferencedActivity1stCheck(TransactionContext txContext) {
		if (logger.isTraceEnabled()) {
			logger.trace("1st check for unreferenced activity on ac "+this.getActivityContextHandle());
		}
		
		if (!isEnding()) {
			if (this.isSbbAttachmentSetEmpty()
					&& this.isAttachedTimersEmpty()
					&& this.isNamingBindingEmpty()) {

				// raise the 2nd check flag
				cacheData.setCheckingReferences(true);
				
				// lets submit final check task to activity event executor after commit
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						getLocalActivityContext().getExecutorService()
						.execute(new UnreferencedActivity2ndCheckTask(ActivityContextImpl.this));
					}
				};
				txContext.getAfterCommitActions().add(action);				
			}
		}
	}
	
	public void unreferencedActivity2ndCheck() {
		// final verification
		if (cacheData.isCheckingReferences()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Activity Context with handle "+getActivityContextHandle() + " is now unreferenced");
			}
			switch (activityContextHandle.getActivityType()) {
			case RA:
				// external activity, notify RA that the activity is
				// unreferenced
				((ResourceAdaptorActivityContextHandle) activityContextHandle).getResourceAdaptorEntity()
						.getResourceAdaptorObject().activityUnreferenced(
								activityContextHandle.getActivityHandle());
				break;
			case NULL:
				// null activity unreferenced, end it
				endActivity();
				break;
			default:
				// do nothing
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContext#getActivityContextInterface()
	 */
	public ActivityContextInterfaceImpl getActivityContextInterface() {
		return new ActivityContextInterfaceImpl(this);
	}
	
}
