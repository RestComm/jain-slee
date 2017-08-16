/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.runtime.activity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.CacheType;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.activity.cache.ActivityCacheKey;
import org.mobicents.slee.runtime.activity.cache.ActivityCacheType;
import org.mobicents.slee.runtime.activity.cache.ActivityContextCacheDataWrapper;
import org.mobicents.slee.runtime.activity.cache.ActivityContextFactoryCacheData;
import org.restcomm.cluster.DataRemovalListener;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
	
	@Override
	public void sleeInitialization() {
		sleeContainer.getCluster(CacheType.ACTIVITIES).addDataRemovalListener(new DataRemovalClusterListener());		
	}
	
	@Override
	public void sleeStarting() {
		cacheData = new ActivityContextFactoryCacheData(sleeContainer.getCluster(CacheType.ACTIVITIES));		
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.AbstractSleeContainerModule#getSleeContainer()
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	LocalActivityContextImpl getLocalActivityContext(ActivityContextImpl ac) {
		final ActivityContextHandle ach = ac.getActivityContextHandle();
		LocalActivityContextImpl localActivityContext = localActivityContexts.get(ach);
		if (localActivityContext == null) {
			final LocalActivityContextImpl newLocalActivityContext = new LocalActivityContextImpl(ach, ac.getActivityFlags(), ac.getStringID(), this);
			localActivityContext = localActivityContexts.putIfAbsent(ach,newLocalActivityContext);
			if (localActivityContext == null) {
				localActivityContext = newLocalActivityContext;
				final EventRouterExecutor executor = sleeContainer.getEventRouter().getEventRouterExecutorMapper().getExecutor(ach);
				localActivityContext.setExecutorService(executor);
				executor.activityMapped(ach);
				TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
				if (txContext != null) {
					TransactionalAction txAction = new TransactionalAction() {
						@Override
						public void execute() {
							try {
								Runnable r = new Runnable() {									
									@Override
									public void run() {
										if (getActivityContext(ach) == null) {
											localActivityContexts.remove(ach);
											executor.activityUnmapped(ach);									
										}
									}
								};
								executor.execute(r);								
							}
							catch (Throwable e) {
								logger.error("Failed to rollback removal of AC local resources",e);
							}
							
						}
					};
					txContext.getAfterRollbackActions().add(txAction);
				}
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
		ActivityContextCacheDataWrapper activityContextCacheData =getCacheData(ach);
		if (activityContextCacheData.exists()) {
			throw new ActivityAlreadyExistsException(ach.toString());
		}
				
		ActivityContextImpl ac = new ActivityContextImpl(ach,activityContextCacheData,tracksIdleTime(ach,true),Integer.valueOf(activityFlags),this);
		if (logger.isDebugEnabled()) {
			logger.debug("Created activity context with handle "+ach);			
		}
		return ac;
	}

	private boolean tracksIdleTime(ActivityContextHandle ach, boolean updateLastAccessTime) {
		if(!updateLastAccessTime) {
			return false;
		}
		if (configuration.getTimeBetweenLivenessQueries() < 1) {
			return false;
		}
		return ach.getActivityType() == ActivityType.RA;
	}

	@Override
	public ActivityContextImpl getActivityContext(ActivityContextHandle ach) {
		return getActivityContext(ach,false);
	}
	
	@Override
	public ActivityContextImpl getActivityContext(ActivityContextHandle ach, boolean updateLastAccessTime) {
		ActivityContextCacheDataWrapper activityContextCacheData = getCacheData(ach);
		if (activityContextCacheData.exists()) {
			try {
				return new ActivityContextImpl(ach,activityContextCacheData,tracksIdleTime(ach, updateLastAccessTime),this);
			}
			catch (Throwable e) {
				logger.error("Failed to load AC.",e);
				// force cache data & local resources removal
				final LocalActivityContextImpl localActivityContext = localActivityContexts.remove(ach);
				if (localActivityContext != null) {
					localActivityContext.getExecutorService().activityUnmapped(ach);
				}
				activityContextCacheData.remove();
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	@Override
	public ActivityContext getActivityContext(String sid) {
		return getActivityContext(sid, false);
	}

	@Override
	public ActivityContext getActivityContext(String sid,
			boolean updateLastAccessTime) {
		// TODO add alternative strategy where there is a cache data mapping from sid to
		// ach, and configuration option to select strategy
		ActivityContextImpl ac = null;
		for (ActivityContextHandle ach : getAllActivityContextsHandles()) {
			ac = getActivityContext(ach, updateLastAccessTime);
			if (sid.equals(ac.getStringID(false))) {
				break;
			}
		}
		return ac;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.activity.ActivityContextFactory#getAllActivityContextsHandles()
	 */
	public Set<ActivityContextHandle> getAllActivityContextsHandles() {
		return cacheData.getActivityContextHandles();
	}
	
	@Override
	public Set<ActivityContextHandle> getAllActivityContextsHandles(ActivityType type) {
		return cacheData.getActivityContextHandlesByType(type);
	}
	
	public void removeActivityContext(final ActivityContextImpl ac) {

		if (doTraceLogs) {
			logger.trace("Removing activity context "+ac.getActivityContextHandle());
		}
		
		// remove runtime resources
		final LocalActivityContextImpl localActivityContext = localActivityContexts.remove(ac.getActivityContextHandle());
		if (localActivityContext != null) {
			localActivityContext.getExecutorService().activityUnmapped(ac.getActivityContextHandle());
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
		return getCacheData(ach).exists();
	}	
	
	@Override
	public String toString() {
		return "ActivityContext Factory: " 
			+ "\n+-- Local ACs: " + (localActivityContexts.size() > 20 ? localActivityContexts.size() : localActivityContexts.keySet())
			+ "\n+-- ACs: " + (getActivityContextCount() > 20 ? getActivityContextCount() : getAllActivityContextsHandles());
	}
	
	private ActivityContextCacheDataWrapper getCacheData(ActivityContextHandle ach)
	{
		return new ActivityContextCacheDataWrapper(ach, sleeContainer.getCluster(CacheType.ACTIVITIES));
	}
	
	private class DataRemovalClusterListener implements DataRemovalListener {		
		public void dataRemoved(Object key) {
			//metadata is the important part
			if(key instanceof ActivityCacheKey && ((ActivityCacheKey)key).getType()==ActivityCacheType.METADATA) {
				final ActivityContextHandle ach = ((ActivityCacheKey)key).getActivityHandle();
				final LocalActivityContextImpl localActivityContext = localActivityContexts.remove(ach);
				if(localActivityContext != null) {
					final EventRouterExecutor executor = localActivityContext.getExecutorService(); 
					if (executor != null) {
						executor.activityUnmapped(localActivityContext.getActivityContextHandle());
					}
					if(doTraceLogs) {
						logger.trace("Remotely removed local AC for "+ach);
					}
				}
			}
		}		
	}	
}