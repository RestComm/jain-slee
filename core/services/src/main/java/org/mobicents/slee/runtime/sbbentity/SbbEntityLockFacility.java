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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.slee.ServiceID;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.NodeRemoved;
import org.jboss.cache.notifications.event.NodeRemovedEvent;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * 
 * @author martins
 *
 */
@CacheListener(sync = false)
public class SbbEntityLockFacility {

	private static final Logger logger = Logger.getLogger(SbbEntityLockFacility.class);
	private boolean doTraceLogs = logger.isTraceEnabled();
	
	/**
	 * 
	 */
	private final ConcurrentHashMap<SbbEntityID,ReentrantLock> locks = new ConcurrentHashMap<SbbEntityID, ReentrantLock>();
	
	/**
	 * 
	 */
	public SbbEntityLockFacility(SleeContainer container) {
		//container.getCluster().addDataRemovalListener(new DataRemovaClusterListener());
		MobicentsCache cache = container.getCluster().getMobicentsCache();
		if (!cache.isLocalMode()) {
			cache.getJBossCache().addCacheListener(this);
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
				if(doTraceLogs) {
					logger.trace(Thread.currentThread()+" put of lock "+newLock+" for "+sbbEntityId);
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
		if(doTraceLogs) {
			logger.trace(Thread.currentThread()+" removed lock for "+sbbEntityId);
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
	
	@NodeRemoved
	public void onNodeRemovedEvent(NodeRemovedEvent event) {
		if(!event.isOriginLocal() && !event.isPre()) {			
			// remote node removal
			Fqn<?> fqn = event.getFqn();
			if(doTraceLogs) {
				logger.trace("onNodeRemovedEvent( fqn = "+fqn+", size = "+fqn.size()+" )");
			}
			if (fqn.get(0).equals(SbbEntityFactoryCacheData.SBB_ENTITY_FACTORY_FQN_NAME)) {
				// is child of sbb entity factory cache data, i.e., /sbbe
				int fqnSize = fqn.size();				
				if (fqnSize < 3) {
					return;
				}
				SbbEntityID sbbEntityID = null;
				if (fqnSize == 3) {
					// /sbbe/serviceid/convergenceName root sbb entity
					ServiceID serviceID = (ServiceID) fqn.get(1);
					String convergenceName = (String) fqn.get(2);
					sbbEntityID = new RootSbbEntityID(serviceID, convergenceName);
					if(doTraceLogs) {
						logger.trace("Root sbb entity "+sbbEntityID+" was remotely removed, ensuring there is no local lock");
					}
				}
				else {
					// must end as /chd/chdRelationName/childId
					if(!fqn.get(fqnSize-3).equals(SbbEntityCacheData.CHILD_RELATIONs_CHILD_NODE_NAME)) {
						return;
					}
					// let get the party started and rebuild the sbb entity id!
					ServiceID serviceID = (ServiceID) fqn.get(1);
					String convergenceName = (String) fqn.get(2);
					sbbEntityID = new RootSbbEntityID(serviceID, convergenceName);
					int i = 3;
					while(fqnSize >= i+3) {
						// fqn get(i) is chd, skip 
						String childRelationName = (String) fqn.get(i+1);
						String childId = (String) fqn.get(i+2);
						sbbEntityID = new NonRootSbbEntityID(sbbEntityID, childRelationName, childId);
						i+=3;
					}
					if(doTraceLogs) {
						logger.trace("Non root sbb entity "+sbbEntityID+" was remotely removed, ensuring there is no local lock");
					}
				}
				if(locks.remove(sbbEntityID) != null) {
					if(doTraceLogs) {
						logger.trace("Remotely removed lock for "+sbbEntityID);
					}
				}
			}
		}
	}	
		
 }