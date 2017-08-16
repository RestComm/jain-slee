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
