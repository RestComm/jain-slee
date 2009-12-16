package org.mobicents.slee.runtime.sbb;

import javax.slee.ServiceID;

import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.SbbComponent;

/**
 * Wrapper for apache commons objectpool, allows better logging and future exposure of jmx stats.
 * 
 * @author martins
 *
 */
public class SbbObjectPool {

	private static final Logger logger = Logger.getLogger(SbbObjectPool.class);
	
	private final ObjectPool pool;
	private final SbbComponent sbbComponent;
	private final ServiceID serviceID;
	
	public SbbObjectPool(SbbComponent sbbComponent, ServiceID serviceID, ObjectPool pool) {
		this.sbbComponent = sbbComponent;
		this.serviceID = serviceID;
		this.pool = pool;
	}
	
	/**
	 * @return the sbbComponent
	 */
	public SbbComponent getSbbComponent() {
		return sbbComponent;
	}
	
	/**
	 * @return the serviceID
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}
	
	public Object borrowObject() throws java.lang.Exception, java.util.NoSuchElementException, java.lang.IllegalStateException {		
		final Object obj = pool.borrowObject();
		if (logger.isTraceEnabled()) {
			logger.trace("borrowed object "+obj + " from " + this);
		}
		return obj; 
	}
	
	public void returnObject(SbbObject obj) throws java.lang.Exception {				
		pool.returnObject(obj);	
		if (logger.isTraceEnabled()) {
			logger.trace("returned object "+obj + " to " + this);
		}
	}
	
	@Override
	public String toString() {
		return "Sbb Object Pool ( "+sbbComponent+", "+serviceID+" ) : active objects = "+this.pool.getNumActive() + ", idle objects "+this.pool.getNumIdle();
	}

	public void close() throws Exception {
		if (logger.isTraceEnabled()) {
			logger.trace("close() " + this);
		}
		pool.close();		
	}

	public void invalidateObject(SbbObject obj) throws Exception {		
		pool.invalidateObject(obj);
		if (logger.isTraceEnabled()) {
			logger.trace("invalidated object "+obj + " to " + this);
		}
	}
}
