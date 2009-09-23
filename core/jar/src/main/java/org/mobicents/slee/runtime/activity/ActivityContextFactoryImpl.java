package org.mobicents.slee.runtime.activity;

import java.util.Set;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.ActivityContextCacheData;
import org.mobicents.slee.runtime.cache.ActivityContextFactoryCacheData;

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
		cacheData = new ActivityContextFactoryCacheData(sleeContainer.getCluster());
		cacheData.create();		
	}
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach) throws ActivityAlreadyExistsException {
		return createActivityContext(ach,ActivityFlags.NO_FLAGS);
	}
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach, int activityFlags) throws ActivityAlreadyExistsException {
		
		// create ac
		ActivityContextCacheData activityContextCacheData = new ActivityContextCacheData(ach, sleeContainer.getCluster());
		if (activityContextCacheData.exists()) {
			throw new ActivityAlreadyExistsException(ach.toString());
		}
				
		ActivityContext ac = new ActivityContext(ach,activityContextCacheData,tracksIdleTime(ach),Integer.valueOf(activityFlags));
		if (logger.isDebugEnabled()) {
			logger.debug("Created ac "+ac+" for handle "+ach);
		}
		return ac;
	}

	private boolean tracksIdleTime(ActivityContextHandle ach) {
		return ach.getActivityType() == ActivityType.RA;
	}

	public ActivityContext getActivityContext(ActivityContextHandle ach) {
		ActivityContextCacheData activityContextCacheData = new ActivityContextCacheData(ach, sleeContainer.getCluster());
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