package org.mobicents.slee.runtime.activity;

import java.util.Set;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.ActivityContextCacheData;
import org.mobicents.slee.runtime.cache.ActivityContextFactoryCacheData;
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
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach) throws ActivityAlreadyExistsException {
		return createActivityContext(ach,ActivityFlags.NO_FLAGS);
	}
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach, int activityFlags) throws ActivityAlreadyExistsException {
		
		// create ac
		ActivityContextCacheData activityContextCacheData = sleeContainer.getCache().getActivityContextCacheData(ach);
		if (activityContextCacheData.exists()) {
			throw new ActivityAlreadyExistsException(ach.toString());
		}
				
		ActivityContext ac = new ActivityContext(ach,activityContextCacheData,tracksIdleTime(ach),Integer.valueOf(activityFlags));
		if (logger.isDebugEnabled()) {
			logger.debug("Created ac "+ac+" for handle "+ach);
		}
		// warn event router about it
		sleeContainer.getEventRouter().activityStarted(ach);
		// add rollback tx action to remove state created
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				sleeContainer.getEventRouter().activityEnded(ach);				
			}
		};
		try {
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		} catch (SystemException e) {
			throw new SLEEException("failed to add rollback action to remove activity runtime resources",e);
		}
		return ac;
	}

	private boolean tracksIdleTime(ActivityContextHandle ach) {
		return ach.getActivityType() == ActivityType.RA;
	}

	public ActivityContext getActivityContext(ActivityContextHandle ach) {
		ActivityContextCacheData activityContextCacheData = sleeContainer.getCache().getActivityContextCacheData(ach);
		if (activityContextCacheData.exists()) {
			return new ActivityContext(ach,activityContextCacheData,tracksIdleTime(ach));
		}
		else {
			return null; 
		}
	}
	

	public Set<ActivityContextHandle> getAllActivityContextsHandles() {
		return cacheData.getActivityContextHandles();
	}
	
	public void removeActivityContext(final ActivityContext ac) {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing ac "+ac);
		}
		
		final ActivityContextHandle ach = ac.getActivityContextHandle();
		
		// do end procedures 
		ac.activityEnded();
		// remove runtime resources
		sleeContainer.getEventRouter().activityEnded(ach);
		// add rollback tx action to recreate state removed
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				sleeContainer.getEventRouter().activityStarted(ach);				
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
	
}