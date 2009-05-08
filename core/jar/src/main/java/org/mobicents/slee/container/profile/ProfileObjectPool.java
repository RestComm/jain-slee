package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;

import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;

/**
 * Wrapper for apache commons objectpool, allows better logging and future exposure of jmx stats.
 * 
 * @author martins
 *
 */
public class ProfileObjectPool {

	private static final Logger logger = Logger.getLogger(ProfileObjectPool.class);
	
	private final ObjectPool pool;
	
	public ProfileObjectPool(ObjectPool pool) {
		this.pool = pool;
	}
	
	public ProfileObject borrowObject() throws SLEEException {
		
		ProfileObject obj = null;
		try {
			obj = (ProfileObject) pool.borrowObject();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("borrowed object "+obj + " from " + this);
		}
		return obj; 
	}
	
	public void returnObject(ProfileObject obj) {				
		try {
			pool.returnObject(obj);
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}	
		if (logger.isDebugEnabled()) {
			logger.debug("returned object "+obj + " to " + this);
		}
	}
	
	@Override
	public String toString() {
		return "Profile Object Pool : active objects = "+this.pool.getNumActive() + ", idle objects "+this.pool.getNumIdle();
	}

	public void close() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("close() " + this);
		}
		pool.close();		
	}

	public void invalidateObject(ProfileObject obj) throws Exception {		
		pool.invalidateObject(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("invalidated object "+obj + " to " + this);
		}
	}
}
