 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.util;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class SharedHashMapCache implements CacheUtility {

	private static SharedHashMapCache instance;

	private Logger log = Logger.getLogger(SharedHashMapCache.class);

	private final static String CACHE_JNDI_NAME = "java:jayway";

	public SharedHashMapCache() throws NamingException {
		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			ctx.lookup(CACHE_JNDI_NAME);
			
			rebind();
			
		} catch (NamingException e) {
			log.info("Binding cache to " + CACHE_JNDI_NAME);
			// This is expected if there is already a TreeCache in JNDI
			try {
				ctx.bind(CACHE_JNDI_NAME, new HashMap());
			} catch (NamingException ex) {
				log.error("Error in constructor, could not bind cache", ex);
				throw ex;
			}
		}
	}
	
	private void rebind() {
		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			ctx.unbind(CACHE_JNDI_NAME);
			ctx.bind(CACHE_JNDI_NAME, new HashMap());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized static SharedHashMapCache getInstance()
			throws NamingException {
		if (instance == null) {
			instance = new SharedHashMapCache();
		}

		return instance;
	}

	public void put(String key, Object value) {
		try {
			Map cache = getCache();
			cache.put(key, value);
			log.debug("Putting " + value + " in key " + key);
		} catch (Exception e) {
			log.error("Exception caught while putting " + key + " + " + value
					+ " into Cache", e);
		}

	}

	public Object get(String key) {
		Object returnValue = null;
		try {
			Map cache = getCache();
			returnValue = cache.get(key);
			log.debug("Getting value " + returnValue + " for key " + key);
		} catch (Exception e) {
			log.error("Exception caught while getting key " + key);
		}

		return returnValue;
	}

	private Map getCache() throws NamingException {
		Map cache = null;
		try {
			InitialContext ctx = new InitialContext();
			cache = (Map) ctx.lookup(CACHE_JNDI_NAME);
		} catch (NamingException e) {
			log.error("Error looking up cache", e);
			throw e;
		}

		return cache;
	}

	public String toString() {
		Map cache = null;
		try {
			cache = getCache();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.toString();
	}
}
