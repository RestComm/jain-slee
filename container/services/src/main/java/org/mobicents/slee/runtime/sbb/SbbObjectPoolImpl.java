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

package org.mobicents.slee.runtime.sbb;

import javax.slee.ServiceID;

import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;

/**
 * Wrapper for apache commons objectpool, allows better logging and future exposure of jmx stats.
 * 
 * @author martins
 *
 */
public class SbbObjectPoolImpl implements SbbObjectPool {

	private static final Logger logger = Logger.getLogger(SbbObjectPoolImpl.class);
	
	private final ObjectPool pool;
	private final SbbComponent sbbComponent;
	private final ServiceID serviceID;
	
	public SbbObjectPoolImpl(SbbComponent sbbComponent, ServiceID serviceID, ObjectPool pool) {
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
	
	public SbbObject borrowObject() throws java.lang.Exception, java.util.NoSuchElementException, java.lang.IllegalStateException {		
		final SbbObject obj = (SbbObject) pool.borrowObject();
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
