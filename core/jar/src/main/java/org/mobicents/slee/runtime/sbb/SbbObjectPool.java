package org.mobicents.slee.runtime.sbb;

import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;

/**
 * Wrapper for apache commons objectpool, allows better logging and future exposure of jmx stats.
 * 
 * @author martins
 *
 */
public class SbbObjectPool {

	private static final Logger logger = Logger.getLogger(SbbObjectPool.class);
	
	private final ObjectPool pool;
	
	public SbbObjectPool(ObjectPool pool) {
		this.pool = pool;
	}
	
	public Object borrowObject() throws java.lang.Exception, java.util.NoSuchElementException, java.lang.IllegalStateException {
		
		Object obj = pool.borrowObject();
		if (logger.isDebugEnabled()) {
			logger.debug("borrowed object "+obj + " from " + this);
		}
		return obj; 
	}
	
	public void returnObject(SbbObject obj) throws java.lang.Exception {				
		pool.returnObject(obj);	
		if (logger.isDebugEnabled()) {
			logger.debug("returned object "+obj + " to " + this);
		}
	}
	
	@Override
	public String toString() {
		return "Sbb Object Pool : active objects = "+this.pool.getNumActive() + ", idle objects "+this.pool.getNumIdle();
	}

	public void close() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("close() " + this);
		}
		pool.close();		
	}

	public void invalidateObject(SbbObject obj) throws Exception {		
		pool.invalidateObject(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("invalidated object "+obj + " to " + this);
		}
	}
}
