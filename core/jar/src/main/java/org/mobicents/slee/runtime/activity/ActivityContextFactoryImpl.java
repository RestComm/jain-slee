/*
 * Created on Jul 8, 2004
 *
 * The Open SLEE project.
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.runtime.activity;

import java.util.Set;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.ActivityContextFactoryCacheData;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Activity context factory -- return an activity context given an activity or
 * create one and put it in the table. This also implements the activity context
 * naming facility for the SLEE.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Tim Fox
 * @author eduardomartins second version
 * @version 2.0
 * 
 * 
 */
public class ActivityContextFactoryImpl implements ActivityContextFactory {

	private static Logger logger = Logger.getLogger(ActivityContextFactoryImpl.class);
	
	/**
	 * the container for this factory
	 */
	private final SleeContainer sleeContainer;
	
	private ActivityContextFactoryCacheData cacheData;
	
	public ActivityContextFactoryImpl(SleeContainer sleeContainer) {		
		this.sleeContainer = sleeContainer;		
		cacheData = sleeContainer.getCache().getActivityContextFactoryCacheData();
		if (!cacheData.exists()) {
			cacheData.create();
		}
	}
	
	private String createActivityContextId(ActivityContextHandle ach) {
		String acId = null;
		if (ach.getActivityType() != ActivityType.nullActivity) {
			acId = "ac:" + sleeContainer.getUuidGenerator().createUUID();
		}
		else {
			// lets reuse its id
			acId = "ac:" + ((NullActivityHandle) ach.getActivityHandle()).getId();			
		}	
		return acId;
	}
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach) throws ActivityAlreadyExistsException {
		return createActivityContext(ach,ActivityFlags.NO_FLAGS);
	}

	public ActivityContext createActivityContext(final ActivityContextHandle ach, int activityFlags) throws ActivityAlreadyExistsException {
		
		if (cacheData.getActivityContextId(ach) != null) {
			throw new ActivityAlreadyExistsException(ach.toString()); 
		}
		// create id and map it to the ac handle
		final String acId = createActivityContextId(ach);
		cacheData.addActivityContext(ach, acId);
		// create ac
		ActivityContext ac = new ActivityContext(ach,acId,true,Integer.valueOf(activityFlags));
		if (logger.isDebugEnabled()) {
			logger.debug("Created ac "+ac+" for handle "+ach);
		}
		// warn event router about it
		sleeContainer.getEventRouter().activityStarted(acId);
		// add rollback tx action to remove state created
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				sleeContainer.getEventRouter().activityEnded(acId);				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException("failed to add rollback action to remove activity runtime resources",e);
		}
		return ac;
	}

	public ActivityContext getActivityContext(ActivityContextHandle ach, boolean updateAccessTime) {
		String acId = cacheData.getActivityContextId(ach);
		if (acId != null) {
			return new ActivityContext(ach,acId,updateAccessTime,null);
		}
		else {
			return null; 
		}
	}
	
	public ActivityContext getActivityContext(String acId, boolean updateAccessTime) {
		ActivityContextHandle ach = (ActivityContextHandle) cacheData.getActivityContextHandle(acId);
		if (ach != null) {
			return new ActivityContext(ach,acId,updateAccessTime,null);
		}
		else {
			return null; 
		}
	}

	public Set<ActivityContextHandle> getAllActivityContextsHandles() {
		return cacheData.getActivityContextHandles();
	}

	public Set<String> getAllActivityContextsIds() {
		return cacheData.getActivityContextIds();
	}
	
	public void removeActivityContext(final ActivityContext ac) {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing ac "+ac);
		}
		
		ac.activityEnded();
		
		cacheData.removeActivityContext(ac.getActivityContextHandle(),ac.getActivityContextId());
		
		sleeContainer.getEventRouter().activityEnded(ac.getActivityContextId());
		
		// add rollback tx action to recreate state removed
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				sleeContainer.getEventRouter().activityStarted(ac.getActivityContextId());				
			}
		};	
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException("failed to add rollback action to readd aruntime activity resources",e);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Activity context with handle "+ac.getActivityContextHandle()+" removed");
		}
	}
	
	public int getActivityContextCount() {		
		return getAllActivityContextsHandles().size();
	}
	
	@Override
	public String toString() {
		return "ActivityContext Factory: " 
			+ "\n+-- Number of ACs: " + getActivityContextCount();
	}

	public ActivityContextHandle getActivityContextHandle(String acId) {
		return (ActivityContextHandle) cacheData.getActivityContextHandle(acId);
	}
	
}