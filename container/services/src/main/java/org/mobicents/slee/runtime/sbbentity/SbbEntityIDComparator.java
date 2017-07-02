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

import java.util.Comparator;
import java.util.LinkedList;

import org.mobicents.slee.container.CacheType;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.cache.SbbEntityCacheDataWrapper;

/**
 * Comparator used to sort a set of {@link SbbEntityID} by priority.
 * 
 * @author martins
 * 
 */
public class SbbEntityIDComparator implements Comparator<SbbEntityID> {

	private final SbbEntityFactoryImpl sbbEntityFactory;
	
	public SbbEntityIDComparator(SbbEntityFactoryImpl sbbEntityFactory) {
		this.sbbEntityFactory = sbbEntityFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(SbbEntityID sbbEntityID1, SbbEntityID sbbEntityID2) {		
		if (sbbEntityID1 == sbbEntityID2) {
			return 0;
		}		
		if (sbbEntityID1 == null) {
			return 1;			
		}		
		if (sbbEntityID2 == null) {
			return -1;			
		}		
		return higherPrioritySbb(sbbEntityID1, sbbEntityID2);					
	}

	private int higherPrioritySbb(SbbEntityID sbbEntityID1, SbbEntityID sbbEntityID2) {

		final LinkedList<Byte> stack1 = priorityOfSbb(sbbEntityID1);
		final LinkedList<Byte> stack2 = priorityOfSbb(sbbEntityID2);
		
		// stacks may be null if sbb entity is removed concurrently
		if (stack1 == null) {
			if (stack2 == null) {
				return 0;
			} else {
				return 1;
			}
		}
		else {
			if (stack2 == null) {
				return -1;
			}
		}
		
		// both are non null
		byte priority1 = 0;
		byte priority2 = 0;
		while (true) {
			priority1 = stack1.removeFirst();
			priority2 = stack2.removeFirst();
			if (priority1 == priority2) {
				// sbb entities have the same ancestor.
				if (stack1.isEmpty()) {
					if (stack2.isEmpty()) {
						// compare id as string
						return sbbEntityID1.toString().compareTo(sbbEntityID2.toString());
					}
					else {
						return -1;
					}
				} else if (stack2.isEmpty()) {
					return 1;
				}
			} else if (priority1 > priority2) {
				return -1;
			}
			else {
				return 1;
			}							
		}
	}

	private LinkedList<Byte> priorityOfSbb(SbbEntityID sbbEntityID) {
		final LinkedList<Byte> list = new LinkedList<Byte>();
		// push priority of all non root sbb entities
		SbbEntityCacheDataWrapper sbbEntityCacheData = null;
		while (!sbbEntityID.isRootSbbEntity()) {
			sbbEntityCacheData = new SbbEntityCacheDataWrapper(sbbEntityID,sbbEntityFactory.getSleeContainer().getCluster(CacheType.SBB_ENTITIES));
			if(!sbbEntityCacheData.exists()) {
				// edge case where a sbb entity was concurrently removed, ignore this sbb entity
				return null;
			}
			list.addFirst(Byte.valueOf(sbbEntityCacheData.getPriority()));
			sbbEntityID = sbbEntityID.getParentSBBEntityID();
		}
		// push the root one
		sbbEntityCacheData = new SbbEntityCacheDataWrapper(sbbEntityID,sbbEntityFactory.getSleeContainer().getCluster(CacheType.SBB_ENTITIES));
		if(!sbbEntityCacheData.exists()) {
			// edge case where a sbb entity was concurrently removed, ignore this sbb entity
			return null;
		}
		list.addFirst(Byte.valueOf(sbbEntityCacheData.getPriority()));
		return list;
	}
	
}
