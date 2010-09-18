package org.mobicents.slee.runtime.activity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.mobicents.cluster.DataRemovalListener;
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
		sleeContainer.getCluster().addDataRemovalListener(new DataRemovaClusterListener());
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
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#createActivityContext(org.mobicents.slee.container.activity.ActivityContextHandle, int)
	 */
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

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#getActivityContext(org.mobicents.slee.container.activity.ActivityContextHandle)
	 */
	public ActivityContextImpl getActivityContext(ActivityContextHandle ach) {
		ActivityContextCacheData activityContextCacheData = new ActivityContextCacheData(ach, sleeContainer.getCluster());
		if (activityContextCacheData.exists()) {
			return new ActivityContextImpl(ach,activityContextCacheData,tracksIdleTime(ach),this);
		}
		else {
			return null; 
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#getAllActivityContextsHandles()
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#getActivityContextCount()
	 */
	public int getActivityContextCount() {		
		return getAllActivityContextsHandles().size();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#activityContextExists(org.mobicents.slee.container.activity.ActivityContextHandle)
	 */
	public boolean activityContextExists(ActivityContextHandle ach) {
		return new ActivityContextCacheData(ach, sleeContainer.getCluster()).exists();
	}	
	
	@Override
	public String toString() {
		return "ActivityContext Factory: " 
			+ "\n+-- Number of Local ACs: " + localActivityContexts.size()
			+ "\n+-- Number of ACs: " + getActivityContextCount();
	}
	
	private class DataRemovaClusterListener implements DataRemovalListener {
		
		@SuppressWarnings("unchecked")
		public void dataRemoved(Fqn arg0) {
			final ActivityContextHandle ach = (ActivityContextHandle) arg0.getLastElement();
			final LocalActivityContextImpl localActivityContext = localActivityContexts.remove(ach);
			if(localActivityContext != null) {
				final EventRouterExecutor executor = localActivityContext.getExecutorService(); 
				if (executor != null) {
					executor.activityUnmapped(localActivityContext.getActivityContextHandle());
				}
				localActivityContext.setExecutorService(null);
				if(doTraceLogs) {
					logger.trace("Remotely removed local AC for "+ach);
				}
			}
		}

		@SuppressWarnings("unchecked")
		public Fqn getBaseFqn() {
			return cacheData.getNodeFqn();
		}
		
	}
}