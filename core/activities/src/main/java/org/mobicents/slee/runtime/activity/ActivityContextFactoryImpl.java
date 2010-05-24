package org.mobicents.slee.runtime.activity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;

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
public class ActivityContextFactoryImpl extends AbstractSleeContainerModule implements ActivityContextFactory {

	private static Logger logger = Logger.getLogger(ActivityContextFactoryImpl.class);
	
	/**
	 * a map with the local resources related with an activity context, which hold all runtime structures related to the activity
	 */
	private final ConcurrentHashMap<ActivityContextHandle, LocalActivityContextImpl> localActivityContexts = new ConcurrentHashMap<ActivityContextHandle, LocalActivityContextImpl>();
	
	private ActivityContextFactoryCacheData cacheData;
	
	private final ActivityManagementConfiguration configuration;
	
	private final static boolean doTraceLogs = logger.isTraceEnabled();
	
	public ActivityContextFactoryImpl(ActivityManagementConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityManagementConfiguration getConfiguration() {
		return configuration;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		cacheData = new ActivityContextFactoryCacheData(sleeContainer.getCluster());
		cacheData.create();
		// set local ACs GC timer
		final long period = 60*60*1000;  
		this.sleeContainer.getNonClusteredScheduler().scheduleAtFixedRate(new LocalResourcesGarbageCollectionTimerTask(), period, period, TimeUnit.MILLISECONDS);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.AbstractSleeContainerModule#getSleeContainer()
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextFactory#getLocalActivityContext(org.mobicents.slee.runtime.activity.ActivityContextHandle, boolean)
	 */
	public LocalActivityContextImpl getLocalActivityContext(
			ActivityContextHandle ach, boolean create) {
		LocalActivityContextImpl localActivityContext = localActivityContexts.get(ach);
		if (localActivityContext == null && create) {
			final LocalActivityContextImpl newLocalActivityContext = new LocalActivityContextImpl(ach,sleeContainer);
			localActivityContext = localActivityContexts.putIfAbsent(ach,newLocalActivityContext);
			if (localActivityContext == null) {
				localActivityContext = newLocalActivityContext;
				EventRouterExecutor executor = sleeContainer.getEventRouter().getEventRouterExecutorMapper().getExecutor(ach);
				localActivityContext.setExecutorService(executor);
				executor.activityMapped(ach);
			}
		}
		return localActivityContext;
	}
	
	public ActivityContextImpl createActivityContext(final ActivityContextHandle ach, int activityFlags) throws ActivityAlreadyExistsException {
		
		if (sleeContainer.getCongestionControl().refuseStartActivity()) {
			throw new SLEEException("congestion control refused activity start");
		}
		
		// create ac
		ActivityContextCacheData activityContextCacheData = new ActivityContextCacheData(ach, sleeContainer.getCluster());
		if (activityContextCacheData.exists()) {
			throw new ActivityAlreadyExistsException(ach.toString());
		}
				
		ActivityContextImpl ac = new ActivityContextImpl(ach,activityContextCacheData,tracksIdleTime(ach),Integer.valueOf(activityFlags),this);
		if (logger.isDebugEnabled()) {
			logger.debug("Created activity context with handle "+ach);			
		}
		return ac;
	}

	private boolean tracksIdleTime(ActivityContextHandle ach) {
		return ach.getActivityType() == ActivityType.RA;
	}

	public ActivityContextImpl getActivityContext(ActivityContextHandle ach) {
		ActivityContextCacheData activityContextCacheData = new ActivityContextCacheData(ach, sleeContainer.getCluster());
		if (activityContextCacheData.exists()) {
			return new ActivityContextImpl(ach,activityContextCacheData,tracksIdleTime(ach),this);
		}
		else {
			return null; 
		}
	}
	

	public Set<ActivityContextHandle> getAllActivityContextsHandles() {
		return cacheData.getActivityContextHandles();
	}
	
	public void removeActivityContext(final ActivityContextImpl ac) {

		if (doTraceLogs) {
			logger.trace("Removing activity context "+ac.getActivityContextHandle());
		}
		
		// remove runtime resources
		final LocalActivityContextImpl localActivityContext = localActivityContexts.remove(ac.getActivityContextHandle());
		if (localActivityContext != null) {
			localActivityContext.getExecutorService().activityUnmapped(ac.getActivityContextHandle());
			localActivityContext.setExecutorService(null);
		}
				
		if (logger.isDebugEnabled()) {
			logger.debug("Removed activity context with handle "+ac.getActivityContextHandle());			
		}
	}
	
	public int getActivityContextCount() {		
		return getAllActivityContextsHandles().size();
	}
	
	@Override
	public String toString() {
		return "ActivityContext Factory: " 
			+ "\n+-- Number of Local ACs: " + localActivityContexts.size()
			+ "\n+-- Number of ACs: " + getActivityContextCount();
	}
	
	/**
	 * Runnable to remove local resources for ACs that are already gone
	 */
	private class LocalResourcesGarbageCollectionTimerTask implements Runnable {
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (logger.isDebugEnabled()) {
				logger.debug("Running Local AC garbage collection task");
			}
			try {
				final Set<ActivityContextHandle> set = new HashSet<ActivityContextHandle>(localActivityContexts.keySet());
				set.removeAll(sleeContainer.getActivityContextFactory().getAllActivityContextsHandles());
				for (ActivityContextHandle ach : set) {
					if (logger.isDebugEnabled()) {
						logger.debug("Removing Local AC with handle "+ach);
					}
					localActivityContexts.remove(ach);
				}	
			}
			catch (Throwable e) {
				logger.error("Failure when running Local AC garbage collection task",e);
			}
		}
	}
}