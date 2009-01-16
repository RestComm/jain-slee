package org.mobicents.slee.runtime.activity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.TimerID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.eventrouter.PendingActivityContextAttachmentsManager;
import org.mobicents.slee.runtime.eventrouter.PendingAttachementsMonitor;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;
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

public class ActivityContext implements Serializable {

	private static final long serialVersionUID = 487857681918072300L;
	private static final transient SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	private static final transient SleeTransactionManager txManager = sleeContainer.getTransactionManager();
	private static final transient Logger logger = Logger.getLogger(ActivityContext.class);

	// --- cache data naming
	
	private static final transient String tcache = TransactionManagerImpl.RUNTIME_CACHE;
	
	private static final transient String ATTACHED_SBBs = tcache + ":AC:SbbsAttached:";
	private static final transient String ATTACHED_TIMERS = tcache + ":AC:AttachedTimers:";
	private static final transient String CMP_ATTRIBUTES = tcache + ":AC:CMPAttributes:";
	private static final transient String DATA_ATTRIBUTES = tcache + ":AC:DataAttributes:";
	private static final transient String NAMES_BOUND = tcache + ":AC:NamesBound:";
	
	private static final transient String DATA_ATTRIBUTE_STATE = "state";
	private static final transient String DATA_ATTRIBUTE_DELIVERED_SBB_SET = "deliveredSbbSet:";
	
	// --- time stamps
	
	/**
	 * This map contains mappings between acId and Long Objects. Long object represnt  timestamp of last access time to ac. 
	 * Cash is not a good place to hold this data - it is modified frequently  across tx causing rollbacks because of version errors. Values are first inserted when ac is created.
	 * There is posibility not to update or create this mapping by specifying <b>false</b> value to ActivityContext.createActivityContext(Object,boolean).
	 * This entry should be always updates if ac is used within core.
	 * Mapping are removed upon call of ActivityCOntext.markForRemoval function. However as this is static ap and we are
	 * acting in multithreaded env we cant be sure if this operation wasnt followed by some update (however tests didnt reveal this situation when trash is left),
	 * thus here is used WeakHashMap in which entries fade durign time. This is not a problem since ac should be accessed frequently, and if not we consider them old.
	 */
	private static ConcurrentHashMap<ActivityContextHandle, Long> timeStamps = new ConcurrentHashMap<ActivityContextHandle, Long>(500);
	
	// --- cache data
	
	// this is the distributed data structure which keeps a distributed cache
	// copy of an ActivityContext
	// the following section has the actual AC attributes that represent an AC
	// instance and are stored in the cache map
	private transient CacheableMap dataAttributes;

	// Set of timers that are attached to this ActivityContext
	private transient CacheableSet attachedTimers;

	// this should contain the sbb entity identities that are
	// attached to me.
	// a unique ordered list (aka Set). This map is ordered
	// from highest priority to lowest priority, with consideration of SbbEs
	// realations - child/parents
	// this is ensured by
	private transient CacheableMap attachedSbbs;

	// named AC bindings. Used by AC Naming facility
	private transient CacheableSet namesBound;

	// This stores the Activity Context specific data.
	// this stores the data that is shared between activities of the activity
	// context.
	private transient CacheableMap cmpAttributes;

	// --- non cached data

	// The activity for which this activity context was generated.
	private ActivityContextHandle activityContextHandle;
	
	private transient SbbEntityFactory sbbEntityFactory;

	private transient SbbEntityComparator sbbEntityComparator;

	/**
	 * flags whether the AC is marked for removal. It can no longer be used past
	 * that point. AC is marked for removal after ActivityEnd event has been
	 * delivered.
	 */
	private boolean isMarkedForRemoval = false;
	
	/**
	 * the node name for this object in the containers cache
	 */
	private String activityContextId;
	 
