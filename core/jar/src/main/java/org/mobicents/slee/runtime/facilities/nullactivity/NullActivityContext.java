package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.TimerID;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class NullActivityContext extends ActivityContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static transient Logger logger = Logger
			.getLogger(NullActivityContext.class);
	
	private static final String tcache = TransactionManagerImpl.RUNTIME_CACHE;
	
	/**
	 * unique key used to flag that an implicit null aci end check action has
	 * been scheduled on tx commit
	 */
	private static final String txLocalNullACEnd1stCheckKey = "tx-local-flag-NullActivityContextImplicitEndCheck-1stcheck";
	private static final String txLocalNullACEnd2ndCheckKey = "tx-local-flag-NullActivityContextCreation-2ndcheck";
	private static final String txLocalNullACCreatedKey = "tx-local-flag-NullActivityContextCreation";
	private static final Object MAP_VALUE = new Object();
	
	private CacheableMap cacheMap;
	
	public NullActivityContext(ActivityContextHandle ach, String acId, boolean updateAccessTime) {
		super(ach,acId,updateAccessTime);
		cacheMap = new CacheableMap(tcache + "-" + getNodeNameInCache());
		if (cacheMap.get(txLocalNullACCreatedKey) == null) {
			// ac creation, put flag in cache and schedule check for implicit end
			cacheMap.put(txLocalNullACCreatedKey, MAP_VALUE);
			scheduleCheckForNullActivityImplicitEnd();			
		}				
	}
	
	@Override
	public boolean attachSbbEntity(String sbbEntityId)
			throws TransactionRequiredLocalException {
		boolean b = super.attachSbbEntity(sbbEntityId);
		if (b && cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
			// a new attachment and a task to check for implicit removal is queued, cancel it by setting the flag off
			cacheMap.remove(txLocalNullACEnd2ndCheckKey);
		}
		return b;
	}
	
	public void detachSbbEntity(String sbbEntityId) {		
		super.detachSbbEntity(sbbEntityId);
		scheduleCheckForNullActivityImplicitEnd();
	}
	
	@Override
	public void attachTimer(TimerID timerID) {
		super.attachTimer(timerID);
		if (cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
			// a new timer and a task to check for implicit removal is queued, cancel it by setting the flag off
			cacheMap.remove(txLocalNullACEnd2ndCheckKey);
		}
	}
	
	public boolean detachTimer(TimerID timerID, boolean checkForActivityEnd) {
		if(super.detachTimer(timerID, checkForActivityEnd)) {
			scheduleCheckForNullActivityImplicitEnd();
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void addNameBinding(String aciName) {
		super.addNameBinding(aciName);
		if (cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
			// a new name bound and a task to check for implicit removal is queued, cancel it by setting the flag off
			cacheMap.remove(txLocalNullACEnd2ndCheckKey);
		}
	}
	
	public boolean removeNameBinding(String aciName) {
		if(super.removeNameBinding(aciName)) {
			scheduleCheckForNullActivityImplicitEnd();
			return true;
		}		
		else {
			return false;
		}
	}
	
	public String getNodeNameInCache() {		
		return "null"+ super.getNodeNameInCache();
	}
	
	private void scheduleCheckForNullActivityImplicitEnd() {
		
		
		
		if (this.getState() != ActivityContextState.ENDING) {
			
			// schedule of implicit check only once at  time
			if (cacheMap.get(txLocalNullACEnd1stCheckKey) != null && cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
				return;
			}
			else {
				// raise the 1st check flag to ensure that the check is not scheduled more than once
				cacheMap.put(txLocalNullACEnd1stCheckKey, MAP_VALUE);		
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("schedule checking for implicit end of null activity on ac "+this.getActivityContextHandle());
			}
			
			TransactionalAction implicitEndCheck = new TransactionalAction() {
				public void execute() {
					implicitEndFirstCheck();
				}
			};
			
			getTxManager().addPrepareCommitAction(implicitEndCheck);
		}
		
	}
	
	/*
	 * There are 3 &quot;rounds&quot; to implicitly remove a null activity:
	 * 
	 * 1st) in event handling if there is a sbb detach, cancel timer or name
	 * unbound we add a tx action to check if the rules (except outstanding
	 * events) for implicit removal are satisfied.
	 * 
	 * 2nd) When the action is executed and if the rules are satisfied a
	 * runnable task to do another rules check is enqueued in the activity event
	 * executor service, thus serialized with events
	 * 
	 * 3rd a) If an event handling for this activity fires an event on it, attaches an sbb, sets a
	 * timer or binds a name then the implicit removal is canceled
	 * 
	 * 3rd b) When the runnable is executed, if the implicit removal was not
	 * canceled then the activity is ended
	 */
	
	/**
	 * 
	 * If the activity is a null activity it implicitly ends when there are no
	 * sbbs or facilities (timer or naming) attached to it at the end of an
	 * ongoing transaction.
	 * 
	 * <pre>
	 *      
	 *       From JSLEE 1.0 Spec #7.10.5.1
	 *       7.10.5.1 Implicitly ending a NullActivity object
	 *      	The condition that implicitly ends a NullActivity object is as follows:
	 *      	 ‡ No SBB entities are attached to the Activity Context of the NullActivity object, and
	 *      	 ‡ No SLEE Facilities reference the Activity Context of the NullActivity object, and
	 *      	 ‡ No events remain to be delivered on the Activity Context of the NullActivity object.
	 *      
	 *      	All methods that may change the attachments or references of an Activity Context are mandatory transactional.
	 *      	Hence, if an SBB method invocation changes an Activity ContextÍs attachments and references, it
	 *      	must do so within a transaction. Therefore, when the transaction commits, the SLEE only has to examine
	 *      	the NullActivity objects whose Activity Contexts were enrolled in the transaction to determine which
	 *      	NullActivity objects should end.
	 * </pre>
	 */
	private void implicitEndFirstCheck() {
		if (logger.isDebugEnabled()) {
			logger.debug("1st Checking for implicit end of null activity on ac "+this.getActivityContextHandle());
		}
		
		if (cacheMap.get(txLocalNullACEnd2ndCheckKey) == null) {
			if (this.getState() == ActivityContextState.ACTIVE) {
				if (this.isSbbAttachmentSetEmpty()
						&& this.isAttachedTimersEmpty()
						&& this.isNamingBindingEmpty()) {

					// raise the 2nd check flag
					cacheMap.put(txLocalNullACEnd2ndCheckKey, MAP_VALUE);
					// remove 1st level check flag
					cacheMap.remove(txLocalNullACEnd1stCheckKey);
					
					// lets submit final check task to activity event executor after commit
					final ActivityContextHandle ach = getActivityContextHandle();
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							SleeContainer.lookupFromJndi().getEventRouter()
							.getEventRouterActivity(ach).getExecutorService()
							.submit(new NullActivityImplictEndTask(ach));
						}
					};
					
					getTxManager().addAfterCommitAction(action);
					
				}
			}
		}
	}
	
	public void implicitEndSecondCheck() {
		// final verification task, remove activity		
		if (cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("implicit null activity ending");
			}
			try {
				SleeContainer.lookupFromJndi().getNullActivityFactory().endNullActivity((NullActivityHandle)getActivityContextHandle().getActivityHandle());
			} catch (SystemException e) {
				logger.error("failed to implicit end null activity",e);
			}
		}
	}
	
	public void firingEvent() {
		if (cacheMap.get(txLocalNullACEnd2ndCheckKey) != null) {
			// we are firing an event thus we need to cancel a possible implict end
			cacheMap.remove(txLocalNullACEnd2ndCheckKey);
			// but this event may not be adding new attach, timer or name, so schedule the implict end 1st check again
			scheduleCheckForNullActivityImplicitEnd();
		}
	}
	
	public void markForRemove() {
		super.markForRemove();
		cacheMap.remove();
	}
	
}
