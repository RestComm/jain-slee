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
	
	public ProfileObjectImpl borrowObject() throws SLEEException {
		
		ProfileObjectImpl obj = null;
		try {
			obj = (ProfileObjectImpl) pool.borrowObject();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		if (logger.isTraceEnabled()) {
			logger.trace("borrowed object "+obj + " from " + this);
		}
		return obj; 
	}
	
	public void returnObject(ProfileObjectImpl profileObject) {				
		if (profileObject.getState() == ProfileObjectState.POOLED) {
			try {
				pool.returnObject(profileObject);
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(),e);
			}
			if (logger.isTraceEnabled()) {
				logger.trace("returned object "+profileObject + " to " + this);
			}
		}
		else {
			invalidateObject(profileObject);
		}
	}
	
	@Override
	public String toString() {
		return "Profile Object Pool : active objects = "+this.pool.getNumActive() + ", idle objects "+this.pool.getNumIdle();
	}

	public void close() throws Exception {
		if (logger.isTraceEnabled()) {
			logger.trace("close() " + this);
		}
		pool.close();		
	}

	public void invalidateObject(ProfileObject obj) {		
		try {
			pool.invalidateObject(obj);
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}			
		if (logger.isTraceEnabled()) {
			logger.trace("invalidated object "+obj + " to " + this);
		}
	}
}