	private void printNode() {
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.printNode() { \n"
					+ "activityContextHandle = " + activityContextHandle
					+ "nodeNameInCache = " + activityContextId
					+ "\nsbbAttachmentSet = "
					+ this.getSbbAttachmentSetForDebug()
					+ "\ngetDeliveredSet() = " + this.getDeliveredSetForDebug()
					+ "\nnamingBinding  = " + this.getNamingBinding()
					+ "\nattachedTimers = " + this.getAttachedTimers()
					+ "\nstate = " + this.getState()
					+ "\ndata. = " + this.getData() + "}");
		}
	}

	public ActivityContext(ActivityContextHandle activityContextHandle, String id, boolean updateAccessTime) {

		assert (activityContextHandle != null) : "activityContextHandle cannot be null";

		this.activityContextHandle = activityContextHandle;
		this.activityContextId = id;
		this.sbbEntityComparator = new SbbEntityComparator(sbbEntityFactory);

		init(updateAccessTime);		
				
		printNode();		
	}

	public String getActivityContextId() {
		return activityContextId;
	}
	/**
	 * Associate this object instance with an underlying distributed cache
	 * structure
	 * 
	 */
	private void init(boolean updateAccessTime) {
		
		sleeContainer.getTransactionManager().mandateTransaction();

		this.dataAttributes = new CacheableMap(DATA_ATTRIBUTES	+ activityContextId);
		this.attachedSbbs = new CacheableMap(ATTACHED_SBBs + activityContextId);
		this.attachedTimers = new CacheableSet(ATTACHED_TIMERS + activityContextId);
		this.cmpAttributes = new CacheableMap(CMP_ATTRIBUTES + activityContextId);
		this.namesBound = new CacheableSet(NAMES_BOUND + activityContextId);
			
		Object o = dataAttributes.get(DATA_ATTRIBUTE_STATE);
		
		// if the AC is just being created, there would not be a cached copy
		if (o == null) {
			dataAttributes.put(DATA_ATTRIBUTE_STATE, ActivityContextState.ACTIVE);
			dataAttributes.put(DATA_ATTRIBUTE_DELIVERED_SBB_SET, new HashSet());
		}
		
		if (updateAccessTime) {
			this.updateLastAccessTime();
		}
		
	}

	/**
	 * return the node name under which this AC is cached.
	 * 
	 */
	public String getNodeNameInCache() {
		return activityContextId;
	}

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

		txManager.mandateTransaction();
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.attach() : " + sbbEntityId
					+ " activityContextId = " + activityContextHandle);
		}

		if (!getSbbAttachmentSet().containsKey(sbbEntityId)) {
			getSbbAttachmentSet().put(sbbEntityId, sbbEntityId);
			if (logger.isDebugEnabled()) {
				logger
						.debug("After Attach to Activity Context : Attachment Set = "
								+ this.getSbbAttachmentSet());
			}
			PendingAttachementsMonitor pendingAttachementsMonitor = sleeContainer.getEventRouter().getEventRouterActivity(activityContextHandle).getPendingAttachementsMonitor();
			if (pendingAttachementsMonitor != null) {
				pendingAttachementsMonitor.txAttaching();
			}			
			return true;
		} else {
			// we have a case of multiple consequent attachment attempts,
			// without detachment between them
			return false;
		}
	}

	/**
	 * Detach the sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public void detachSbbEntity(String sbbEntityId) throws javax.slee.TransactionRequiredLocalException {

		getSbbAttachmentSet().remove(sbbEntityId);

		PendingAttachementsMonitor pendingAttachementsMonitor = sleeContainer.getEventRouter().getEventRouterActivity(activityContextHandle).getPendingAttachementsMonitor();
		if (pendingAttachementsMonitor != null) {
			pendingAttachementsMonitor.txDetaching();
		}
				
		if (logger.isDebugEnabled()) {
			try {
				logger.debug("Detaching SbbEntity[ID:" + sbbEntityId
						+ "] \n" + " from ActivityContext[ID:"
						+ activityContextHandle + "]\n"
						+ " Remaining SbbEntities: "
						+ getSbbAttachmentSet().keySet() + "\n"
						+ " Transaction[ID:" + txManager.getTransaction()
						+ "]");
			} catch (SystemException e) {
				logger.warn("Failed to obtain transaction", e);
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
	public List getSortedCopyOfSbbAttachmentSet() {
		Map sbbSet = new TreeMap(sbbEntityComparator);
		Map sbbAttachmentSet = getSbbAttachmentSet();
		if (logger.isDebugEnabled()) {
			try {
				logger.debug("ActivityContext[ID:" + activityContextHandle + "],\n"
						+ "  SbbAttachmentSet: " + sbbAttachmentSet.keySet()
						+ ",\n  Transaction[ID:" + txManager.getTransaction()
						+ "]");
			} catch (SystemException e) {
				logger.warn("Failed to obtain transaction", e);
			}
		}

		sbbSet.putAll(sbbAttachmentSet);
		return new LinkedList(sbbSet.values());
	}

	/**
	 * get activity attached to this ac.
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return this.activityContextHandle;
	}

	/**
	 * set the current state of the activity context
	 * 
	 * @param acState
	 */
	public void setState(ActivityContextState activityContextState) {
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.setState( " + activityContextState
					+ " )");
		}
		this.setAcState(activityContextState);
	}

	/**
	 * test if the activity context is ending.
	 * 
	 * @return true if ending.
	 */
	public boolean isEnding() {
		if (logger.isDebugEnabled())
			logger.debug("ActivityContext.isEnding(): state = "
					+ this.getState());
		return this.getState() == ActivityContextState.ENDING;
	}

	/**
	 * test if the AC is invalid
	 * 
	 * @return
	 */
	public boolean isInvalid() {
		return (this.isMarkedForRemoval || this.getState() == ActivityContextState.INVALID);
	}

	/**
	 * get the state of the AC
	 * 
	 * @return the state.
	 */
	public ActivityContextState getState() {
		ActivityContextState acState = (ActivityContextState) dataAttributes
				.get(DATA_ATTRIBUTE_STATE);
		return (acState != null) ? acState : ActivityContextState.ACTIVE;
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
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.setDataAttribute(): " + key
					+ " value " + newValue);
		}
		cmpAttributes.put(key, newValue);
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
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.getDataAttribute(): " + key);
		}
		return cmpAttributes.get(key);
	}
	
	public Map getDataAttributesCopy() {
		// Is this safe?
		return new HashMap((Map) getData());

	}

	/**
	 * This is called by the event router to release all the name bindings after
	 * the activity end event is delivered to the sbb.
	 * 
	 */
	public void removeNamingBindings() {

		Iterator iterator = this.getNamingBinding().iterator();
		ActivityContextNamingFacilityImpl acf = (ActivityContextNamingFacilityImpl) this.sleeContainer
				.getActivityContextNamingFacility();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();

			try {

				acf.unbindWithoutCheck(name);

				// iterator.remove();
			} catch (Exception e) {
				logger.warn("Failed to unbind name: " + name
						+ " from activity context Id:" + activityContextHandle, e);
			}
		}

	}

	// Spec Sec 7.3.4.1 Step 10. "The SLEE notifies the SLEE Facilities that
	// have references to the Activity Context that the Activ-ity
	// End Event has been delivered on the Activity Context.
	public void removeFromTimers() {
		TimerFacilityImpl timerFacility = sleeContainer.getTimerFacility();
		// Iterate through the attached timers, telling the timer facility to
		// remove them
		Iterator iter = new HashSet(getAttachedTimers()).iterator();

		while (iter.hasNext()) {
			TimerID timerID = (TimerID) iter.next();
			timerFacility.cancelTimer(timerID);
		}
	}

	/**
	 * add a naming binding to this activity context.
	 * 
	 * @param aciName -
	 *            new name binding to be added.
	 * 
	 */
	public void addNameBinding(String aciName) {
		this.getNamingBinding().add(aciName);
	}

	/**
	 * Fetches set of names given to this ac
	 * 
	 * @return Set containing String objects that are names of this ac
	 */
	public Set getNamingBindingCopy() {
		return new HashSet(getNamingBinding());

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
		return this.getNamingBinding().remove(aciName);
	}

	/**
	 * attach the given timer to the current activity context.
	 * 
	 * @param timerID --
	 *            timer id to attach.
	 * 
	 */
	public void attachTimer(TimerID timerID) {
		if (logger.isDebugEnabled())
			logger.debug("attachTimer(" + timerID + ")");
		this.getAttachedTimers().add(timerID);
	}

	/**
	 * Add the sbb entity id to our delivered set
	 * 
	 * @param sbbEid --
	 *            sbb entity id to add to the set.
	 * 
	 */

	public void addToDeliveredSet(String sbbEid) {
		this.getDeliveredSet().add(sbbEid);
	}

	/**
	 * Mark this AC for garbage collection. It can no longer be used past this
	 * point.
	 * 
	 */
	public void markForRemove() {
		dataAttributes.remove();
		attachedTimers.remove();
		namesBound.remove();
		cmpAttributes.remove();
		attachedSbbs.remove();
		isMarkedForRemoval = true;
		// Beyond here we dont stamp this one
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				ActivityContext.timeStamps.remove(activityContextHandle);
			}
		};
		txManager.addAfterCommitAction(action);
	}

	/**
	 * return true if the delviered set contains a given SbbEntity ID.
	 * 
	 * @param sbbEntityId
	 * @return
	 */

	public boolean deliveredSetContains(String sbbEntityId) {
		return this.getDeliveredSet().contains(sbbEntityId);
	}

	public String getDeliveredSetForDebug() {
		return this.getDeliveredSet().toString();
	}

	public void clearDeliveredSet() {
		Set ds = (Set) dataAttributes.get(DATA_ATTRIBUTE_DELIVERED_SBB_SET);
		ds.clear();
		dataAttributes.put(DATA_ATTRIBUTE_DELIVERED_SBB_SET, ds);
	}

	public boolean removeFromDeliveredSet(String sbbEntityId) {
		return getDeliveredSet().remove(sbbEntityId);
	}

	/**
	 * Return a string containing the sbb entity ids attached.
	 * 
	 * @return
	 */
	public String getSbbAttachmentSetForDebug() {
		return this.getSbbAttachmentSet().values().toString();
	}

	/**
	 * Detach timer
	 * 
	 * @param timerID
	 * @param checkForActivityEnd
	 */
	public boolean detachTimer(TimerID timerID,
			boolean checkForActivityEnd) {

		if (logger.isDebugEnabled())
			logger.debug("detachTimer ( timerID= " + timerID
					+ ", checkForAcEnd =" + checkForActivityEnd + ")");

		return this.getAttachedTimers().remove(timerID);

	}
	
	/**
	 * Fetches set of attached timers.
	 * 
	 * @return Set containing TimerID objects representing timers attached to
	 *         this ac.
	 */
	public Set getAttachedTimersCopy() {
		return new HashSet(getAttachedTimers());
	}

	public Map getSbbAttachmentSet() {
		// List sbbAttachmentSet =
		// (List)activityContextCacheMap.get(SBB_ATTACHMENT_SET);
		return attachedSbbs;
	}
	
	public SbbEntity getSbbEntityAttachedWithHigherPriority(EventTypeID eventTypeID) {

		String sbbEntityId = null;
		SbbEntity sbbEntity = null;

		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator iter = getSortedCopyOfSbbAttachmentSet().iterator(); iter
				.hasNext();) {
			sbbEntityId = (String) iter.next();
			// check sbb entity is not on the delivery set
			if (deliveredSetContains(sbbEntityId)) {
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
	
	// --- private helpers
	
	private Object getData() {
		if (logger.isDebugEnabled()) {
			logger.debug("ActivityContext.getData()");
		}
		return cmpAttributes;
	}

	private Set getNamingBinding() {
		return namesBound;
	}

	private Set getDeliveredSet() {
		Set ds = (Set) dataAttributes.get(DATA_ATTRIBUTE_DELIVERED_SBB_SET);
		return ds;
	}
	
	private void setAcState(ActivityContextState acState) {
		dataAttributes.put(DATA_ATTRIBUTE_STATE, acState);
	}

	private Set getAttachedTimers() {
		return attachedTimers;
	}
	
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
		Long l = (Long) ActivityContext.timeStamps.get(activityContextHandle);
		if (l != null)
			return l.longValue();
		else
			return 0;
	}

	void updateLastAccessTime() {

		if (this.isMarkedForRemoval)
			return;
		
		// only update for resource adaptor activity contexts
		if (this.activityContextHandle.getActivityType() != ActivityType.externalActivity) {
			return;
		}
		
		// update once per tx, after commit
		if (txManager.getTxLocalData("ts:"+activityContextHandle) == null) {
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					ActivityContext.timeStamps.put(activityContextHandle, new Long(System.currentTimeMillis()));
				}
			};
			txManager.addAfterCommitAction(action);
			txManager.putTxLocalData("ts:"+activityContextHandle,new Object());
		}
	}

	public String toString() {
		return getClass().getName() + "[" + activityContextHandle + "]";
	}
	
	// emmartins: added to split null activity end related logic
	
	public SleeTransactionManager getTxManager() {
		return txManager;
	}	
	
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	public boolean isSbbAttachmentSetEmpty() {
		return getSbbAttachmentSet().isEmpty();
	}

	public boolean isAttachedTimersEmpty() {
		return getAttachedTimers().isEmpty();
	}
	
	public boolean isNamingBindingEmpty() {
		return getNamingBinding().isEmpty();
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
	
}
