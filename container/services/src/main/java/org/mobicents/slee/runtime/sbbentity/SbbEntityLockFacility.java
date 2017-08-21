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

package org.mobicents.slee.runtime.sbbentity;

import org.apache.log4j.Logger;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.mobicents.slee.container.CacheType;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.cache.SbbEntityCacheKey;
import org.mobicents.slee.runtime.sbbentity.cache.SbbEntityCacheType;
import org.restcomm.cache.MobicentsCache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author martins
 *
 */
@Listener
public class SbbEntityLockFacility {

	private static final Logger logger = Logger.getLogger(SbbEntityLockFacility.class);
	private boolean doTraceLogs = logger.isTraceEnabled();
	private boolean doInfoLogs = logger.isInfoEnabled();
	
	/**
	 * 
	 */
	private final ConcurrentHashMap<SbbEntityID,ReentrantLock> locks = new ConcurrentHashMap<SbbEntityID, ReentrantLock>();
	
	/**
	 * 
	 */

	public SbbEntityLockFacility(SleeContainer container) {
		//container.getCluster().addDataRemovalListener(new DataRemovaClusterListener());
		MobicentsCache cache = container.getCluster(CacheType.SBB_ENTITIES).getMobicentsCache();
		if (!cache.isLocalMode()) {
			// SergeyLee: test
			cache.addListener(this);
		}
	}

	
	/**
	 * 
	 * @param sbbEntityId
	 * @return
	 */
	public ReentrantLock get(SbbEntityID sbbEntityId) {
		ReentrantLock lock = locks.get(sbbEntityId);
		if (lock == null) {
			final ReentrantLock newLock = new ReentrantLock();
			lock = locks.putIfAbsent(sbbEntityId, newLock);
			if (lock == null) {
				if(doInfoLogs) {
					logger.info(Thread.currentThread()+" put of lock "+newLock+" for "+sbbEntityId);
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
	public ReentrantLock remove(SbbEntityID sbbEntityId) {
		if(doInfoLogs) {
			logger.info(Thread.currentThread()+" removed lock for "+sbbEntityId);
		}
		return locks.remove(sbbEntityId);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<SbbEntityID> getSbbEntitiesWithLocks() {
		return locks.keySet();
	}
	
	// FIXME this does not work in the new sbbe tree model, ensure cluster framework gets such feature
	/*
	private class DataRemovaClusterListener implements DataRemovalListener {

		private final Fqn<?> baseFqn = null;
		
		@SuppressWarnings("unchecked")
		public void dataRemoved(Fqn arg0) {
			final SbbEntityID sbbEntityId = (SbbEntityID) arg0.getLastElement();
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
		
	}*/
	
	@SuppressWarnings("rawtypes")
	@CacheEntryRemoved
	public void onNodeRemovedEvent(CacheEntryRemovedEvent event) {
		if(!event.isOriginLocal() && !event.isPre()) {
			// remote node removal			
			if(event.getKey() instanceof SbbEntityCacheKey && ((SbbEntityCacheKey)event.getKey()).getType()==SbbEntityCacheType.METADATA) {
				SbbEntityCacheKey key=(SbbEntityCacheKey)event.getKey();				
				if(doInfoLogs) {
					logger.info("onNodeRemovedEvent( = " + key.getSbbEntityID() + " )");
				}
				SbbEntityID sbbEntityID=key.getSbbEntityID();
				
				if (locks.remove(sbbEntityID) != null) {
					if (doInfoLogs) {
						logger.info("Remotely removed lock for " + sbbEntityID);
					}
				}				
			}
		}
	}		
 }