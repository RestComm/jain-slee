package org.mobicents.slee.runtime.activity;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.facilities.TimerID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.ActivityContextCacheData;
import org.mobicents.slee.runtime.eventrouter.PendingAttachementsMonitor;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
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

	private static final transient SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	private static final transient Logger logger = Logger.getLogger(ActivityContext.class);

	// --- map keys for attributes cached
	private static final transient String NODE_MAP_KEY_STATE = "state";
	
	/**
	 * This map contains mappings between acId and Long Objects. Long object represnt  timestamp of last access time to ac. 
	 * Cash is not a good place to hold this data - it is modified frequently  across tx causing rollbacks because of version errors. Values are first inserted when ac is created.
	 * There is posibility not to update or create this mapping by specifying <b>false</b> value to ActivityContext.createActivityContext(Object,boolean).
	 * This entry should be always updates if ac is used within core.
	 * Mapping are removed upon call of ActivityCOntext.markForRemoval function. However as this is static ap and we are
	 * acting in multithreaded env we cant be sure if this operation wasnt followed by some update (however tests didnt reveal this situation when trash is left),
	 * thus here is used WeakHashMap in which entries fade durign time. This is not a problem since ac should be accessed frequently, and if not we consider them old.
	 */
	private static ConcurrentHashMap<String, Long> timeStamps = new ConcurrentHashMap<String, Long>(500);
	
	/**
	 * the handle for this ac
	 */
	private ActivityContextHandle activityContextHandle;
	
	/**
	 * the node name for this object in the containers cache
	 */
	private String activityContextId;
	
	/**
	 * the data stored in cache for this ac
	 */
	protected ActivityContextCacheData cacheData;
	
	private transient SbbEntityFactory sbbEntityFactory;

	private transient SbbEntityComparator sbbEntityComparator;

	private void printNode() {
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.printNode() { \n"
					+ "activityContextHandle = " + activityContextHandle
					+ "nodeNameInCache = " + activityContextId
					+ "\nsbbAttachmentSet = "
					+ this.getSbbAttachmentSet().keySet()					
					+ "\nnamingBinding  = " + this.getNamingBindingCopy()
					+ "\nattachedTimers = " + this.getAttachedTimersCopy()
					+ "\nstate = " + this.getState() + "}");
		}
	}

	public ActivityContext(ActivityContextHandle activityContextHandle, String id, boolean updateAccessTime) {

		assert (activityContextHandle != null) : "activityContextHandle cannot be null";

		this.activityContextHandle = activityContextHandle;
		this.activityContextId = id;
		this.sbbEntityComparator = new SbbEntityComparator(sbbEntityFactory);
		// init cache data
		cacheData = sleeContainer.getCache().getActivityContextCacheData(id);
		if (!cacheData.exists()) {
			cacheData.create();
		}
		// update last acess time if needed	
		if (updateAccessTime) {
			updateLastAccessTime();
		}
		printNode();		
	}

	/**
	 * Retrieves the string id of this ac
	 * @return
	 */
	public String getActivityContextId() {
		return activityContextId;
	}
	
	/**
	 * Retrieves the handle of this ac
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return activityContextHandle;
	}

	/**
	 * get the state of the AC
	 * 
	 * @return the state.
	 */
	public ActivityContextState getState() {
		ActivityContextState acState = (ActivityContextState) cacheData.getObject(NODE_MAP_KEY_STATE);
		return (acState != null) ? acState : ActivityContextState.ACTIVE;
	}
	
	/**
	 * set the current state of the activity context
	 * 
	 * @param acState
	 */
	public void setState(ActivityContextState activityContextState) {
		
		cacheData.putObject(NODE_MAP_KEY_STATE,activityContextState);
		if (logger.isDebugEnabled()) {
			logger.debug("ac "+getActivityContextId()+" state set to " + activityContextState);
		}
	}

	/**
	 * test if the activity context is ending.
	 * 
	 * @return true if ending.
	 */
	public boolean isEnding() {
		return this.getState() == ActivityContextState.ENDING;
	}

	/**
	 * test if the AC is invalid
	 * 
	 * @return
	 */
	public boolean isInvalid() {
		return this.getState() == ActivityContextState.INVALID;
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
			logger.debug("ac "+getActivityContextId()+" cmp attribute set : attr name = " + key
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
	}

	/**
	 * This is called by the event router to release all the name bindings after
	 * the activity end event is delivered to the sbb.
	 * 
	 */
	public void removeNamingBindings() {
		ActivityContextNamingFacilityImpl acf = (ActivityContextNamingFacilityImpl) sleeContainer
				.getActivityContextNamingFacility();
		for (Object obj : cacheData.getNamesBoundCopy()) {	
			String aciName = (String) obj;
			try {
				acf.removeName(aciName);
			} catch (Exception e) {
				logger.warn("failed to unbind name: " + aciName
						+ " from ac:" + getActivityContextId(), e);
			}	
		}		
	}
	
	/**
	 * Fetches set of names given to this ac
	 * 
	 * @return Set containing String objects that are names of this ac
	 */
	public Set getNamingBindingCopy() {
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
		return cacheData.nameUnbound(aciName);		
	}

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID --
	 *            timer id to attach.
	 * 
	 */
	public void attachTimer(TimerID timerID) {
		cacheData.attachTimer(timerID);		
	}

	/**
	 * Detach timer
	 * 
	 * @param timerID
	 * 
	 */
	public boolean detachTimer(TimerID timerID) {
		return cacheData.detachTimer(timerID);
	}
	
	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	public Set getAttachedTimersCopy() {
		return cacheData.getAttachedTimersCopy();
	}
	
	// Spec Sec 7.3.4.1 Step 10. "The SLEE notifies the SLEE Facilities that
	// have references to the Activity Context that the Activ-ity
	// End Event has been delivered on the Activity Context.
	public void removeFromTimers() {
		TimerFacilityImpl timerFacility = sleeContainer.getTimerFacility();
		// Iterate through the attached timers, telling the timer facility to
		// remove them
		for (Object obj : cacheData.getAttachedTimersCopy()) {
			timerFacility.cancelTimer((TimerID)obj);
		}
	}
	
	/**
	 * Mark this AC for garbage collection. It can no longer be used past this
	 * point.
	 * 
	 */
	public void removeFromCache() {
		cacheData.remove();
		// Beyond here we dont stamp this one
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				ActivityContext.timeStamps.remove(activityContextId);
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterCommitAction(action);
		} catch (SystemException e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * Add the sbb entity id to our delivered set
	 * 
	 * @param sbbEid --
	 *            sbb entity id to add to the set.
	 * 
	 */
/*
	public void addToDeliveredSet(String sbbEid) {
		Set ds = (Set) cacheData.getObject(NODE_MAP_KEY_DELIVERED_SBB_SET);
		if (ds == null) {
			ds = new HashSet();
			cacheData.putObject(NODE_MAP_KEY_DELIVERED_SBB_SET,ds);
		}
		ds.add(sbbEid);
	}
	*/
	/**
	 * return true if the delviered set contains a given SbbEntity ID.
	 * 
	 * @param sbbEntityId
	 * @return
	 */
	/*
	public Set getDeliveredSet() {
		Set ds = (Set) cacheData.getObject(NODE_MAP_KEY_DELIVERED_SBB_SET);
		return ds == null ? emptySet : ds; 
	}
	private static final Set emptySet = new HashSet(0);
	
	public void clearDeliveredSet() {
		Set ds = (Set) cacheData.removeObject(NODE_MAP_KEY_DELIVERED_SBB_SET);
	}

	public boolean removeFromDeliveredSet(String sbbEntityId) {
		Set ds = (Set) cacheData.getObject(NODE_MAP_KEY_DELIVERED_SBB_SET);
		if (ds != null) {
			return ds.remove(sbbEntityId);
		}
		else {
			return false;
		}
	}	
*/
	/**
	 * attach an sbb entity to this AC.
	 * 
	 * @param sbbEntity --
	 *            sbb entity to attach.
	 * @return true if the SBB Entity is attached successfully, otherwise when
	 *         the SBB Entitiy has already been attached before, return false
	 * @throws javax.slee.TransactionRequiredLocalException
	 * @throws javax.slee.TransactionRolledbackLocalException
	 * @throws javax.slee.SLEEException
	 */

	public boolean attachSbbEntity(String sbbEntityId)
			throws javax.slee.TransactionRequiredLocalException {

		sleeContainer.getTransactionManager().mandateTransaction();

		boolean attached = cacheData.attachSbbEntity(sbbEntityId);
		if (attached) {
			PendingAttachementsMonitor pendingAttachementsMonitor = sleeContainer.getEventRouter().getEventRouterActivity(activityContextId).getPendingAttachementsMonitor();
			if (pendingAttachementsMonitor != null) {
				try {
					pendingAttachementsMonitor.txAttaching();
				} catch (SystemException e) {
					logger.error(e.getMessage(),e);
				}
			}	
		}
		if (logger.isDebugEnabled()) {
			logger
					.debug("attachement from sbb entity "+sbbEntityId+" to ac "+getActivityContextId()+" result: "+attached);
		}
		return attached;
	}

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(String sbbEntityId) throws javax.slee.TransactionRequiredLocalException {

		cacheData.detachSbbEntity(sbbEntityId);
		
		PendingAttachementsMonitor pendingAttachementsMonitor = sleeContainer.getEventRouter().getEventRouterActivity(activityContextId).getPendingAttachementsMonitor();
		if (pendingAttachementsMonitor != null) {
			try {
				pendingAttachementsMonitor.txDetaching();
			} catch (SystemException e) {
				logger.error(e.getMessage(),e);
			}
		}
				
		if (logger.isDebugEnabled()) {
			logger
					.debug("detached sbb entity "+sbbEntityId+" from ac "+getActivityContextId());
		}
	}

	/**
	 * get an ordered copy of the set of SBBs attached to this ac. The ordering
	 * is by SBB priority.
	 * 
	 * @return list of SbbEIDs
	 * 
	 */
	public List getSortedCopyOfSbbAttachmentSet() {
		Map sbbSet = new TreeMap(sbbEntityComparator);
		sbbSet.putAll(cacheData.getSbbEntitiesAttachedCopy());
		return new LinkedList(sbbSet.values());
	}	
	
	public Map getSbbAttachmentSet() {
		return cacheData.getSbbEntitiesAttachedCopy();	
	}
	
	// --- private helpers
		
	private class SbbEntityComparator implements Comparator {
		SbbEntityFactory sbbEntityFactory;

		SbbEntityComparator(SbbEntityFactory sbbEntityFactory) {
			this.sbbEntityFactory = sbbEntityFactory;
		}

		private Stack priorityOfSbb(SbbEntity sbbe) {
			Stack stack = new Stack();
			// push all non root sbb entities
			while (!sbbe.isRootSbbEntity()) {
				stack.push(sbbe);
				sbbe = SbbEntityFactory.getSbbEntity(sbbe
						.getParentSbbEntityId());
			}
			;
			// push the root one
			stack.push(sbbe);

			return stack;
		}

		public int compare(Object sbbeId1, Object sbbeId2) {

			// In case we are looking for this entry - this will save some
			// CPU cycles, cache readings etc.
			if (sbbeId1.equals(sbbeId2))
				return 0;
			SbbEntity sbbe1 = null;

			try {
				sbbe1 = SbbEntityFactory.getSbbEntity((String) sbbeId1);
			} catch (Exception e) {
				// ignore
			}
			SbbEntity sbbe2 = null;
			try {
				sbbe2 = SbbEntityFactory.getSbbEntity((String) sbbeId2);
			} catch (Exception e) {
				// ignore
			}
			if (sbbe1 == null) {
				if (sbbe2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (sbbe2 == null) {
					return -1;
				} else {
					return higherPrioritySbb(sbbe1, sbbe2);
				}
			}

		}

		private int higherPrioritySbb(SbbEntity sbbe1, SbbEntity sbbe2) {
			logger.debug("higherPrioritySbb " + sbbe1.getSbbId() + " "
					+ sbbe2.getSbbId());

			Stack stack1 = priorityOfSbb(sbbe1);
			Stack stack2 = priorityOfSbb(sbbe2);
			while (true) {
				SbbEntity sbb1a = (SbbEntity) stack1.pop();
				SbbEntity sbb2a = (SbbEntity) stack2.pop();
				if (sbb1a == sbb2a) {
					// sbb entities have the same ancestor.
					if (stack1.isEmpty()) {

						return -1;

					} else if (stack2.isEmpty()) {

						return 1;

					}
				} else {

					if (sbb1a.getPriority() > sbb2a.getPriority()) {

						return -1;

					} else if (sbb1a.getPriority() < sbb2a.getPriority()) {

						return 1;

					} else {

						return sbb1a.getSbbEntityId().compareTo(
								sbb2a.getSbbEntityId());
						// does it matter?
						// We need predicatble order to get TreeMap opeartional
						// and not returning some silly null values
						// So this will ensure that if SbbE are on the same leve
						// (childs) in SbbETree, and have the same priority
						// They will be stored alphabeticaly.
					}

				}
			}
		}
	}

	/**
	 * Returns time stamp of last access to this ac. If timestamp is found it
	 * return its value, if not "0"- it means that ac was accessed not during
	 * last few cycles, thus its value has faded.
	 * 
	 * @return
	 */
	public long getLastAccessTime() {
		Long l = (Long) ActivityContext.timeStamps.get(activityContextId);
		if (l != null)
			return l.longValue();
		else
			return 0;
	}

	private static final Object MAP_VALUE = new Object();
	
	void updateLastAccessTime() {

		// only update for resource adaptor activity contexts
		if (this.activityContextHandle.getActivityType() != ActivityType.externalActivity) {
			return;
		}
		
		// update once per tx, after commit
		try {
			final String txContextFlagKey = "ts:" + activityContextHandle;
			if (sleeContainer.getTransactionManager().getTransactionContext()
					.getData().containsKey(txContextFlagKey)) {
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						ActivityContext.timeStamps.put(activityContextId,
								new Long(System.currentTimeMillis()));
					}
				};
				sleeContainer.getTransactionManager().addAfterCommitAction(
						action);
				sleeContainer.getTransactionManager().getTransactionContext()
						.getData()
						.put(txContextFlagKey, MAP_VALUE);
			}
		}
		catch (SystemException e) {
			logger.error(e.getMessage(),e);
		}
	}

	public String toString() {
		return  "activity context : id=" + activityContextId;
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
			return ((ActivityContext)obj).activityContextId.equals(this.activityContextId);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return activityContextId.hashCode();
	}
	
	// FIXME add logic to remove refs in facilities when activity is explicitely
	// ended. See example 7.3.4.1 in specs.
	
	public static String dumpState() {
		return "\n+-- AC Timestamps Map size: "+timeStamps.size();
	}
	
}
