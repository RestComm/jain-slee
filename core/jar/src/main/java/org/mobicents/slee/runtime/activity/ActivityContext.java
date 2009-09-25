package org.mobicents.slee.runtime.activity;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.facilities.TimerID;
import javax.slee.management.ServiceState;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityIsEndingException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceActivityImpl;
import org.mobicents.slee.runtime.eventrouter.ActivityEventQueueManager;
import org.mobicents.slee.runtime.eventrouter.CommitDeferredEventAction;
import org.mobicents.slee.runtime.eventrouter.DeferredActivityEndEvent;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.EventRouterActivity;
import org.mobicents.slee.runtime.eventrouter.PendingAttachementsMonitor;
import org.mobicents.slee.runtime.eventrouter.RollbackDeferredEventAction;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.runtime.sbbentity.SbbEntityComparator;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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

public class ActivityContext {

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private static final Logger logger = Logger.getLogger(ActivityContext.class);

	// --- map keys for attributes cached
	private static final String NODE_MAP_KEY_ACTIVITY_FLAGS = "flags"; 
	
	/**
	 * This map contains mappings between acId and Long Objects. Long object represnt  timestamp of last access time to ac. 
	 * Cash is not a good place to hold this data - it is modified frequently  across tx causing rollbacks because of version errors. Values are first inserted when ac is created.
	 * There is posibility not to update or create this mapping by specifying <b>false</b> value to ActivityContext.createActivityContext(Object,boolean).
	 * This entry should be always updates if ac is used within core.
	 * Mapping are removed upon call of ActivityCOntext.markForRemoval function. However as this is static ap and we are
	 * acting in multithreaded env we cant be sure if this operation wasnt followed by some update (however tests didnt reveal this situation when trash is left),
	 * thus here is used WeakHashMap in which entries fade durign time. This is not a problem since ac should be accessed frequently, and if not we consider them old.
	 */
	private static final ConcurrentHashMap<ActivityContextHandle, Long> timeStamps = new ConcurrentHashMap<ActivityContextHandle, Long>(500);
	
	/**
	 * the handle for this ac
	 */
	private final ActivityContextHandle activityContextHandle;
	
	/**
	 * the data stored in cache for this ac
	 */
	protected final ActivityContextCacheData cacheData;
	
	private static final SbbEntityComparator sbbEntityComparator = new SbbEntityComparator();

