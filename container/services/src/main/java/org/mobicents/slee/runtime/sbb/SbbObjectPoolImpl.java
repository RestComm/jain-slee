/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
