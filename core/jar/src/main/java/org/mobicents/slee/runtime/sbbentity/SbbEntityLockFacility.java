package org.mobicents.slee.runtime.sbbentity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

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
	 * @param sbbEntityId
	 * @return
	 */
	public ReentrantLock get(String sbbEntityId) {
		return locks.get(sbbEntityId);
	}
	
	/**
	 * 
	 * @param sbbEntityId
	 * @param reentrantLock
	 * @return
	 */
	public ReentrantLock putIfAbsent(String sbbEntityId, ReentrantLock reentrantLock) {
		if(logger.isDebugEnabled()) {
			logger.debug(Thread.currentThread()+" put of lock "+reentrantLock+" for "+sbbEntityId);
		}
		return locks.putIfAbsent(sbbEntityId,reentrantLock);
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
	
}
