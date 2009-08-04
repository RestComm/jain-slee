package org.mobicents.slee.runtime.cache;

import javax.slee.ServiceID;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.jboss.cache.Cache;

/**
 * The container's HA and FT data source.
 * 
 * @author martins
 * 
 */
public class MobicentsCache {

	private static Logger logger = Logger.getLogger(MobicentsCache.class);

	private final Cache<?,?> jBossCache;

	public MobicentsCache(Cache<?,?> jBossCache, TransactionManager txManager) {
		this.jBossCache = jBossCache;
		//this.jBossCache.addInterceptor(new MobicentsCommandInterceptor(txManager), 0);
		logger.info("SLEE Cache created.");
	}

	/**
	 * Retrieves an instance of an {@link ActivityContextCacheData} for the
	 * Activity Context with the specified id.
	 * 
	 * @param activityContextHandle
	 * @return
	 */
	public ActivityContextCacheData getActivityContextCacheData(
			Object activityContextHandle) {
		return new ActivityContextCacheData(activityContextHandle, jBossCache);
	}

	/**
	 * Retrieves an instance of an {@link ActivityContextFactoryCacheData}, the
	 * cache proxy for the Activity Context Factory
	 * 
	 * @return
	 */
	public ActivityContextFactoryCacheData getActivityContextFactoryCacheData() {
		return new ActivityContextFactoryCacheData(jBossCache);
	}

	/**
	 * Retrieves an instance of an
	 * {@link ActivityContextNamingFacilityCacheData}, the cache proxy for the
	 * Activity Context Naming Facility
	 * 
	 * @return
	 */
	public ActivityContextNamingFacilityCacheData getActivityContextNamingFacilityCacheData() {
		return new ActivityContextNamingFacilityCacheData(jBossCache);
	}

	/**
	 * Retrieves an instance of an {@link SbbEntityCacheData}, the cache proxy
	 * for the sbb entity table with the specified id.
	 * 
	 * @param sbbEntityId
	 * @return
	 */
	public SbbEntityCacheData getSbbEntityCacheData(String sbbEntityId) {
		return new SbbEntityCacheData(sbbEntityId, jBossCache);
	}

	/**
	 * Retrieves an instance of an {@link ServiceCacheData}, the cache proxy
	 * for the service with the specified id.
	 * 
	 * @param serviceID
	 * @return
	 */
	public ServiceCacheData getServiceCacheData(ServiceID serviceID) {
		return new ServiceCacheData(serviceID, jBossCache);
	}

	/**
	 * Retrieves an instance of an {@link TimerTasksCacheData}, the cache
	 * proxy for the Timer Facility
	 * 
	 * @return
	 */
	public TimerTasksCacheData getTimerFacilityCacheData() {
		return new TimerTasksCacheData(jBossCache);
	}
}
