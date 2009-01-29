package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.TimerID;
import javax.slee.nullactivity.NullActivity;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * A special activity context, which is bound to a {@link NullActivity}.
 * @author martins
 *
 */
public class NullActivityContext extends ActivityContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static transient Logger logger = Logger
			.getLogger(NullActivityContext.class);
	
	/**
	 * unique key used to flag that an implicit null aci end check action has
	 * been scheduled on tx commit
	 */
	private static final String NODE_MAP_KEY_NullACEnd1stCheckKey = "nac-end-check-1";
	private static final String NODE_MAP_KEY_NullACEnd2ndCheckKey = "nac-end-check-2";
	private static final String NODE_MAP_KEY_NullACCreatedKey = "nac-creation";
	
	private static final Object MAP_VALUE = new Object();
	
	public NullActivityContext(ActivityContextHandle ach, String acId, boolean updateAccessTime) {
		super(ach,acId,updateAccessTime);
		if (cacheData.getObject(NODE_MAP_KEY_NullACCreatedKey) == null) {
			// ac creation, put flag in cache and schedule check for implicit end
			cacheData.putObject(NODE_MAP_KEY_NullACCreatedKey, MAP_VALUE);
			try {
				scheduleCheckForNullActivityImplicitEnd();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}			
		}				
	}
	
	@Override
	public boolean attachSbbEntity(String sbbEntityId)
			throws TransactionRequiredLocalException {
		boolean b = super.attachSbbEntity(sbbEntityId);
		if (b) {
			// a new attachment and a task to check for implicit removal may be queued, cancel it by setting the flag off
			cacheData.removeObject(NODE_MAP_KEY_NullACEnd2ndCheckKey);
		}
		return b;
	}
	
	public void detachSbbEntity(String sbbEntityId) {		
		super.detachSbbEntity(sbbEntityId);
		try {
			scheduleCheckForNullActivityImplicitEnd();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}
	
	@Override
	public void attachTimer(TimerID timerID) {
		super.attachTimer(timerID);
		// a new timer and a task to check for implicit removal may be queued, cancel it by setting the flag off
		cacheData.removeObject(NODE_MAP_KEY_NullACEnd2ndCheckKey);		
	}
	
	public boolean detachTimer(TimerID timerID) {
		if(super.detachTimer(timerID)) {
			try {
				scheduleCheckForNullActivityImplicitEnd();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void addNameBinding(String aciName) {
		super.addNameBinding(aciName);
		// a new name bound and a task to check for implicit removal may be queued, cancel it by setting the flag off
		cacheData.removeObject(NODE_MAP_KEY_NullACEnd2ndCheckKey);		
	}
	
	public boolean removeNameBinding(String aciName) {
		if(super.removeNameBinding(aciName)) {
			try {
				scheduleCheckForNullActivityImplicitEnd();
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			}
			return true;
		}		
		else {
			return false;
		}
	}
	
	private void scheduleCheckForNullActivityImplicitEnd() throws SystemException {
		
		if (this.getState() != ActivityContextState.ENDING) {
			
			// schedule of implicit check only once at  time
			if (cacheData.getObject(NODE_MAP_KEY_NullACEnd1stCheckKey) != null && cacheData.getObject(NODE_MAP_KEY_NullACEnd2ndCheckKey) != null) {
				return;
			}
			else {
				// raise the 1st check flag to ensure that the check is not scheduled more than once
				cacheData.putObject(NODE_MAP_KEY_NullACEnd1stCheckKey, MAP_VALUE);		
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("schedule checking for implicit end of null activity on ac "+this.getActivityContextId());
			}
			
			TransactionalAction implicitEndCheck = new TransactionalAction() {
				public void execute() {
					implicitEndFirstCheck();
				}
			};
			
			sleeContainer.getTransactionManager().addBeforeCommitAction(implicitEndCheck);
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
	 * @throws SystemException 
	 */
	private void implicitEndFirstCheck() {
		if (logger.isDebugEnabled()) {
			logger.debug("1st Checking for implicit end of null activity on ac "+this.getActivityContextId());
		}
		
		if (cacheData.getObject(NODE_MAP_KEY_NullACEnd2ndCheckKey) == null) {
			if (this.getState() == ActivityContextState.ACTIVE) {
				if (this.isSbbAttachmentSetEmpty()
						&& this.isAttachedTimersEmpty()
						&& this.isNamingBindingEmpty()) {

					// raise the 2nd check flag
					cacheData.putObject(NODE_MAP_KEY_NullACEnd2ndCheckKey, MAP_VALUE);
					// remove 1st level check flag
					cacheData.removeObject(NODE_MAP_KEY_NullACEnd1stCheckKey);
					
					// lets submit final check task to activity event executor after commit
					final String acId = getActivityContextId();
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							SleeContainer.lookupFromJndi().getEventRouter()
							.getEventRouterActivity(acId).getExecutorService()
							.submit(new NullActivityImplictEndTask(acId));
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
	}
	
	public void implicitEndSecondCheck() {
		// final verification task, remove activity		
		if (cacheData.getObject(NODE_MAP_KEY_NullACEnd2ndCheckKey) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("implicit null activity ending");
			}
			try {
				sleeContainer.getNullActivityFactory().endNullActivity(getActivityContextHandle());
			} catch (SystemException e) {
				logger.error("failed to implicit end null activity",e);
			}
		}
	}
	
	public void firingEvent() throws SystemException {
		if (cacheData.getObject(NODE_MAP_KEY_NullACEnd2ndCheckKey) != null) {
			// we are firing an event thus we need to cancel a possible implict end
			cacheData.removeObject(NODE_MAP_KEY_NullACEnd2ndCheckKey);
			// but this event may not be adding new attach, timer or name, so schedule the implict end 1st check again
			scheduleCheckForNullActivityImplicitEnd();
		}
	}
	
	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
}
