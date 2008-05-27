package org.mobicents.slee.runtime.facilities;

import javax.slee.facilities.TimerID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextState;
import org.mobicents.slee.runtime.DeferredEvent;
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
	private static final String txLocalNullACEndCheckKey = "tx-local-flag-NullActivityContextImplicitEndCheck";
	private static final String txLocalNullACCreatedKey = "tx-local-flag-NullActivityContextCreation";
	
	private CacheableMap cacheMap;
	
	public NullActivityContext(String activityContextId, Object activity,boolean refreshAccessTime) {
		super(activityContextId,activity,refreshAccessTime);
		cacheMap = new CacheableMap(tcache + "-" + getNodeNameInCache());
		if (cacheMap.get(txLocalNullACCreatedKey) == null) {
			// ac creation, put flag in cache and schedule check for implicit end
			cacheMap.put(txLocalNullACCreatedKey, Boolean.TRUE);
			scheduleCheckForNullActivityImplicitEnd();
			
		}				
	}
	
	public void detachSbbEntity(String sbbEntityId) {		
		super.detachSbbEntity(sbbEntityId);
		scheduleCheckForNullActivityImplicitEnd();
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
		return new StringBuilder("null").append(super.getNodeNameInCache()).toString();
	}
	
	private void scheduleCheckForNullActivityImplicitEnd() {
		
		if (!this.getState().equals(ActivityContextState.ENDING)) {
			
			// the implicit check should be scheduled only once
			if (cacheMap.get(txLocalNullACEndCheckKey) != null) {
				return;
			}
			else {
				// raise the flag to ensure that the check is not scheduled more than once
				cacheMap.put(txLocalNullACEndCheckKey, Boolean.TRUE);		
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("schedule checking for implicit end of null activity on ac "+this.getActivityContextId());
			}
			
			TransactionalAction implicitEndCheck = new TransactionalAction() {
				public void execute() {
					checkForNullActivityImplicitEnd();
				}
			};
			
			getTxManager().addPrepareCommitAction(implicitEndCheck);
		}
		
	}
	
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
	private void checkForNullActivityImplicitEnd() {
		if (logger.isDebugEnabled()) {
			logger.debug("Checking for implicit end of null activity on ac "+this.getActivityContextId());
		}
				
		if (!this.isEnding() && !this.isInvalid()) {
			if (this.isSbbAttachmentSetEmpty()
					&& this.isAttachedTimersEmpty()
					&& this.isNamingBindingEmpty() && getOutstandingEvents() == 0) {
				
				// No refs
				if (logger.isDebugEnabled()) {
					logger.debug("scheduling activity end event");
				}
				
				final Object activity = this.getActivity();
				
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						Runnable runnable = new Runnable() {
							public void run() {
								SleeContainer.lookupFromJndi().getSleeEndpoint()
								.scheduleActivityEndedEvent(activity);
							}
						};
						SleeContainer.lookupFromJndi().getEventRouter().serializeTaskForActivity(runnable,activity);
					}
				};
				this.getTxManager().addAfterCommitAction(action);
			}
		}
		
		cacheMap.remove(txLocalNullACEndCheckKey);
		 
	}
	
	public void removeOutstandingEvent(DeferredEvent deferredEvent) {
		super.removeOutstandingEvent(deferredEvent);
		if (getOutstandingEvents() == 0) {
			scheduleCheckForNullActivityImplicitEnd();
		}
	}
	
	public void markForRemove() {
		super.markForRemove();
		cacheMap.remove();
	}
	
	
}
