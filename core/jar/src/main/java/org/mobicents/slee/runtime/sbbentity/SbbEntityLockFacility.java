package org.mobicents.slee.runtime.sbbentity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 *
 */
public class SbbEntityLockFacility {

	private static final Logger logger = Logger.getLogger(SbbEntityLockFacility.class);
	
	/**
	 * 
	 */
	private final ConcurrentHashMap<String,ReentrantLock> locks = new ConcurrentHashMap<String, ReentrantLock>();
	
	/**
	 * 
	 */
	private final SleeContainer container;
	
	/**
	 * 
	 */
	public SbbEntityLockFacility(SleeContainer container) {
		this.container = container;
		final long period = 60*60*1000; //1h 
		container.getNonClusteredScheduler().scheduleAtFixedRate(new LocalResourcesGarbageCollectionTimerTask(), period, period, TimeUnit.MILLISECONDS);			
	}
	
	/**
	 * 
	 * @param sbbEntityId
	 * @return
	 */
	public ReentrantLock get(String sbbEntityId) {
		ReentrantLock lock = locks.get(sbbEntityId);
		if (lock == null) {
			final ReentrantLock newLock = new ReentrantLock();
			lock = locks.putIfAbsent(sbbEntityId, newLock);
			if (lock == null) {
				if(logger.isDebugEnabled()) {
					logger.debug(Thread.currentThread()+" put of lock "+newLock+" for "+sbbEntityId);
				}
				lock = newLock;
			}
		}
		return lock;
	}
	
	/**
	 * 
	 * @param sbbEntityId
	 * @return
	 */
	public ReentrantLock remove(String sbbEntityId) {
		if(logger.isDebugEnabled()) {
			logger.debug(Thread.currentThread()+" removed lock for "+sbbEntityId);
		}
		return locks.remove(sbbEntityId);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<String> getSbbEntitiesWithLocks() {
		return locks.keySet();
	}
	
	/**
	 * timer task to remove event router local resources for activities that are already gone
	 */
	private class LocalResourcesGarbageCollectionTimerTask implements Runnable {
		
		/* 
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				final Set<String> set = new HashSet<String>(locks.keySet());
				set.removeAll(SbbEntityFactory.getSbbEntityIDs());
				for (String sbbEntityId : set) {
					locks.remove(sbbEntityId);
				}	
			}
			catch (Throwable e) {
				logger.error("Failure in sbb entity lock facility local resources garbage collection",e);
			}
		}
	}
 }