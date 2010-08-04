package org.mobicents.slee.runtime.sbbentity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.mobicents.cluster.DataRemovalListener;
import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * @author martins
 *
 */
public class SbbEntityLockFacility {

	private static final Logger logger = Logger.getLogger(SbbEntityLockFacility.class);
	private boolean doTraceLogs = logger.isTraceEnabled();
	
	/**
	 * 
	 */
	private final ConcurrentHashMap<String,ReentrantLock> locks = new ConcurrentHashMap<String, ReentrantLock>();
	
	/**
	 * 
	 */
	public SbbEntityLockFacility(SleeContainer container) {
		container.getCluster().addDataRemovalListener(new DataRemovaClusterListener());			
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
				if(doTraceLogs) {
					logger.trace(Thread.currentThread()+" put of lock "+newLock+" for "+sbbEntityId);
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
		if(doTraceLogs) {
			logger.trace(Thread.currentThread()+" removed lock for "+sbbEntityId);
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
	
	private class DataRemovaClusterListener implements DataRemovalListener {

		private final Fqn<?> baseFqn = Fqn.fromElements(SbbEntityCacheData.parentNodeFqn);
		
		@SuppressWarnings("unchecked")
		public void dataRemoved(Fqn arg0) {
			final String sbbEntityId = (String) arg0.getLastElement();
			if(locks.remove(sbbEntityId) != null) {
				if(doTraceLogs) {
					logger.trace("Remotely removed lock for "+sbbEntityId);
				}
			}
		}

		@SuppressWarnings("unchecked")
		public Fqn getBaseFqn() {
			return baseFqn;
		}
		
	}
 }