	public ActivityContext(final ActivityContextHandle activityContextHandle, ActivityContextCacheData cacheData, boolean updateAccessTime, Integer activityFlags) {
		this(activityContextHandle,cacheData,false);
		// ac creation, create cache data and set activity flags
		this.cacheData.create();
		this.cacheData.putObject(NODE_MAP_KEY_ACTIVITY_FLAGS, activityFlags);
		
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				updateLastAccessTime(activityContextHandle);				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterCommitAction(action);
			// then we need to schedule check for it
			if (ActivityFlags.hasRequestSleeActivityGCCallback(activityFlags) && activityContextHandle.getActivityType() == ActivityType.NULL) {
				scheduleCheckForUnreferencedActivity();
			}
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}	
	}
	
	public ActivityContext(ActivityContextHandle activityContextHandle, ActivityContextCacheData cacheData, boolean updateAccessTime) {
		this.activityContextHandle = activityContextHandle;
		this.cacheData = cacheData;
		// update last acess time if needed	
		if (updateAccessTime) {
			updateLastAccessTime(activityContextHandle);
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
			logger.debug("ac "+getActivityContextHandle()+" cmp attribute set : attr name = " + key
					+ " , attr value = " + newValue);
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
	
	public Map getDataAttributesCopy() {
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
		ActivityContextNamingFacilityImpl acf = (ActivityContextNamingFacilityImpl) sleeContainer
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
	public Set<String> getNamingBindingCopy() {
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
			try {
				scheduleCheckForUnreferencedActivity();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
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
			try {
				scheduleCheckForUnreferencedActivity();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
		}
		return detached;
	}
	
	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	public Set<TimerID> getAttachedTimersCopy() {
		return cacheData.getAttachedTimers();
	}
	
	// Spec Sec 7.3.4.1 Step 10. "The SLEE notifies the SLEE Facilities that
	// have references to the Activity Context that the Activ-ity
	// End Event has been delivered on the Activity Context.
	private void removeFromTimers() {
		TimerFacilityImpl timerFacility = sleeContainer.getTimerFacility();
		// Iterate through the attached timers, telling the timer facility to
		// remove them
		for (Object obj : cacheData.getAttachedTimers()) {
			timerFacility.cancelTimer((TimerID)obj);
		}
	}
	
	/**
	 * Mark this AC for garbage collection. It can no longer be used past this
	 * point.
	 * 
	 */
	private void removeFromCache() {
		cacheData.remove();
		// Beyond here we dont stamp this one
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				ActivityContext.timeStamps.remove(activityContextHandle);
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterCommitAction(action);
		} catch (SystemException e) {
			logger.error(e.getMessage(),e);
		}
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
			PendingAttachementsMonitor pendingAttachementsMonitor = getEventRouterActivity().getPendingAttachementsMonitor();
			if (pendingAttachementsMonitor != null) {
				try {
					pendingAttachementsMonitor.txAttaching();
				} catch (SystemException e) {
					logger.error(e.getMessage(),e);
				}
			}	
			// cancel a possible check for unreferenced activity, no need to
			// waste time in checkingif the flags requested such process 
			cacheData.setCheckingReferences(false);
		}
		if (logger.isDebugEnabled()) {
			logger
					.debug("attachement from sbb entity "+sbbEntityId+" to ac "+getActivityContextHandle()+" result: "+attached);
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
				try {
					scheduleCheckForUnreferencedActivity();
				} catch (SystemException e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
			PendingAttachementsMonitor pendingAttachementsMonitor = getEventRouterActivity().getPendingAttachementsMonitor();
			if (pendingAttachementsMonitor != null) {
				try {
					pendingAttachementsMonitor.txDetaching();
				} catch (SystemException e) {
					logger.error(e.getMessage(),e);
				}
			}
			if (logger.isDebugEnabled()) {
				logger
						.debug("detached sbb entity "+sbbEntityId+" from ac "+getActivityContextHandle());
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
	public Set getSortedSbbAttachmentSet(Set excludeSet) {
		final Set<String> sbbAttachementSet = cacheData.getSbbEntitiesAttached();
		final SortedSet orderSbbSet = new TreeSet(sbbEntityComparator);
		for (String sbbEntityId : sbbAttachementSet) {
			if (!excludeSet.contains(sbbEntityId)) {
				orderSbbSet.add(sbbEntityId);
			}
		}
		return orderSbbSet;
	}	
	
	public Set<String> getSbbAttachmentSet() {
		return cacheData.getSbbEntitiesAttached();	
	}
	
	// --- private helpers

	/**
	 * Returns time stamp of last access to this ac. If timestamp is found it
	 * return its value, if not "0"- it means that ac was accessed not during
	 * last few cycles, thus its value has faded.
	 * 
	 * @return
	 */
	public long getLastAccessTime() {
		final Long l = timeStamps.get(activityContextHandle);
		if (l != null)
			return l.longValue();
		else
			return 0;
	}

	private void updateLastAccessTime(ActivityContextHandle activityContextHandle) {
		timeStamps.put(activityContextHandle,Long.valueOf(System.currentTimeMillis()));		
	}
	
	private static final Object MAP_VALUE = new Object();
	

	public String toString() {
		return  "ActivityContext{ handle = "+activityContextHandle+" }";
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
			return ((ActivityContext)obj).activityContextHandle.equals(this.activityContextHandle);
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
	
	public static String dumpState() {
		return "\n+-- AC Timestamps Map size: "+timeStamps.size();
	}
		
	/**
	 * Fires an event on this AC
	 * @param eventTypeId
	 * @param event
	 * @param address
	 * @param serviceID
	 * @param eventFlags
	 * @throws ActivityIsEndingException
	 * @throws SLEEException
	 */
	public void fireEvent(EventTypeID eventTypeId, Object event, Address address, ServiceID serviceID, int eventFlags) throws ActivityIsEndingException, SLEEException {
		if (isEnding()) {
			throw new ActivityIsEndingException(getActivityContextHandle().toString());           		
		} 
		else {
			// cancel a possible check for unreferenced activity, no need to
			// waste time in checking if the flags requested such process 
			if (cacheData.setCheckingReferences(false)) {
				try {
					scheduleCheckForUnreferencedActivity();
				} catch (SystemException e) {
					throw new SLEEException(e.getMessage(),e);
				}	
			}
			// fire event
			fireDeferredEvent(new DeferredEvent(eventTypeId,event,this,address,serviceID,eventFlags,getEventRouterActivity(),sleeContainer));			
		}      
	}
	
	/**
	 * Ends the activity context.
	 */
	public void endActivity() {
		if (logger.isDebugEnabled()) {
			logger.debug("Ending ac "+this);
		}
		if (cacheData.setEnding(true)) {
			fireDeferredEvent(new DeferredActivityEndEvent(this,getEventRouterActivity(),sleeContainer));	
		}	
	}
	
	public void activityEnded() {
		
		// remove references to this AC in timer and ac naming facility
		removeNamingBindings();
		removeFromTimers(); // Spec 7.3.4.1 Step 10
		
		// check activity type
		switch (activityContextHandle.getActivityType()) {
		
		case RA:
			// external activity, notify RA that the activity has ended
			final int activityFlags = getActivityFlags();
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						sleeContainer.getResourceManagement().getResourceAdaptorEntity(activityContextHandle.getActivitySource()).activityEnded(activityContextHandle.getActivityHandle(),activityFlags);
					}
					catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}					
				}
			};
			try {
				sleeContainer.getTransactionManager().addAfterCommitAction(action);
			} catch (Throwable e) {
				logger.error("error adding tx action to tx manager, to inform ra entity of activity end, executing action now",e);
				action.execute();
			}			
			break;
		
		case NULL:
			// do nothing
			break;
			
		case PTABLE:
			// do nothing
			break;
			
		case SERVICE:
			ServiceActivityImpl serviceActivity = (ServiceActivityImpl) activityContextHandle.getActivity();			
			
			try {
				// change service state to inactive if it is stopping
				Service service = sleeContainer.getServiceManagement().getService(serviceActivity.getService());
				if (service.getState().isStopping()) {
					service.setState(ServiceState.INACTIVE);
					// schedule task to remove outstanding root sbb entities of the service
					new RootSbbEntitiesRemovalTask(serviceActivity.getService());
					Logger.getLogger(ServiceManagement.class).info("Deactivated "+ serviceActivity.getService());
				}
			} catch (UnrecognizedServiceException e) {
				logger.error("Unable to find "+serviceActivity.getService()+" to deactivate",e);
			}
			
			break;

		default:
			throw new SLEEException("Unknown activity type " + activityContextHandle.getActivityType());
		}
		
		removeFromCache();
	}
	
	private void fireDeferredEvent(DeferredEvent dE) {
		// put event as pending in ac event queue manager
		ActivityEventQueueManager aeqm = dE.getEventRouterActivity().getEventQueueManager();
		if (aeqm != null) {
			aeqm.pending(dE);
			// add tx actions to commit or rollback
			final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
			try {
				txManager.addAfterCommitPriorityAction(
						new CommitDeferredEventAction(dE, aeqm));
				txManager.addAfterRollbackAction(
						new RollbackDeferredEventAction(dE,
								this.activityContextHandle));
			} catch (SystemException e) {
				aeqm.rollback(dE);
				throw new SLEEException("failed to add tx actions to commit and rollback event firing",e);
			}			
		} else {
			throw new SLEEException("unable to find ACs event queue manager");
		}		
	}
	
	public EventRouterActivity getEventRouterActivity() {
		return sleeContainer.getEventRouter().getEventRouterActivity(activityContextHandle);
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
	
	private void scheduleCheckForUnreferencedActivity() throws SystemException {
		
		if (!isEnding()) {
			
			String activityUnreferenced1stCheckKey = activityContextHandle + NODE_MAP_KEY_ActivityUnreferenced1stCheck;
			Map txLocalData = sleeContainer.getTransactionManager().getTransactionContext().getData();
			// schedule check only once at time
			if (txLocalData.get(activityUnreferenced1stCheckKey) != null) {
				return;
			}
			else {
				// raise the 1st check flag to ensure that the check is not scheduled more than once
				txLocalData.put(activityUnreferenced1stCheckKey, MAP_VALUE);		
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("schedule checking for unreferenced activity on ac "+this.getActivityContextHandle());
			}
			
			TransactionalAction implicitEndCheck = new TransactionalAction() {
				public void execute() {
					unreferencedActivity1stCheck();
				}
			};
			
			sleeContainer.getTransactionManager().addBeforeCommitAction(implicitEndCheck);
		}	
	}
	
	private void unreferencedActivity1stCheck() {
		if (logger.isDebugEnabled()) {
			logger.debug("1st check for unreferenced activity on ac "+this.getActivityContextHandle());
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
						getEventRouterActivity().getExecutorService()
						.submit(new UnreferencedActivity2ndCheckTask(getActivityContextHandle()));
					}
				};
				try {
					sleeContainer.getTransactionManager().addAfterCommitAction(action);
				} catch (SystemException e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
	}
	
	protected void unreferencedActivity2ndCheck() {
		// final verification
		if (cacheData.isCheckingReferences()) {
			if (logger.isDebugEnabled()) {
				logger.debug(toString() + " is unreferenced");
			}
			switch (activityContextHandle.getActivityType()) {
			case RA:
				// external activity, notify RA that the activity is
				// unreferenced
				sleeContainer.getResourceManagement().getResourceAdaptorEntity(
						activityContextHandle.getActivitySource())
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
	
}
